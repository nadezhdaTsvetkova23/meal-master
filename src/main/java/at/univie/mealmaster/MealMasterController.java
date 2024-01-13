package at.univie.mealmaster;

import at.univie.mealmaster.model.Recipe;
import at.univie.mealmaster.model.Tag;
import at.univie.mealmaster.repository.RecipeRepository;
import at.univie.mealmaster.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MealMasterController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private TagRepository tagRepository;

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

        return "redirect:/recipe/" + recipe.getId();
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


    @GetMapping("/list")
    String showRecipeList(Model model) {
        model.addAttribute("recipes", recipeRepository.findAll());
        return "list";
    }

    @GetMapping("/recipe/{id}")
    String showRecipe(@PathVariable("id") long id, Model model) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        model.addAttribute("recipe", recipe);
        return "show-recipe";
    }

    @GetMapping("/generateData")
    String generateData() {

        //add generateData()
        return "redirect:/";
    }
}
