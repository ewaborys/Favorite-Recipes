package com.eborys.favoriterecipes.controller;

import com.eborys.favoriterecipes.contract.model.BadRequest;
import com.eborys.favoriterecipes.exceptions.RecipeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RecipeControllerAdvice {

    @ExceptionHandler(RecipeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequest handleRecipeException(RecipeException ex) {
        return new BadRequest().message(ex.getMessage());
    }
}
