package com.eborys.favoriterecipes.model;

import lombok.Data;

import java.util.List;

@Data
public class Recipe {
    private final long id;
    private final String name;
    private final String instructions;
    private final List<String> ingredients;
    private final int numberOfServings;
    private final String category;
}
