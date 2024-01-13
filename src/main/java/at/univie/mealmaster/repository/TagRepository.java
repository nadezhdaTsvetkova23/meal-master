package at.univie.mealmaster.repository;

import at.univie.mealmaster.model.Recipe;
import at.univie.mealmaster.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    //DB Queries
}