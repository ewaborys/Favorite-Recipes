# Favorite Recipes Application

The Favorite Recipes Application is a server application (backend) that allows users to manage their favorite recipes.

## Features

- Search own recipes by name, category, ingredients, text in instructions and number of servings
- View detailed favorite recipe information
- Save favorite recipes to a personal collection
- Edit and delete saved recipes

## Getting Started (local development)

To run the Favorite Recipes Application on your local machine, follow these steps:

1. Install MongoDB either in docker (expose its port) or locally without using authentication (for simplicity)
2. Install Maven
3. Start the application (run in the same directory as pom.xml):
```
    mvn spring-boot:run -Dspring-boot.run.profiles=local
```
4. Use provided postman collection to interact with the server (localhost, port 8080)

## API Documentation (OpenAPI)

The Favorite Recipes Application API is documented using OpenAPI. The API documentation is available in the `contract` folder of the repository as a YAML file.

## Postman Collection

The Favorite Recipes Application API is also available as a Postman collection in the `postman` folder of the repository. You can import the collection into your Postman workspace to test the API endpoints.

## Contract-First Approach

The Favorite Recipes Application was built using the contract-first approach to API development. This approach involves defining the API specification first, using OpenAPI, and then generating the API implementation code from the specification.

Using the contract-first approach has several benefits:

- The API specification serves as a single source of truth for the API, making it easier to maintain and update the API over time.
- The API specification can be used to generate API documentation and client libraries, reducing the amount of manual work required to create these artifacts.
- The API specification provides a clear and concise view of the API, making it easier for stakeholders to understand and review the API design.

## Extra Note

Please keep in mind although this application was written considering it being "production-ready" it has a few things simplified for the sake of this exercise:

- Security is very simple (not meant for production (passwords in bytecode)! Most of times the architecture dictates what should be used for authentication)
- There is only one user, but in tests it is covered that the user cannot see recipes of other people
- Search is not case-insensitive as it depends on the requirements of the project, and it would make the solution more complicated
- It was ambiguous whether the recipes should be fetchable by this service from another db or have its own db with recipes, but I decided that in postman collection there is a request to fetch a random recipe from TheMealDb (other features are paid) which user can adapt to save the recipe (that work would be mainly done by frontend which is out of scope for this exercise)
- Once DB is installed using the method from local development setup then each user will not have any recipes, you need to add them first