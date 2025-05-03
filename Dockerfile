FROM maven:3.8.4-openjdk-11-slim as builder

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src/ /app/src/

# Build the application
RUN mvn package -DskipTests

# Use a smaller JRE runtime image
FROM openjdk:11-jre-slim

WORKDIR /app

# Copy the built artifact from the builder stage
COPY --from=builder /app/target/user-api-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]