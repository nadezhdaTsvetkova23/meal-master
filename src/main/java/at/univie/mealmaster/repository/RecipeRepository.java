package at.univie.mealmaster.repository;

import at.univie.mealmaster.model.Recipe;
import at.univie.mealmaster.model.RecipeIngredient;
import at.univie.mealmaster.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    ArrayList<Recipe> findByTags(Tag tags);
    List<Recipe> findByNameStartingWith(String name);
    List<Recipe> findByNameContainingIgnoreCase(String trim);
    //DB Queries
}
