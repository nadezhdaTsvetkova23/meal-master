package at.univie.mealmaster.repository;

import at.univie.mealmaster.model.Tag;
import at.univie.mealmaster.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    Optional<Unit> findByName(String name);

}