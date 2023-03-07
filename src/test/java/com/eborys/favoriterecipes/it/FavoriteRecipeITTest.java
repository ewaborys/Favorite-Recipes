package com.eborys.favoriterecipes.it;

import com.eborys.favoriterecipes.contract.model.Recipe;
import com.eborys.favoriterecipes.repository.UserRecipeRepository;
import com.eborys.favoriterecipes.repository.dao.UserRecipe;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.eborys.favoriterecipes.util.RecipeUtils.createTestRecipe;
import static com.eborys.favoriterecipes.util.RecipeUtils.createTestUserRecipe;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class FavoriteRecipeITTest {

    @Container
    private static final MongoDBContainer mongo = new MongoDBContainer("mongo:6.0.4");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRecipeRepository userRecipeRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    public void tearDown() {
        userRecipeRepository.deleteAll();
    }

    @Test
    void testGetFavoriteRecipes() throws Exception {
        // given
        var recipe = createTestUserRecipe();
        userRecipeRepository.save(recipe);
        // when/then
        mockMvc.perform(get("/recipes/favorite")
                .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpwYXNzd29yZA=="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name", is(recipe.getName())))
                .andExpect(jsonPath("$[0].category", is(recipe.getCategory())))
                .andExpect(jsonPath("$[0].ingredients", is(recipe.getIngredients())))
                .andExpect(jsonPath("$[0].instructions", is(recipe.getInstructions())))
                .andExpect(jsonPath("$[0].numberOfServings", is(recipe.getNumberOfServings())))
                .andExpect(jsonPath("$[0].username").doesNotExist());
    }

    @Test
    void testAddFavoriteRecipe() throws Exception {
        // given
        var recipe = createTestRecipe();
        // when/then
        mockMvc.perform(post("/recipes/favorite")
                        .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpwYXNzd29yZA==")
                        .content(objectMapper.writeValueAsString(recipe))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is(recipe.getName())))
                .andExpect(jsonPath("$.category", is(recipe.getCategory())))
                .andExpect(jsonPath("$.ingredients", is(recipe.getIngredients())))
                .andExpect(jsonPath("$.instructions", is(recipe.getInstructions())))
                .andExpect(jsonPath("$.numberOfServings", is(recipe.getNumberOfServings())))
                .andExpect(jsonPath("$.username").doesNotExist());
        var recipes = userRecipeRepository.findAll();
        assertEquals(1, recipes.size());
        assertNotNull(recipes.get(0).getId());
        var userRecipe = createTestUserRecipe();
        userRecipe.setId(recipes.get(0).getId());
        assertEquals(userRecipe, recipes.get(0));
    }

    @Test
    void testDeleteFavoriteRecipe() throws Exception {
        // given
        var userRecipe = createTestUserRecipe();
        userRecipe = userRecipeRepository.save(userRecipe);

        // when/then
        mockMvc.perform(delete("/recipes/favorite/" + userRecipe.getId().toString())
                        .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpwYXNzd29yZA=="))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        assertTrue(userRecipeRepository.findAll().isEmpty());
    }

    @Test
    void testUpdateFavoriteRecipe() throws Exception {
        // given
        var userRecipe = createTestUserRecipe();
        var recipe = createTestRecipe();
        recipe.setName("Scrambled Eggs");
        userRecipe = userRecipeRepository.save(userRecipe);
        recipe.setId(userRecipe.getId().toString());

        // when/then
        mockMvc.perform(put("/recipes/favorite/" + userRecipe.getId().toString())
                        .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpwYXNzd29yZA==")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(recipe)));

        userRecipe.setName("Scrambled Eggs");
        var recipes = userRecipeRepository.findAll();
        assertEquals(1, recipes.size());
        assertEquals(userRecipe, recipes.get(0));
    }

    @CsvFileSource(numLinesToSkip = 1, files = {"src/test/resources/csv/filteringFavoriteRecipes.csv"})
    @ParameterizedTest
    void testFilteringFavoriteRecipes(String filteringQuery, String expectedRecipeNames) throws Exception {
        // given
        Set<String> expectedRecipeNamesList = expectedRecipeNames == null || expectedRecipeNames.isBlank() ? Set.of()
                : Arrays.stream(expectedRecipeNames.split(",")).map(String::trim).collect(Collectors.toSet());
        List<UserRecipe> userRecipes = objectMapper.readValue(
                Path.of("src", "test", "resources", "json", "sampleRecipesForFiltering.json").toFile(),
                new TypeReference<>() {});
        userRecipeRepository.saveAll(userRecipes);
        // when/then
        var response = mockMvc.perform(get("/recipes/favorite?" + filteringQuery)
                        .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpwYXNzd29yZA=="))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<Recipe> filteredRecipes = objectMapper.readValue(response, new TypeReference<>() {});
        assertEquals(expectedRecipeNamesList, filteredRecipes.stream().map(Recipe::getName).collect(Collectors.toSet()));
    }

    @Test
    void testUnauthorized() throws Exception {
        mockMvc.perform(get("/recipes/favorite"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateFavoriteRecipeBadRequest() throws Exception {
        // given
        var recipe = createTestRecipe();
        recipe.setName("Scrambled Eggs");

        // when/then
        mockMvc.perform(put("/recipes/favorite/6133f7e16af0806d43cbb3c3")
                        .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpwYXNzd29yZA==")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipe)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {"message":"Recipe with id=6133f7e16af0806d43cbb3c3 not found"}
                        """));
    }

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongo::getHost);
        registry.add("spring.data.mongodb.port", () -> mongo.getMappedPort(27017));
    }
}
