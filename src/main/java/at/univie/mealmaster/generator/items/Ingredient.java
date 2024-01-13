package at.univie.mealmaster.generator.items;

public class Ingredient {
    private String ingredient;
    private String form;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredients) {
        this.ingredient = ingredients;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }
}
