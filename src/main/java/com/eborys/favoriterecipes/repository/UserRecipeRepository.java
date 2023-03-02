package com.eborys.favoriterecipes.repository;

import com.eborys.favoriterecipes.repository.dao.UserRecipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRecipeRepository extends MongoRepository<UserRecipe, Long> {

    List<UserRecipe> findByUsername(String username);
}
