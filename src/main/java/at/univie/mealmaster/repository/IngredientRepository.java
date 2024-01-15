package at.univie.mealmaster.repository;

import at.univie.mealmaster.model.Ingredient;
import at.univie.mealmaster.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByName(String name);
}