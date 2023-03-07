package com.eborys.favoriterecipes.repository.dao;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "user_recipes", language = "en")
@Data
@Builder
public class UserRecipe {

    @Id
    private ObjectId id;

    @NotEmpty
    private String name;

    @NotEmpty
    @TextIndexed
    private String instructions;

    @NotEmpty
    private List<String> ingredients;

    @NotEmpty
    private String category;

    @NotNull
    private Integer numberOfServings;

    @NotEmpty
    private String username;
}
