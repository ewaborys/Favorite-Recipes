package com.eborys.favoriterecipes.service;

import com.eborys.favoriterecipes.model.Recipe;
import com.eborys.favoriterecipes.repository.UserRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final UserRecipeRepository userRecipeRepository;

    public List<Recipe> getFavoriteRecipes(String username) {
        return userRecipeRepository.findByUsername(username);
    }
}
