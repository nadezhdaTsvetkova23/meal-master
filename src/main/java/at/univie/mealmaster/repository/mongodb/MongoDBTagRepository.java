package at.univie.mealmaster.repository.mongodb;

import at.univie.mealmaster.model.Tag;
import at.univie.mealmaster.model.mongodb.MongoDBTag;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MongoDBTagRepository extends MongoRepository<MongoDBTag, String> {
    Optional<MongoDBTag> findByName(String name);
}