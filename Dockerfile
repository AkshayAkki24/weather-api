
# ----------------------------
# Stage 1: Build application
# ----------------------------
FROM eclipse-temurin:25-jdk AS build

WORKDIR /workspace

# Copy Maven configuration first for better layer caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

# Copy application source
COPY src/ src/

# Build executable JAR
RUN ./mvnw -B clean package -DskipTests


# ----------------------------
# Stage 2: Runtime image
# ----------------------------
FROM eclipse-temurin:25-jre

WORKDIR /app

# Install curl for Docker health checks
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

# Copy only the executable JAR
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8024

# Check whether the application is healthy
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
    CMD curl -fsS http://localhost:8024/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
