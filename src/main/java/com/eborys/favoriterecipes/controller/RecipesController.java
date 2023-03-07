package com.eborys.favoriterecipes.controller;

import com.eborys.favoriterecipes.contract.api.RecipesApi;
import com.eborys.favoriterecipes.contract.model.Recipe;
import com.eborys.favoriterecipes.contract.model.RecipeInput;
import com.eborys.favoriterecipes.mapper.RecipeMapper;
import com.eborys.favoriterecipes.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecipesController implements RecipesApi {

    private final RecipeService recipeService;

    @Override
    public ResponseEntity<List<Recipe>> recipesFavoriteGet(
            List<String> includeCategories, List<String> excludeCategories, Integer numberOfServings,
            List<String> includeIngredients, List<String> excludeIngredients, String instructionsContain) {
        String username = getUsername();
        return ResponseEntity.ok(recipeService.getFavoriteRecipes(username, includeCategories, excludeCategories,
                numberOfServings, includeIngredients, excludeIngredients, instructionsContain));
    }

    @Override
    public ResponseEntity<Recipe> recipesFavoritePost(
            RecipeInput recipeInput) {
        String username = getUsername();
        Recipe recipe = RecipeMapper.INSTANCE.map(recipeInput);
        return ResponseEntity.ok(recipeService.addFavoriteRecipe(username, recipe));
    }

    @Override
    public ResponseEntity<Void> recipesFavoriteRecipeIdDelete(String recipeId) {
        String username = getUsername();
        recipeService.deleteFavoriteRecipe(username, new ObjectId(recipeId));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Recipe> recipesFavoriteRecipeIdPut(
            String recipeId, RecipeInput recipeInput) {
        String username = getUsername();
        Recipe recipe = RecipeMapper.INSTANCE.map(recipeInput);
        recipe.id(recipeId);
        return ResponseEntity.ok(recipeService.updateFavoriteRecipe(username, recipe));
    }

    private static String getUsername() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
