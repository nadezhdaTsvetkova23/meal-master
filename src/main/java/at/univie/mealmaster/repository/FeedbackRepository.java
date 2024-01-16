package at.univie.mealmaster.repository;

import at.univie.mealmaster.model.Feedback;
import at.univie.mealmaster.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByRecipeId(long id);
    Collection<Object> findAllByScoreGreaterThanEqual(int scoreThreshold);
    List<Feedback> findByScoreGreaterThanEqual(int scoreThreshold);
    List<Feedback> findByRecipeAndScoreGreaterThanEqual(Recipe recipe, int ratingThreshold);
    List<Feedback> findByRecipe(Recipe recipe);
    //DB Queries
}