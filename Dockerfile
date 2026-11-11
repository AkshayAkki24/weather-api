
# Stage 1: Build the Spring Boot application
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

# Copy project source code
COPY . .

# Build the executable JAR
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copy the generated JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8024

# Start Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
