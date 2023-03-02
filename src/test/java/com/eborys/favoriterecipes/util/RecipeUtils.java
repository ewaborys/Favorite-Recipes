package com.eborys.favoriterecipes.util;

import com.eborys.favoriterecipes.contract.model.Recipe;
import com.eborys.favoriterecipes.contract.model.RecipeInput;
import com.eborys.favoriterecipes.repository.dao.UserRecipe;

import java.util.List;

public class RecipeUtils {
    public static UserRecipe createTestUserRecipe() {
        return UserRecipe.builder()
                .name("aaa")
                .category("bbb")
                .ingredients(List.of("Bacon", "White Wine Vinegar", "Butter"))
                .instructions("First make the Hollandaise sauce. Put the lemon juice")
                .numberOfServings(1)
                .username("user").build();
    }

    public static Recipe createTestRecipe() {
        return new Recipe()
                .name("aaa")
                .category("bbb")
                .ingredients(List.of("Bacon", "White Wine Vinegar", "Butter"))
                .instructions("First make the Hollandaise sauce. Put the lemon juice")
                .numberOfServings(1);
    }

    public static RecipeInput createTestRecipeInput() {
        return new RecipeInput()
                .name("aaa")
                .category("bbb")
                .ingredients(List.of("Bacon", "White Wine Vinegar", "Butter"))
                .instructions("First make the Hollandaise sauce. Put the lemon juice")
                .numberOfServings(1);
    }
}
