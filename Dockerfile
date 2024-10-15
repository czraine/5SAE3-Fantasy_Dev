# Use a Java base image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR from Jenkins to the Docker image
COPY target/*.jar app.jar

# Expose the application’s port
EXPOSE 8096

# Command to run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]