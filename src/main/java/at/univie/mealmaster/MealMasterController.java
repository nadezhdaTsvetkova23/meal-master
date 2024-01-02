package at.univie.mealmaster;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MealMasterController {
    @RequestMapping("/")
    String hello() {
        return "Hello World!";
    }
}
