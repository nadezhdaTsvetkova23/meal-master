package at.univie.mealmaster.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Recipe {

    private String name;
    private String instruction;
    private int servings;
    private String link;
    private String imageLink;
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "recipe")
    private Set<RecipeIngredient> recipeIngredients;

    @ManyToMany
    @JoinTable(
            name = "RecipeHasTag",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_name")
    )
    private Set<Tag> tags;

    public Recipe(){
        tags = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag){
        tags.add(tag);
    }

    public Set<RecipeIngredient> getRecipeIngredients(){
        return recipeIngredients;
    }

    public String getTagsString(){
        StringBuilder returnString = new StringBuilder();
        for(Tag tag: tags){
            returnString.append(tag.getName()).append(", ");
        }

        //Remove the last comma
        if (returnString.length() > 0) {
            returnString.setLength(returnString.length() - 2);
        }
        return returnString.toString();
    }
}
