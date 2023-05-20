# Use the official OpenJDK 11 image as the base image
FROM adoptopenjdk:11-jdk-hotspot

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file to the container
COPY target/*.jar app.jar
# COPY . .
# Expose the port that the application is listening on
EXPOSE 8082
ENV MONGO_DB_URL=mongodb://admin:admin@mongodb

# Set the command to run the JAR file
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "app.jar"]
# CMD ["./mvnw", "-Pdev"]
