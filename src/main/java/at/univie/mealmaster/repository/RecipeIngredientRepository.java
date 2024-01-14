package at.univie.mealmaster.repository;

import at.univie.mealmaster.model.Recipe;
import at.univie.mealmaster.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    ArrayList<RecipeIngredient> findByRecipe(Recipe recipe);
}