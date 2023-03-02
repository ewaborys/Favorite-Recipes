package com.eborys.favoriterecipes.repository;

import com.eborys.favoriterecipes.repository.dao.UserRecipe;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserRecipeRepository {

    private final MongoTemplate mongoTemplate;

    public List<UserRecipe> findBy(@NotNull String username, List<String> includedCategories,
                                    List<String> excludedCategories, Integer numberOfServings,
                                    List<String> includedIngredients, List<String> excludedIngredients,
                                    String instructionContainsText) {
        var criteria = new Criteria();
        criteria.and("username").is(username);
        addIncludeAndExclude(includedCategories, excludedCategories, criteria, "category", true);
        if (numberOfServings != null) {
            criteria.and("numberOfServings").is(numberOfServings);
        }
        addIncludeAndExclude(includedIngredients, excludedIngredients, criteria, "ingredients", false);
        var query = new Query();
        query.addCriteria(criteria);
        if (instructionContainsText != null && !instructionContainsText.isEmpty()) {
            TextCriteria textCriteria = TextCriteria.forLanguage("en").matching(instructionContainsText);
            query.addCriteria(textCriteria);
        }
        return mongoTemplate.find(query, UserRecipe.class);
    }

    private static void addIncludeAndExclude(List<String> includedThings,
                                             List<String> excludedThings,
                                             Criteria criteria, String field, boolean in) {
        var includedThingsExist = includedThings != null && !includedThings.isEmpty();
        var excludedThingsExist = excludedThings != null && !excludedThings.isEmpty();
        if (includedThingsExist && excludedThingsExist && in) {
            criteria.and(field).in(includedThings).nin(excludedThings);
        } else if (includedThingsExist && excludedThingsExist) { // !in
            criteria.and(field).all(includedThings).nin(excludedThings);
        } else if (includedThingsExist && in) {
            criteria.and(field).in(includedThings);
        } else if (includedThingsExist){ // !in
            criteria.and(field).all(includedThings);
        } else if (excludedThingsExist) {
            criteria.and(field).nin(excludedThings);
        }
    }
}
