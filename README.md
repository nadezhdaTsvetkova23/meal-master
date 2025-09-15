# Meal Master 🍲

Meal Master is a full-stack recipe management and meal planning application, developed as a university project.  
It allows users to create, search, filter, and rate recipes while managing ingredients with both **relational (SQL)** and **NoSQL (MongoDB)** databases.  
The app also includes random meal plan generation and analytical reports for better culinary insights.

---

## 🚀 Tech Stack
- **Backend:** Java 17 (Spring Boot, Spring Data JPA, Thymeleaf for views)  
- **Databases:** MySQL (RDBMS) & MongoDB (NoSQL migration)  
- **Build Tools:** Maven / Gradle  
- **Deployment:** Docker & Docker Compose  
- **Testing:** JUnit  
- **Architecture:** REST APIs, layered architecture (Controller → Service → Repository)

---

## ✨ Features
- 🔍 Search recipes by **name**  
- 🏷️ Filter recipes by **color-coded tags**  
- ➕ Add new recipes with **ingredients, instructions & tags**  
- ⭐ Add **ratings & feedback** (1–5 stars, comments, author name)  
- 🎲 Generate random meal plans for multiple days  
- 📊 Reports:
  - Most common tag across recipes  
  - Most used ingredient in top-rated recipes  

---

## ⚙️ Getting Started

### Prerequisites
- [Docker](https://www.docker.com/) & [Docker Compose](https://docs.docker.com/compose/) installed

### Run the application
#### Clone the repo
git clone https://github.com/<your-username>/meal-master.git
cd meal-master

#### Build and start services
docker-compose up

Once the services are up, access the app at:  
👉 **http://localhost:8080**

> ⚠️ **Note:** During data generation, a file named `generatedContent.txt` is created.  
> If you want to generate data again, delete this file before restarting.

---

## 📡 API Endpoints (examples)

```http
GET  /api/recipes?name=pasta         # Search recipes by name
GET  /api/recipes?tag=vegan          # Filter recipes by tag
POST /api/recipes                    # Add a new recipe
POST /api/recipes/{id}/feedback      # Add feedback to a recipe
```

## 🗄️ Database Design

### Relational (MySQL)
- Tables: `Recipe`, `Ingredient`, `Tag`, `Feedback` + associative tables for many-to-many relations  
- Normalized design for data integrity and query efficiency  

### NoSQL (MongoDB)
- Collections: `Recipe`, `Tag`  
- Embedded documents for ingredients and feedback  
- Indexed fields (e.g., `name`, `tags`, `feedback.score`) for faster queries  

**Example Recipe Document (MongoDB):**
```json
{
  "name": "Spaghetti Carbonara",
  "imagelink": "https://example.com/carbonara.jpg",
  "link": "https://recipe.com/carbonara",
  "instructions": "Boil pasta, mix with sauce...",
  "servings": 4,
  "tags": ["Italian", "Pasta"],
  "ingredients": [
    { "name": "Spaghetti", "amount": 200, "unit": "g" },
    { "name": "Eggs", "amount": 2, "unit": "pcs" }
  ],
  "feedback": [
    { "user": "Anna", "score": 5, "description": "Delicious and easy!" }
  ]
}
```

## Model
<img width="1001" height="775" alt="image" src="https://github.com/user-attachments/assets/bc5855ee-d242-48bb-8526-f1ad6396e07c" />

### NoSQL
<img width="966" height="714" alt="image" src="https://github.com/user-attachments/assets/308274e7-ae0c-4e55-ac3e-269f16fb276a" />

---

## 👩‍💻 Authors
- Nadezhda Tsvetkova
- Lucas Enzi
