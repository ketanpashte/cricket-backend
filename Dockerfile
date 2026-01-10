# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy Maven Wrapper and Config
COPY .mvn/ .mvn/
COPY mvnw ./
RUN chmod +x mvnw

# Copy Project Definitions
COPY pom.xml ./
COPY my-scoring-module ./my-scoring-module
COPY src ./src

# Build the Scoring Module first (Dependency)
RUN ./mvnw -f my-scoring-module/pom.xml clean install -DskipTests

# Build the Main Application
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/live_score-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
