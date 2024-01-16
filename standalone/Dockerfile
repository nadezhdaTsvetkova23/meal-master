# Amazon Coretto OpenJDK 17
FROM amazoncorretto:17 as build

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=/MealMaster-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

# Copy JSON files into the container
COPY json ./json

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]