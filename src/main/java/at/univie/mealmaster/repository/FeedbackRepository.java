package at.univie.mealmaster.repository;

import at.univie.mealmaster.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    //DB Queries
}