package com.eborys.favoriterecipes.service;

import com.eborys.favoriterecipes.contract.model.Recipe;
import com.eborys.favoriterecipes.exceptions.RecipeException;
import com.eborys.favoriterecipes.mapper.RecipeMapper;
import com.eborys.favoriterecipes.repository.CustomUserRecipeRepository;
import com.eborys.favoriterecipes.repository.UserRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final UserRecipeRepository userRecipeRepository;
    private final CustomUserRecipeRepository customUserRecipeRepository;

    public List<Recipe> getFavoriteRecipes(String username, List<String> includeCategories,
                                           List<String> excludeCategories, Integer numberOfServings,
                                           List<String> includeIngredients, List<String> excludeIngredients,
                                           String instructionsContainText) {
        return customUserRecipeRepository.findBy(username, includeCategories, excludeCategories, numberOfServings,
                        includeIngredients, excludeIngredients, instructionsContainText).stream()
                .map(RecipeMapper.INSTANCE::map).toList();
    }

    public Recipe addFavoriteRecipe(String username, Recipe recipe) {
        return RecipeMapper.INSTANCE.map(userRecipeRepository.save(RecipeMapper.INSTANCE.map(recipe, username)));
    }

    public Recipe updateFavoriteRecipe(String username, Recipe recipe) {
        return userRecipeRepository.findByIdAndUsername(new ObjectId(recipe.getId()), username)
                .map(it -> RecipeMapper.INSTANCE.mapSourceToTarget(recipe, it))
                .map(userRecipeRepository::save)
                .map(RecipeMapper.INSTANCE::map)
                .orElseThrow(() -> new RecipeException("Recipe with id=" + recipe.getId() + " not found"));
    }

    public void deleteFavoriteRecipe(String username, ObjectId id) {
        userRecipeRepository.findByIdAndUsername(id, username)
                .ifPresent(userRecipeRepository::delete);
    }
}
