package at.univie.mealmaster.repository;

import at.univie.mealmaster.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByRecipeId(long id);
    //DB Queries
}