package com.eborys.favoriterecipes.mapper;

import com.eborys.favoriterecipes.util.RecipeUtils;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeMapperTest {


    @CsvSource({
            ",,false",
            "6133f7e16af0806d43cbb3c3,6133f7e16af0806d43cbb3c3,false",
            "This is a sample string.,,true"
    })
    @ParameterizedTest
    void testObjectIdToStringMapping(String string, String objectIdString, boolean throwsException) {
        ObjectId mappedObjectId;
        try {
            mappedObjectId = RecipeMapper.INSTANCE.map(string);
        } catch (Exception e) {
            assertTrue(throwsException);
            return;
        }
        assertFalse(throwsException);
        ObjectId objectId = objectIdString == null ? null : new ObjectId(objectIdString);
        assertEquals(objectId, mappedObjectId);
    }

    @CsvSource({
            ",",
            "6133f7e16af0806d43cbb3c3,6133f7e16af0806d43cbb3c3",
    })
    @ParameterizedTest
    void testStringToObjectIdMapping(String objectIdString, String string) {
        ObjectId objectId = objectIdString == null ? null : new ObjectId(objectIdString);
        var mappedString = RecipeMapper.INSTANCE.map(objectId);
        assertEquals(string, mappedString);
    }

    @Test
    void testRecipeToUserRecipeMapping() {
        var recipe = RecipeUtils.createTestRecipe();
        var userRecipe = RecipeUtils.createTestUserRecipe();
        assertEquals(userRecipe, RecipeMapper.INSTANCE.map(recipe, userRecipe.getUsername()));
    }

    @Test
    void testUserRecipeToRecipeMapping() {
        var recipe = RecipeUtils.createTestRecipe();
        var userRecipe = RecipeUtils.createTestUserRecipe();
        assertEquals(recipe, RecipeMapper.INSTANCE.map(userRecipe));
    }

    @Test
    void testRecipeWithoutIdDtoToRecipeMapping() {
        var recipe = RecipeUtils.createTestRecipe();
        var recipeDto = RecipeUtils.createTestRecipeInput();
        assertEquals(recipe, RecipeMapper.INSTANCE.map(recipeDto));
    }

    @Test
    void testRecipeWithoutIdDtoToRecipeMappingWithId() {
        var recipe = RecipeUtils.createTestRecipe();
        recipe.setId("6133f7e16af0806d43cbb3c3");
        var recipeDto = RecipeUtils.createTestRecipeInput();
        assertEquals(recipe, RecipeMapper.INSTANCE.map(recipeDto, new ObjectId(recipe.getId())));
    }

}
