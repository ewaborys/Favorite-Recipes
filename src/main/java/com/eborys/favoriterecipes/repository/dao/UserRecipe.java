package com.eborys.favoriterecipes.repository.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("user_recipes")
@Data
public class UserRecipe {

    @Id
    private Long id;

    private String name;

    private String instructions;

    private List<String> ingredients;

    private String category;

    private int numberOfServings;

    private String username;
}
