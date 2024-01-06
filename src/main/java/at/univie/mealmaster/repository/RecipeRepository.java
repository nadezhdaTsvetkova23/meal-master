package at.univie.mealmaster.repository;

import at.univie.mealmaster.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    //DB Queries
}
