# Build stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew bootJar -x test

# Runtime stage
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/MyService-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]