package at.univie.mealmaster;

import at.univie.mealmaster.model.*;
import at.univie.mealmaster.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class MealMasterController {

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;

    @GetMapping("/")
    String showIndexPage() {
        //Checks if generatedContent.txt contains true and forwards to index. If it does not exist it forwards to setup page
        boolean contentGenerated = new CheckIfContentGenerated().checkFile();
        if (contentGenerated) {
            return "index";
        } else {
            return "setup";
        }
    }

    @GetMapping("/addRecipe")
    String showAddRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "add-recipe";
    }

    @PostMapping("/addRecipe")
    String submitAddRecipeForm(@ModelAttribute Recipe recipe, @RequestParam("string-tags") String tags) {
        Set<Tag> tagSet = new HashSet<>();

        // Split the tags string and process each tag
        for (String tagName : tags.split(",")) {
            tagName = tagName.trim();
            tagName = tagName.substring(0, 1).toUpperCase() + tagName.substring(1).toLowerCase();;

            // Find the tag in the repository or create a new one
            String finalTagName = tagName;
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> new Tag(finalTagName));

            tagSet.add(tag);
            tagRepository.save(tag);
        }

        recipe.setTags(tagSet);

        // Save the recipe
        recipeRepository.save(recipe);

        return "redirect:/editIngredients/" + recipe.getId();
    }

    @GetMapping("editIngredients/{id}")
    String showEditIngredientsForm(@PathVariable("id") long id, Model model){
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));

        ArrayList<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByRecipe(recipe);


        model.addAttribute("ingredients", recipeIngredients);
        model.addAttribute("recipe", recipe);
        model.addAttribute("units", unitRepository.findAll());

        return "edit-ingredients";
    }

    @PostMapping("addIngredient")
    String submitAddIngredient(@RequestParam("recipe") Long recipeString, @RequestParam("ingredient") String ingredientString, @RequestParam("unit") String unitString, @RequestParam("amount") Double amountString){

        Ingredient ingredient = new Ingredient();
        try{
           ingredient =  ingredientRepository.findByName(ingredientString).orElseThrow(() -> new IllegalArgumentException("Invalid ingredient:" + ingredientString));
        } catch(IllegalArgumentException e){
            ingredient.setName(ingredientString);
            ingredientRepository.save(ingredient);
        }

        RecipeIngredient recipeIngredient = new RecipeIngredient();

        recipeIngredient.setIngredient(ingredient);

        Recipe recipe = recipeRepository.findById(recipeString).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + recipeString));

        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setAmount(amountString);

        Unit unit = unitRepository.findByName(unitString).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + unitString));;
        recipeIngredient.setUnit(unit);

        recipeIngredientRepository.save(recipeIngredient);

        return "redirect:/";
    }

    @GetMapping("/addFeedback/{recipeId}")
    String showAddFeedbackForm(@PathVariable("recipeId") long recipeId, Model model) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Recipe Id:" + recipeId));
        model.addAttribute("recipe", recipe);
        return "add-feedback";
    }

    @PostMapping("/addFeedback/{recipeId}")
    String addFeedback(@PathVariable("recipeId") long recipeId, @ModelAttribute Feedback feedback) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Recipe Id:" + recipeId));
        feedback.setRecipe(recipe);
        feedbackRepository.save(feedback);
        return "redirect:/recipe/" + recipeId;
    }



    @GetMapping("/editRecipe/{id}")
    String showEditRecipeForm(@PathVariable("id") long id, Model model) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        model.addAttribute("recipe", recipe);
        return "edit-recipe";
    }

    @PostMapping("/editRecipe")
    String submitEditRecipeForm(@ModelAttribute Recipe recipe, @RequestParam("string-tags") String tags) {
        Set<Tag> tagSet = new HashSet<>();

        // Split the tags string and process each tag
        for (String tagName : tags.split(",")) {
            tagName = tagName.trim();

            // Find the tag in the repository or create a new one
            String finalTagName = tagName;
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> new Tag(finalTagName));

            tagSet.add(tag);
            tagRepository.save(tag);
        }

        recipe.setTags(new HashSet<>());
        recipe.setTags(tagSet);

        recipeRepository.save(recipe);
        return "redirect:/recipe/" + recipe.getId();
    }

    @GetMapping("/deleteRecipe/{id}")
    String deleteRecipe(@PathVariable("id") long id, Model model) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        recipeRepository.delete(recipe);
        return "redirect:/";
    }

    @GetMapping("/deleteIngredient/{recipe-ingredient-id}")
    String deleteIngredientFromRecipe(@PathVariable("recipe-ingredient-id") long recipeIngredientID) {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        try{
            recipeIngredient = recipeIngredientRepository.findById(recipeIngredientID).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + recipeIngredientID));

            recipeIngredientRepository.delete(recipeIngredient);
            return "redirect:/editIngredients/" + recipeIngredient.getRecipe().getId();
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
            return "redirect:/";
        }
    }


    @GetMapping("/list")
    String showRecipeList(Model model, @RequestParam(required = false, defaultValue = "") List<String> tags) {
        System.out.println(tags);
        if(!tags.isEmpty()){
            Set<Recipe> recipes = new HashSet<>();
            for(String tag: tags){
                recipes.addAll(recipeRepository.findByTags(new Tag(tag)));
            }
            model.addAttribute("recipes", recipes);
        }else{
            model.addAttribute("recipes", recipeRepository.findAll());
        }
        model.addAttribute("tags", tagRepository.findAll());
        return "list";
    }

    @GetMapping("/recipe/{id}")
    String showRecipe(@PathVariable("id") long id, Model model) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        List<Feedback> feedbacks = feedbackRepository.findByRecipeId(id);
        model.addAttribute("recipe", recipe);
        model.addAttribute("feedbacks", feedbacks);
        return "show-recipe";
    }

    @GetMapping("/report/tag")
    String getTopIngredientForMostCommonTag(Model model){


        ArrayList<Tag> tags = new ArrayList<>(tagRepository.findAll());

        HashMap<Tag, Integer> tagOccurance = new HashMap<>();


        for(Tag tag: tags){
            tagOccurance.put(tag, tag.getRecipes().size());
        }

        AtomicReference<Tag> topTag = new AtomicReference<>(new Tag());
        AtomicInteger topTagOccurrence = new AtomicInteger();
        tagOccurance.forEach((key, value) -> {
            if(topTagOccurrence.get() < value){
                topTag.set(key);
                topTagOccurrence.set(value);
            }
        });

        model.addAttribute("tag", topTag.get());
        model.addAttribute("tagOccurrence", topTagOccurrence);

        HashMap<Ingredient, Integer> ingredientOccurrence = new HashMap<>();
        ArrayList<Recipe> recipesWithTopTag = recipeRepository.findByTags(topTag.get());

        model.addAttribute("recipes", recipesWithTopTag);

        for(Recipe recipe: recipesWithTopTag){
            for(RecipeIngredient ri : recipeIngredientRepository.findByRecipe(recipe)){
                if(ingredientOccurrence.get(ri.getIngredient()) != null){
                    ingredientOccurrence.put(ri.getIngredient(), ingredientOccurrence.get(ri.getIngredient())+1);
                }else{
                    ingredientOccurrence.put(ri.getIngredient(), 1);
                }
            }
        }


        //Add HashMap to list in order to sort it
        List<Map.Entry<Ingredient, Integer>> list = new ArrayList<>(ingredientOccurrence.entrySet());

        //Sort
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        //Just get the top 5
        int i = 0;
        List<Ingredient> sortedAndFilteredIngredients = new ArrayList<>();
        for(Map.Entry<Ingredient, Integer> entry: list){
            if(i < 5){
                sortedAndFilteredIngredients.add(entry.getKey());
            }
            i++;
        }

        model.addAttribute("ingredients", sortedAndFilteredIngredients);

        return "report-01";
    }

    @GetMapping("/generateData")
    String generateData() {

        //add generateData()
        return "redirect:/";
    }
}
