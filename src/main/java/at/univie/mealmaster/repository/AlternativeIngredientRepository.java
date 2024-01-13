package at.univie.mealmaster.repository;

import at.univie.mealmaster.model.AlternativeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlternativeIngredientRepository extends JpaRepository<AlternativeIngredient, Long> {
    //DB Queries
}