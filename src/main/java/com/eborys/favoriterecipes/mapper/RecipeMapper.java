package com.eborys.favoriterecipes.mapper;

import com.eborys.favoriterecipes.contract.model.Recipe;
import com.eborys.favoriterecipes.contract.model.RecipeInput;
import com.eborys.favoriterecipes.repository.dao.UserRecipe;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecipeMapper {
    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    UserRecipe map(Recipe recipe, String username);
    Recipe map(RecipeInput recipeInput);

    Recipe map(RecipeInput recipeInput, ObjectId id);
    Recipe map(UserRecipe recipe);
    UserRecipe mapSourceToTarget(Recipe source, @MappingTarget UserRecipe target);

    default String map(ObjectId objectId) {
        if (objectId == null) {
            return null;
        }
        return objectId.toString();
    }

    default ObjectId map(String objectIdString) {
        if (objectIdString == null) {
            return null;
        }
        return new ObjectId(objectIdString);
    }
}
