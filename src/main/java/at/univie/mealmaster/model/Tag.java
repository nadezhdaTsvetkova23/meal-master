package at.univie.mealmaster.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Entity
public class Tag {

    @Id
    private String name;
    private String color;

    @ManyToMany(mappedBy = "tags")
    private Set<Recipe> recipes;

    //Required by Hibernate
    public Tag(){}

    public Tag(String tag){
        // List of colors
        Set<String> colors = new HashSet<>(Arrays.asList(
                "0fa538", "6fcaf0", "63b7e7", "95c991", "ea5557", "eb7181"
        ));
        String[] colorArray = colors.toArray(new String[0]);

        setName(tag);
        //Add a random color to new Tags
        setColor(colorArray[new Random().nextInt(colorArray.length)]);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }
}
