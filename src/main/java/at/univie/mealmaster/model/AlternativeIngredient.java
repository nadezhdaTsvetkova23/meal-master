package at.univie.mealmaster.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class AlternativeIngredient {
    @EmbeddedId
    private AlternativeIngredientId id;

    @ManyToOne
    @MapsId("ingredient1Id")
    @JoinColumn(name = "id_1")
    private Ingredient ingredient1;

    @ManyToOne
    @MapsId("ingredient2Id")
    @JoinColumn(name = "id_2")
    private Ingredient ingredient2;

    public Ingredient getIngredient1() {
        return ingredient1;
    }

    public void setIngredient1(Ingredient ingredient1) {
        this.ingredient1 = ingredient1;
    }

    public Ingredient getIngredient2() {
        return ingredient2;
    }

    public void setIngredient2(Ingredient ingredient2) {
        this.ingredient2 = ingredient2;
    }
}
