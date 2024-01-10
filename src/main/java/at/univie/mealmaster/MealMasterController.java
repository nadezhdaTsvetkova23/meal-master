package at.univie.mealmaster;

import at.univie.mealmaster.model.Recipe;
import at.univie.mealmaster.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MealMasterController {

    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping("/")
    String showIndexPage(){
        //Checks if generatedContent.txt contains true and forwards to index. If it does not exist it forwards to setup page
        boolean contentGenerated =  new CheckIfContentGenerated().checkFile();
        if(contentGenerated){
            return "index";
        } else{
            return "setup";
        }
    }

    @GetMapping("/addRecipe")
    String showAddRecipeForm(Model model){
        model.addAttribute("recipe", new Recipe());
        return "add-recipe";
    }

    @PostMapping("/addRecipe")
    String submitAddRecipeForm(@ModelAttribute Recipe recipe){
        recipeRepository.save(recipe);
        return "redirect:/success";
    }

    @GetMapping("/list")
    String showRecipeList(Model model){
        model.addAttribute("recipes", recipeRepository.findAll());
        return "list";
    }

    @GetMapping("/recipe/{id}")
    String showRecipe(@PathVariable("id") long id, Model model){
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        model.addAttribute("recipe", recipe);
        return "show-recipe";
    }

    @GetMapping("/generateData")
    String generateData(){

        //add generateData()
        return "redirect:/";
    }
}
