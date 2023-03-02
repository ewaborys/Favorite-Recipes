package com.eborys.favoriterecipes.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipesController {


    @GetMapping("/recipes")
    Object getRecipes() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @GetMapping("/recipes/favorite")
    Object getFavoriteRecipes() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

    }
}
