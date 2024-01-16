# MealMaster
Our application is built on the Spring Boot framework, leveraging Java 17 for robust backend development. We use Thymeleaf for server-side HTML views, Spring Data JPA for relational database interactions, and connect to MongoDB for NoSQL data handling. The app is containerized with Docker, simplifying deployment and ensuring consistent environments across development and production.
To run the app:
Install Docker and Docker Compose if not already installed.
Navigate to the root directory of the project.
Execute docker-compose up in the terminal to build and start the services.
Once the services are up, access the application at http://localhost

This setup ensures a streamlined workflow from development to deployment, with Docker handling service orchestration.

Important: A file called generatedContent.txt is automatically created during the data generation process. If you want to generate the data again you must first delete this file. 
