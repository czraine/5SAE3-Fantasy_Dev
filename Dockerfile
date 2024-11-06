# Use a minimal Java runtime image for better performance
FROM openjdk:17-jdk-alpine

# Define environment variables (optional but recommended for readability and maintenance)
ENV APP_HOME=/app \
    APP_JAR=kaddem-0.0.1.jar

# Set the working directory
WORKDIR $APP_HOME

# Copy the application JAR file from the target directory to the image
COPY target/$APP_JAR app.jar

# Expose the port used by the Spring Boot application
EXPOSE 8096

# Use exec form for ENTRYPOINT, and refer to the environment variables
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
