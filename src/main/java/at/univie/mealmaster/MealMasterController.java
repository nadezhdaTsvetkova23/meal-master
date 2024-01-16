package at.univie.mealmaster;

import at.univie.mealmaster.generator.ModelGenerator;
import at.univie.mealmaster.model.*;
import at.univie.mealmaster.model.mongodb.MongoDBRecipe;
import at.univie.mealmaster.model.mongodb.MongoDBTag;
import at.univie.mealmaster.repository.*;
import at.univie.mealmaster.repository.mongodb.MongoDBRecipeRepository;
import at.univie.mealmaster.repository.mongodb.MongoDBTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
    @Autowired
    private MongoDBRecipeRepository mongoDBRecipeRepository;
    @Autowired
    private MongoDBTagRepository mongoDBTagRepository;

    private boolean useMongoDB = false;

    @GetMapping("/")
    String showIndexPage(Model model) {
        if(!useMongoDB){
            //Checks if generatedContent.txt contains true and forwards to index. If it does not exist it forwards to setup page
            boolean contentGenerated = new CheckIfContentGenerated().checkFile();
            if (contentGenerated) {

                //Get 4 random recipes
                ArrayList<Recipe> recipes = new ArrayList<>(recipeRepository.findAll());
                ArrayList<Recipe> randomRecipes = new ArrayList<>();
                for(int i = 0; i< 4; i++){
                    randomRecipes.add(recipes.get(new Random().nextInt(recipes.size())));
                }
                model.addAttribute("recipes", randomRecipes);
                return "index";
            } else {
                return "setup";
            }
        }else{
            ArrayList<MongoDBRecipe> recipes = new ArrayList<>(mongoDBRecipeRepository.findAll());
            ArrayList<MongoDBRecipe> randomRecipes = new ArrayList<>();
            for(int i = 0; i< 4; i++){
                randomRecipes.add(recipes.get(new Random().nextInt(recipes.size())));
            }
            model.addAttribute("mongoRecipes", randomRecipes);
            return "index-mongo";
        }
    }

    @GetMapping("/search")
    public String searchRecipes(@RequestParam(required = false) String name, Model model) {
        List<Recipe> searchResults;
        if (name == null || name.trim().isEmpty()) {
            // If the search query is empty, initialize an empty list
            searchResults = Collections.emptyList();
            model.addAttribute("message", "Please enter a search term.");
        } else {
            // Get recipes based on the search query
            searchResults = recipeRepository.findByNameContainingIgnoreCase(name.trim());
        }

        model.addAttribute("searchTerm", name);
        model.addAttribute("recipes", searchResults);
        return "search-results";
    }

    @GetMapping("/addRecipe")
    String showAddRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "add-recipe";
    }

    @PostMapping("/addRecipe")
    String submitAddRecipeForm(@ModelAttribute Recipe recipe, @RequestParam("string-tags") String tags) {
        if(!useMongoDB){
            if(!tags.isBlank()){
                Set<Tag> tagSet = new HashSet<>();

                // Split the tags string and process each tag
                for (String tagName : tags.split(",")) {
                    tagName = tagName.trim();
                    tagName = tagName.substring(0, 1).toUpperCase() + tagName.substring(1).toLowerCase();

                    // Find the tag in the repository or create a new one
                    String finalTagName = tagName;
                    Tag tag = tagRepository.findByName(tagName)
                            .orElseGet(() -> new Tag(finalTagName));

                    tagSet.add(tag);
                    tagRepository.save(tag);
                }
                recipe.setTags(tagSet);
            }



            // Save the recipe
            recipeRepository.save(recipe);

            return "redirect:/editIngredients/" + recipe.getId();
        }else{

            Set<MongoDBTag> tagSet = new HashSet<>();

            // Split the tags string and process each tag
            for (String tagName : tags.split(",")) {
                tagName = tagName.trim();
                tagName = tagName.substring(0, 1).toUpperCase() + tagName.substring(1).toLowerCase();

                // Find the tag in the repository or create a new one
                String finalTagName = tagName;
                MongoDBTag tag = mongoDBTagRepository.findByName(tagName)
                        .orElseGet(() -> new MongoDBTag(finalTagName));

                tagSet.add(tag);
                mongoDBTagRepository.save(tag);
            }



            MongoDBRecipe mongoDBRecipe = new MongoDBRecipe();
            for(MongoDBTag tag: tagSet){
                mongoDBRecipe.addTag(tag.getName());
            }
            mongoDBRecipe.setName(recipe.getName());
            mongoDBRecipe.setLink(recipe.getLink());
            mongoDBRecipe.setServings(recipe.getServings());
            mongoDBRecipe.setImagelink(recipe.getImageLink());
            mongoDBRecipe.setInstructions(recipe.getInstruction());

            // Save the recipe
            mongoDBRecipeRepository.save(mongoDBRecipe);

            return "redirect:/recipe/" + mongoDBRecipe.getId();
        }
    }

    @GetMapping("editIngredients/{id}")
    String showEditIngredientsForm(@PathVariable("id") long id, Model model) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));

        ArrayList<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByRecipe(recipe);


        model.addAttribute("ingredients", recipeIngredients);
        model.addAttribute("recipe", recipe);
        model.addAttribute("units", unitRepository.findAll());

        return "edit-ingredients";
    }

    @PostMapping("addIngredient")
    String submitAddIngredient(@RequestParam("recipe") String recipeString, @RequestParam("ingredient") String ingredientString, @RequestParam("unit") String unitString, @RequestParam("amount") Double amountString) {
        if(!useMongoDB){
            Ingredient ingredient = new Ingredient();
            try {
                ingredient = ingredientRepository.findByName(ingredientString).orElseThrow(() -> new IllegalArgumentException("Invalid ingredient:" + ingredientString));
            } catch (IllegalArgumentException e) {
                ingredient.setName(ingredientString);
                ingredientRepository.save(ingredient);
            }

            RecipeIngredient recipeIngredient = new RecipeIngredient();

            recipeIngredient.setIngredient(ingredient);

            Recipe recipe = recipeRepository.findById(Long.parseLong(recipeString)).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + recipeString));

            recipeIngredient.setRecipe(recipe);
            recipeIngredient.setAmount(amountString);

            Unit unit = unitRepository.findByName(unitString).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + unitString));
            ;
            recipeIngredient.setUnit(unit);

            recipeIngredientRepository.save(recipeIngredient);

            return "redirect:/";
        }else{

            MongoDBRecipe mongoDBRecipe = mongoDBRecipeRepository.findById(recipeString).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + recipeString));;

            //Abbrevation not yet supported
            mongoDBRecipe.addIngredient(ingredientString, amountString.intValue(), unitString, unitString);
            return "redirect:/";
        }
    }

    @GetMapping("/addFeedback/{recipeId}")
    String showAddFeedbackForm(@PathVariable("recipeId") String recipeId, Model model) {
        Recipe recipe = recipeRepository.findById(Long.valueOf(recipeId))
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
        try {
            recipeIngredient = recipeIngredientRepository.findById(recipeIngredientID).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + recipeIngredientID));

            recipeIngredientRepository.delete(recipeIngredient);
            return "redirect:/editIngredients/" + recipeIngredient.getRecipe().getId();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return "redirect:/";
        }
    }


    @GetMapping("/list")
    String showRecipeList(Model model, @RequestParam(required = false, defaultValue = "") List<String> tags) {
        if(!useMongoDB){
            if (!tags.isEmpty()) {
                Set<Recipe> recipes = new HashSet<>();
                for (String tag : tags) {
                    recipes.addAll(recipeRepository.findByTags(new Tag(tag)));
                }
                model.addAttribute("recipes", recipes);
            } else {
                model.addAttribute("recipes", recipeRepository.findAll());
            }
            model.addAttribute("tags", tagRepository.findAll());
            return "list";
        }else{
            model.addAttribute("mongoRecipes", mongoDBRecipeRepository.findAll());
            return "list-mongo";
        }
    }

    @GetMapping("/recipe/{id}")
    String showRecipe(@PathVariable("id") long id, Model model) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        List<Feedback> feedbacks = feedbackRepository.findByRecipeId(id);
        model.addAttribute("recipe", recipe);
        model.addAttribute("feedbacks", feedbacks);
        return "show-recipe";
    }


    @GetMapping("/deleteFeedback/{feedbackId}")
    String deleteFeedback(@PathVariable("feedbackId") long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Feedback Id:" + feedbackId));
        long recipeId = feedback.getRecipe().getId();
        feedbackRepository.delete(feedback);
        return "redirect:/recipe/" + recipeId;
    }

    @GetMapping("/editFeedback/{feedbackId}")
    String showEditFeedbackForm(@PathVariable("feedbackId") long feedbackId, Model model) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Feedback Id:" + feedbackId));

        Recipe recipe = feedback.getRecipe();
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe not found for feedback Id:" + feedbackId);
        }

        model.addAttribute("feedback", feedback);
        model.addAttribute("recipe", recipe);
        return "edit-feedback";
    }

    @PostMapping("/editFeedback/{feedbackId}")
    public String editFeedback(@PathVariable("feedbackId") long feedbackId, @ModelAttribute Feedback updatedFeedback) {
        Feedback existingFeedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Feedback Id:" + feedbackId));

        existingFeedback.setUserName(updatedFeedback.getUserName());
        existingFeedback.setScore(updatedFeedback.getScore());
        existingFeedback.setComment(updatedFeedback.getComment());
        feedbackRepository.save(existingFeedback);

        return "redirect:/recipe/" + existingFeedback.getRecipe().getId();
    }


    @GetMapping("/report/tag")
    String getTopIngredientForMostCommonTag(Model model) {


        ArrayList<Tag> tags = new ArrayList<>(tagRepository.findAll());

        HashMap<Tag, Integer> tagOccurance = new HashMap<>();


        for (Tag tag : tags) {
            tagOccurance.put(tag, tag.getRecipes().size());
        }

        AtomicReference<Tag> topTag = new AtomicReference<>(new Tag());
        AtomicInteger topTagOccurrence = new AtomicInteger();
        tagOccurance.forEach((key, value) -> {
            if (topTagOccurrence.get() < value) {
                topTag.set(key);
                topTagOccurrence.set(value);
            }
        });

        model.addAttribute("tag", topTag.get());
        model.addAttribute("tagOccurrence", topTagOccurrence);

        HashMap<Ingredient, Integer> ingredientOccurrence = new HashMap<>();
        ArrayList<Recipe> recipesWithTopTag = recipeRepository.findByTags(topTag.get());

        model.addAttribute("recipes", recipesWithTopTag);

        for (Recipe recipe : recipesWithTopTag) {
            for (RecipeIngredient ri : recipeIngredientRepository.findByRecipe(recipe)) {
                ingredientOccurrence.merge(ri.getIngredient(), 1, Integer::sum);
            }
        }

        //Add HashMap to list in order to sort it
        List<Map.Entry<Ingredient, Integer>> list = new ArrayList<>(ingredientOccurrence.entrySet());

        //Sort
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        //Just get the top 5
        int i = 0;
        List<Ingredient> sortedAndFilteredIngredients = new ArrayList<>();
        for (Map.Entry<Ingredient, Integer> entry : list) {
            if (i < 5) {
                sortedAndFilteredIngredients.add(entry.getKey());
            }
            i++;
        }

        model.addAttribute("ingredients", sortedAndFilteredIngredients);

        return "report-01";
    }

    @GetMapping("/report/ingredient")
    public String getMostUsedIngredientsFromTopRatedRecipes(Model model) {
        // Fetch all recipes and their average scores
        List<Recipe> allRecipes = recipeRepository.findAll();
        Map<Recipe, Double> recipeAverageScores = allRecipes.stream()
                .collect(Collectors.toMap(recipe -> recipe, this::calculateAverageScore));

        // Find the highest average score
        double maxAverageScore = recipeAverageScores.values().stream()
                .max(Double::compare)
                .orElse(0.0);

        // Filter recipes that have the highest average score
        List<Recipe> topRatedRecipes = recipeAverageScores.entrySet().stream()
                .filter(entry -> entry.getValue() == maxAverageScore)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Count the frequency of ingredients in top-rated recipes
        Map<Ingredient, Integer> ingredientFrequency = new HashMap<>();
        for (Recipe recipe : topRatedRecipes) {
            for (RecipeIngredient recipeIngredient : recipeIngredientRepository.findByRecipe(recipe)) {
                ingredientFrequency.merge(recipeIngredient.getIngredient(), 1, Integer::sum);
            }
        }

        // Find the most frequent ingredient with the highest score
        Map.Entry<Ingredient, Integer> mostFrequentIngredientEntry = ingredientFrequency.entrySet().stream()
                .max((e1, e2) -> {
                    int compare = Integer.compare(e1.getValue(), e2.getValue());
                    if (compare == 0) {
                        // If frequency is the same, compare the ingredient names
                        return e1.getKey().getName().compareToIgnoreCase(e2.getKey().getName());
                    }
                    return compare;
                })
                .orElse(null);

        Ingredient mostFrequentIngredient = mostFrequentIngredientEntry != null ? mostFrequentIngredientEntry.getKey() : null;

        // Add attributes to the model
        model.addAttribute("mostFrequentIngredient", mostFrequentIngredient);
        model.addAttribute("topRecipes", topRatedRecipes);

        return "report-02";
    }

    private double calculateAverageScore(Recipe recipe) {
        List<Feedback> feedbacks = feedbackRepository.findByRecipe(recipe);
        if (feedbacks.isEmpty()) return 0;
        return feedbacks.stream().mapToInt(Feedback::getScore).average().orElse(0);
    }

        @GetMapping("/generateData")
    String generateData() {
        ModelGenerator mg = new ModelGenerator();

        unitRepository.saveAll(mg.getUnits());
        tagRepository.saveAll(mg.getTags());
        ingredientRepository.saveAll(mg.getIngredients());
        recipeRepository.saveAll(mg.getRecipes());
        recipeIngredientRepository.saveAll(mg.getRecipeIngredients());
        feedbackRepository.saveAll(mg.getFeedback());

        new CheckIfContentGenerated().writeTrueToFile();
        return "redirect:/";
    }

    @GetMapping("/useMongo")
    String useMongoDB(){
        useMongoDB = true;

        //Migrate all Recipes
        ArrayList<Recipe> recipes = new ArrayList<>(recipeRepository.findAll());

        for(Recipe recipe: recipes){
            MongoDBRecipe mongoDBRecipe = new MongoDBRecipe();

            mongoDBRecipe.setName(recipe.getName());
            mongoDBRecipe.setImagelink(recipe.getImageLink());
            mongoDBRecipe.setLink(recipe.getLink());
            mongoDBRecipe.setInstructions(recipe.getInstruction());
            mongoDBRecipe.setServings(recipe.getServings());

            for(RecipeIngredient ri : recipe.getRecipeIngredients()){
                mongoDBRecipe.addIngredient(ri.getIngredient().getName(), ri.getAmount(), ri.getUnit().getName(), ri.getUnit().getAbbreviation());
            }

            mongoDBRecipeRepository.save(mongoDBRecipe);
        }

        return "redirect:/";
    }
}
