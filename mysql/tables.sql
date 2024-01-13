CREATE TABLE Tag(
                    name VARCHAR(100) PRIMARY KEY,
                    color VARCHAR(6)
);

CREATE TABLE Recipe(
                       id INT AUTO_INCREMENT,
                       instruction TEXT,
                       servings INT,
                       name VARCHAR(200) NOT NULL UNIQUE,
                       link TEXT,
                       image_link TEXT,
                       PRIMARY KEY(id)
);

CREATE TABLE RecipeHasTag(
                             recipe_id INT,
                             tag_name VARCHAR(100),
                             FOREIGN KEY (recipe_id) REFERENCES Recipe(id) ON DELETE CASCADE,
                             FOREIGN KEY (tag_name) REFERENCES Tag(name) ON DELETE CASCADE,
                             PRIMARY KEY (recipe_id, tag_name)
);

CREATE TABLE Feedback(
                       id INT AUTO_INCREMENT,
                       recipe_id INT,
                       comment TEXT,
                       score INT NOT NULL,
                       FOREIGN KEY (recipe_id) REFERENCES Recipe(id) ON DELETE CASCADE,
                       PRIMARY KEY (id, recipe_id)
);

CREATE TABLE Unit(
                     id INT AUTO_INCREMENT,
                     name VARCHAR(50) NOT NULL,
                     abbreviation VARCHAR(10) NOT NULL,
                     PRIMARY KEY(id)
);

CREATE TABLE Ingredient(
                           id INT AUTO_INCREMENT,
                           name VARCHAR(50) NOT NULL UNIQUE,
                           PRIMARY KEY(id)
);

CREATE TABLE AlternativeIngredient(
                                      id_1 INT NOT NULL,
                                      id_2 INT NOT NULL,
                                      FOREIGN KEY (id_1) REFERENCES Ingredient(id) ON DELETE CASCADE,
                                      FOREIGN KEY (id_2) REFERENCES Ingredient(id) ON DELETE CASCADE,
                                      PRIMARY KEY (id_1, id_2)
);

CREATE TABLE RecipeContainsIngredient(
                                         recipe_id INT NOT NULL,
                                         ingredient_id INT NOT NULL,
                                         unit_id INT,
                                         quantity INT,
                                         FOREIGN KEY (recipe_id) REFERENCES Recipe(id) ON DELETE CASCADE,
                                         FOREIGN KEY (ingredient_id) REFERENCES Ingredient(id) ON DELETE CASCADE,
                                         FOREIGN KEY (unit_id) REFERENCES Unit(id),
                                         PRIMARY KEY (recipe_id, ingredient_id)
);

