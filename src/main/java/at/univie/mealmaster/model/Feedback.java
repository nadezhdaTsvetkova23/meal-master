package at.univie.mealmaster.model;

import jakarta.persistence.*;

@Entity
public class Feedback {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne // Many feedbacks can be associated with one recipe
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Lob
    private String comment;
    @Column(nullable = false)
    private Integer score;

    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void ifPresent(Object o) {
    }
}
