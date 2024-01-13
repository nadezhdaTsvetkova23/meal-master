package at.univie.mealmaster.repository;

import at.univie.mealmaster.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    //DB Queries
    //<S extends T> Optional<S> findOne(Ingredient example);
}