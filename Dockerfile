# Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
# We use -DskipTests to speed up the build, remove it if you want tests to run during build
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the built jar file from the builder stage
# The jar name matches the artifactId and version from pom.xml
COPY --from=builder /app/target/nextgen2026-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
