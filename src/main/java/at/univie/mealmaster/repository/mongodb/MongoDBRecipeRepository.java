package at.univie.mealmaster.repository.mongodb;

import at.univie.mealmaster.model.mongodb.MongoDBRecipe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDBRecipeRepository extends MongoRepository<MongoDBRecipe, String> {
    // Custom query methods if needed
}