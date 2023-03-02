package com.eborys.favoriterecipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class FavoriteRecipesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FavoriteRecipesApplication.class, args);
	}

}
