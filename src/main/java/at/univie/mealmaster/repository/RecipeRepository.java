package at.univie.mealmaster.repository;

import at.univie.mealmaster.model.Recipe;
import at.univie.mealmaster.model.RecipeIngredient;
import at.univie.mealmaster.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    ArrayList<Recipe> findByTags(Tag tags);
    //DB Queries
}
