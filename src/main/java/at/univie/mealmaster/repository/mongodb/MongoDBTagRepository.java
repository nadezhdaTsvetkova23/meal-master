package at.univie.mealmaster.repository.mongodb;

import at.univie.mealmaster.model.mongodb.MongoDBTag;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDBTagRepository extends MongoRepository<MongoDBTag, String> {
    // Custom query methods if needed
}