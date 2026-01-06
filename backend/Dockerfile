FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Gradle wrapper and dependency resolution
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew --version

# Application sources
COPY src ./src

# Build Spring Boot fat jar
RUN ./gradlew bootJar --no-daemon

# Runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy built jar
COPY --from=builder /app/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=default

ENTRYPOINT ["java", "-jar", "/app/app.jar"]