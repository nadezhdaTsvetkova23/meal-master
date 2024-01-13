package at.univie.mealmaster.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AlternativeIngredientId implements Serializable {

    private Long ingredient1Id;
    private Long ingredient2Id;

    // Default constructor for JPA
    public AlternativeIngredientId() {}

    // Constructor with parameters
    public AlternativeIngredientId(Long ingredient1Id, Long ingredient2Id) {
        this.ingredient1Id = ingredient1Id;
        this.ingredient2Id = ingredient2Id;
    }

    // Getters and Setters
    public Long getIngredient1Id() {
        return ingredient1Id;
    }

    public void setIngredient1Id(Long ingredient1Id) {
        this.ingredient1Id = ingredient1Id;
    }

    public Long getIngredient2Id() {
        return ingredient2Id;
    }

    public void setIngredient2Id(Long ingredient2Id) {
        this.ingredient2Id = ingredient2Id;
    }

    // Equals and HashCode based on ingredient1Id and ingredient2Id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlternativeIngredientId)) return false;
        AlternativeIngredientId that = (AlternativeIngredientId) o;
        return Objects.equals(getIngredient1Id(), that.getIngredient1Id()) &&
                Objects.equals(getIngredient2Id(), that.getIngredient2Id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIngredient1Id(), getIngredient2Id());
    }
}
