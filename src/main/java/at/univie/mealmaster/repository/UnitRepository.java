package at.univie.mealmaster.repository;

import at.univie.mealmaster.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    //DB Queries

}