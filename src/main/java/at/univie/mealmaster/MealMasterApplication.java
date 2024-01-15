package at.univie.mealmaster;

import at.univie.mealmaster.generator.GenerateData;
import at.univie.mealmaster.generator.ModelGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MealMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MealMasterApplication.class, args);
    }
}
