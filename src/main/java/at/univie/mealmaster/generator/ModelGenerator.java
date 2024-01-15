package at.univie.mealmaster.generator;

import at.univie.mealmaster.generator.gson.GsonImageLink;
import at.univie.mealmaster.generator.gson.GsonIngredient;
import at.univie.mealmaster.generator.gson.GsonTag;
import at.univie.mealmaster.generator.gson.GsonUnit;
import at.univie.mealmaster.model.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class ModelGenerator {

    private ArrayList<Ingredient> ingredients;
    private ArrayList<Recipe> recipes;
    private ArrayList<Unit> units;
    private ArrayList<Tag> tags;
    private ArrayList<RecipeIngredient> recipeIngredients;
    private ArrayList<String> imageLinks;
    private ArrayList<String> cookingForms;

    public ModelGenerator(){
       ingredients = new ArrayList<>();
       recipes = new ArrayList<>();
       units = new ArrayList<>();
       tags = new ArrayList<>();
       imageLinks = new ArrayList<>();
       cookingForms = new ArrayList<>();
       recipeIngredients = new ArrayList<>();

       cookingForms.add("grilled");
       cookingForms.add("cooked");
       cookingForms.add("boiled");
       cookingForms.add("steamed");
       cookingForms.add("sliced");
       cookingForms.add("rolled");

       generateData();
    }

    public void generateData(){
        Random random = new Random();

        try{
            JsonReader ingredientReader = new JsonReader(new FileReader("json/ingredients.json"));
            GsonIngredient[] gsonIngredients = new Gson().fromJson(ingredientReader, GsonIngredient[].class);

            for(GsonIngredient i:gsonIngredients){
                ingredients.add(new Ingredient(i.getName()));
                System.out.println("Added:" + i.getName());
            }

        } catch(Exception e){
            System.out.println("Error while getting Ingredients:"+ e);
        }
        try{
            JsonReader unitReader = new JsonReader(new FileReader("json/units.json"));
            GsonUnit[] gsonUnits = new Gson().fromJson(unitReader, GsonUnit[].class);

            for(GsonUnit u: gsonUnits){
                units.add(new Unit(u.getName(), u.getAbbreviation()));
                System.out.println("Added:" + u.getName());
            }
        }catch(Exception e){
            System.out.println("Error while getting Units:"+ e);
        }
        try{
            JsonReader tagReader = new JsonReader(new FileReader("json/tags.json"));
            GsonTag[] gsonTags = new Gson().fromJson(tagReader, GsonTag[].class);

            for(GsonTag t: gsonTags){
                tags.add(new Tag(t.getName(), t.getColor()));
                System.out.println("Added:" + t.getName());
            }
        }catch(Exception e){
            System.out.println("Error while getting Tags:"+ e);
        }
        try{
            JsonReader imageLinkReader = new JsonReader(new FileReader("json/imagelink.json"));
            GsonImageLink[] gsonImageLinks = new Gson().fromJson(imageLinkReader, GsonImageLink[].class);

            for(GsonImageLink i: gsonImageLinks){
                imageLinks.add(i.getImagelink());
                System.out.println("Added:" + i.getImagelink());
            }
        }catch(Exception e){
            System.out.println("Error while getting image links:"+ e);
        }

        int amountOfRecipesToGenerate = 50;

        for(int i = 0; i< amountOfRecipesToGenerate; i++){
            ArrayList<Ingredient> randomIngredients = new ArrayList<>();
            Recipe recipe = new Recipe();

            int randomNumber = 3 + random.nextInt(5); // Random Number between 3 and 7
            for(int j = 0; j < randomNumber; j++){
                randomIngredients.add(ingredients.get(random.nextInt(ingredients.size())));
            }
            for(int j = 0; j< 3; j++){
                recipe.addTag(tags.get(random.nextInt(tags.size())));
            }
            recipe.setName( cookingForms.get(random.nextInt(cookingForms.size())) +" "+randomIngredients.get(0).getName() + " with " +cookingForms.get(random.nextInt(cookingForms.size()))+" "+ randomIngredients.get(1).getName());
            recipe.setInstruction("Cook the" + randomIngredients.get(0).getName() + " in salted water. Finely slice the " + randomIngredients.get(1).getName()+" and the " + randomIngredients.get(2).getName()+" and add it to the rest. Let it simmer for about 30 minutes and enjoy while still hot!");
            recipe.setImageLink(imageLinks.get(random.nextInt(imageLinks.size())));
            recipe.setServings(1+ random.nextInt(5));

            for(Ingredient ingredient:randomIngredients){
                RecipeIngredient recipeIngredient = new RecipeIngredient();

                recipeIngredient.setIngredient(ingredient);
                recipeIngredient.setRecipe(recipe);
                recipeIngredient.setUnit(units.get(random.nextInt(units.size())));
                recipeIngredient.setAmount((double) ((1+ random.nextInt(10))*100));

                recipeIngredients.add(recipeIngredient);
            }

            recipes.add(recipe);
        }




    }
    public ArrayList<Unit> getUnits(){
        return units;
    }
    public ArrayList<Tag> getTags(){
        return tags;
    }
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Recipe> getRecipes(){
        return recipes;
    }
    public ArrayList<RecipeIngredient> getRecipeIngredients(){
        return recipeIngredients;
    }
}
