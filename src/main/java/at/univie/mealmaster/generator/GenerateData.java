package at.univie.mealmaster.generator;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import at.univie.mealmaster.generator.items.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenerateData {

    //TURN OFF TO WRITE TO DATABASE
    static boolean safe = true;


    public void generateData() {
        Database db = new Database();
        System.out.println("Generating data...");


        //ingredient
        int ingredientError = 0;
        Map<String, List<Ingredient>> ingredientsByForm = null;
        Ingredient[] ingredients = null;
        try {
            ingredients = new Gson().fromJson(new JsonReader(new FileReader("json/ingredients.json")), Ingredient[].class);

            //Sort into a Map based on Form
            ingredientsByForm = Arrays.stream(ingredients).collect(Collectors.groupingBy(Ingredient::getForm));

            for (var entry : ingredientsByForm.entrySet()) {
                for (Ingredient ingredient : entry.getValue()) {
                    if (!safe) {
                        if(!db.addQuery("INSERT INTO ingredient (name) VALUES ('" + ingredient.getIngredient() + "')")){
                            ingredientError++;
                        }

                    } else {
                        System.out.println("INSERT INTO ingredient (name) VALUES ('" + ingredient.getIngredient() + "')");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error while processing ingredient:");
            System.err.println(e);
        }
        System.out.println("ingredients added: " + db.checkDatasets("ingredient")+ ", "+ingredientError+" Errors appeared.");



        //ingredient alternative
        int ingredientAlternativeError = 0;
        assert ingredientsByForm != null;
        for (var entry : ingredientsByForm.entrySet()) {
            for (Ingredient ingredient : entry.getValue()) {
                int i = 0;
                while(i < 3) {
                    i++;
                    int random = (int)Math.floor(Math.random()*(entry.getValue().size()));
                    if (!safe) {
                        if(!db.addQuery("INSERT INTO Alternative ingredient (id_1, id_2) SELECT z1.id, z2.id FROM ingredient z1, ingredient z2 WHERE z1.name ='" + ingredient.getIngredient() + "' AND z2.name = '" + entry.getValue().get(random).getIngredient() + "'")){
                            ingredientAlternativeError++;
                        }

                    } else {
                        System.out.println("INSERT INTO Alternative ingredient (id_1, id_2) SELECT z1.id, z2.id FROM ingredient z1, ingredient z2 WHERE z1.name ='" + ingredient.getIngredient() + "' AND z2.name = '" + entry.getValue().get(random).getIngredient() + "'");
                    }
                }
            }
        }
        System.out.println("Alternative ingredient added: " + db.checkDatasets("Alternative ingredient")+ ", "+ingredientAlternativeError+" Errors appeared.");


        //Unit
        int unitErrors = 0;
        try {
            Unit[] units = new Gson().fromJson(new JsonReader(new FileReader("json/units.json")), Unit[].class);

            for (Unit unit : units) {
                if (!safe) {
                    if(!db.addQuery("INSERT INTO Unit (name, abreviation) VALUES ('" + unit.getName() + "', '" + unit.getAbreviation() + "')")){
                        unitErrors++;
                    }
                } else {
                    System.out.println("INSERT INTO Unit (name, abreviation) VALUES ('" + unit.getName() + "', '" + unit.getAbreviation() + "')");
                }
            }
        } catch (Exception e) {
            System.err.println("Error while processing Unit:");
            System.err.println(e);
        }
        System.out.println("Units added: " + db.checkDatasets("Unit")+ ", "+unitErrors+" Errors appeared.");



        //Tag
        int tagErrors = 0;
        Tag[] tags = null;
        try{
            tags = new Gson().fromJson(new JsonReader(new FileReader("json/tags.json")), Tag[].class);
        }catch(Exception e){
            System.err.println("Error while processing Tag:");
            System.err.println(e);
        }
        for(var tag : tags){
            if (!safe) {
                if(!db.addQuery("INSERT INTO tag (name, color) VALUES ('"+ tag.getName() +"', '"+ tag.getFarbe()+"')")){
                    tagErrors++;
                }
            }else{
                System.out.println("INSERT INTO tag (name, color) VALUES ('"+ tag.getName() +"', '"+ tag.getFarbe()+"')");
            }
        }
        System.out.println("Tag added: " + db.checkDatasets("Tag")+ ", "+tagErrors+" Errors appeared.");


        //Recipe
        int recipeErrors = 0;
        int recipeIngredientErrors = 0;
        List<String> dishes = new ArrayList<>();
        ImageLink[] imageLink = null;
        try{
            imageLink = new Gson().fromJson(new JsonReader(new FileReader("json/imageLink.json")), ImageLink[].class);

        }catch(Exception e){
            System.err.println("Error while processing ImageLink:");
            System.err.println(e);
        }
        //Amount of Recipes to generate
        int recipesAmount = 1000;

        List<String> dish = new ArrayList<>();
        dish.add("soup");
        dish.add("gratin");
        dish.add("stew");
        dish.add("casserole");
        dish.add(", baked");
        dish.add("patties");
        dish.add("filet");
        dish.add("pan");
        dish.add("puree");
        dish.add("dumplings");





        int i = 0;
        int ingredient = 0;
        int image = 0;
        while(i < recipesAmount){
            i++;
            StringBuilder name = new StringBuilder();
            StringBuilder instructions = new StringBuilder();
            int randomingredient = (int)Math.floor(Math.random()*((ingredients.length)-1));
            int randomDish = (int)Math.floor(Math.random()*(dish.size()));
            if(ingredient >= ingredients.length-1){
                ingredient = 0;
            }else{
                ingredient++;
            }

            if(image >= imageLink.length-1){
                image = 0;
            }else{
                image++;
            }

            name.append(ingredients[ingredient].getIngredient());
            name.append(dish.get(randomDish));
            name.append(" with ");
            name.append(ingredients[randomingredient].getIngredient());

            instructions.append(ingredients[ingredient].getIngredient());
            instructions.append(" wash and dry, then finely dice. Preheat the oven and set up a pot with fresh water. Salt the water.<br>");
            instructions.append(ingredients[ingredient].getIngredient()).append(" into the boiling water and cook for ").append(((randomingredient%7)+1)*6).append(" minutes.<br>");
            instructions.append("Then add ").append(ingredients[randomingredient].getIngredient()).append(" and bring to a quick boil. Then put in the oven and cook until a core temperature of 60°C.<br>");
            instructions.append("Season to taste and enjoy!");



            dishes.add(name.toString());
            if (!safe) {
                //Recipe
                if(!db.addQuery("INSERT INTO Recipe (name, imageLink, portions, description) VALUES ('" + name + "', '" + imageLink[image].getImageLink()+ "',"+ ((randomingredient%5)+2) +", '"+ instructions.toString() + "')")){
                    recipeErrors++;
                }

                //Recipe contains ingredient
                if(!db.addQuery("INSERT INTO RecipeContainsIngredient (recipe_id, ingredient_id, unit_id, menge) SELECT r.id, z.id, e.id, "+ (((randomingredient%7)+1)*100) +" FROM Recipe r, ingredient z, Unit e WHERE r.name = '" + name.toString() +"' AND z.name = '" + ingredients[ingredient].getIngredient() +"' AND e.name = 'Gramm'")){
                    recipeIngredientErrors++;
                }
                if(!db.addQuery("INSERT INTO RecipeContainsIngredient (recipe_id, ingredient_id, unit_id, menge) SELECT r.id, z.id, e.id, "+ (((randomDish%4)+1)*100) +" FROM Recipe r, ingredient z, Unit e WHERE r.name = '" + name.toString() +"' AND z.name = '" + ingredients[randomingredient].getIngredient() +"' AND e.name = 'Gramm'")){
                    recipeIngredientErrors++;
                };
            } else {
                System.out.println("INSERT INTO Recipe (name, imageLink, portions, description) VALUES ('" + name.toString() + "', '" + imageLink[image].getImageLink()+ "', "+ ((randomingredient%5)+2) +", '"+ instructions.toString() + "')");

                System.out.println("INSERT INTO RecipeContainsIngredient (recipe_id, ingredient_id, unit_id, menge) SELECT r.id, z.id, e.id, "+ (((randomingredient%7)+1)*100) +" FROM Recipe r, ingredient z, Unit e WHERE r.name = '" + name.toString() +"' AND z.name = '" + ingredients[ingredient].getIngredient() +"' AND e.name = 'Gramm'");
                System.out.println("INSERT INTO RecipeContainsIngredient (recipe_id, ingredient_id, unit_id, menge) SELECT r.id, z.id, e.id, "+ (((randomDish%4)+1)*100) +" FROM Recipe r, ingredient z, Unit e WHERE r.name = '" + name.toString() +"' AND z.name = '" + ingredients[randomingredient].getIngredient() +"' AND e.name = 'Gramm'");
            }
        }
        System.out.println("Recipe added: " + db.checkDatasets("Recipe")+ ", "+recipeErrors+" Errors appeared.");
        System.out.println("RecipeContainsIngredient added: " + db.checkDatasets("RecipeContainsIngredient")+ ", "+recipeIngredientErrors+" Errors appeared.");


        //Tags
        int tagRecipeErrors = 0;
        for(String element: dishes){
            int e = 0;
            while(e < 3) {
                if (!safe) {
                    int random = (int)Math.floor(Math.random()*((tags.length)-1));
                    if(!db.addQuery("INSERT INTO RecipeHasTag (recipe_id, tag_name) SELECT r.id, '" + tags[random].getName() + "' FROM Recipe r WHERE r.name ='" + element + "'")){
                        tagRecipeErrors++;
                    }
                    e++;
                }else{
                    int random = (int)Math.floor(Math.random()*((tags.length)-1));
                    System.out.println("INSERT INTO RecipeHasTag (recipe_id, tag_name) SELECT r.id, '" + tags[random].getName() + "' FROM Recipe r WHERE r.name ='" + element + "'");
                    e++;
                }
            }
        }
        System.out.println("RecipeHasTag added: " + db.checkDatasets("RecipeHasTag")+ ", "+tagRecipeErrors+" Errors appeared.");


    }
}
