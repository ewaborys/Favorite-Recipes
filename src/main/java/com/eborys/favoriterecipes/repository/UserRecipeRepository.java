package com.eborys.favoriterecipes.repository;

import com.eborys.favoriterecipes.repository.dao.UserRecipe;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRecipeRepository extends MongoRepository<UserRecipe, Long> {

    Optional<UserRecipe> findByIdAndUsername(ObjectId id, String username);
}
