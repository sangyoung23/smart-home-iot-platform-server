# ---- 1. Build Stage ----
FROM gradle:8.7-jdk17 AS builder
WORKDIR /app

# Gradle 캐싱
COPY build.gradle settings.gradle ./
COPY src ./src

RUN gradle clean build -x test

# ---- 2. Run Stage ----
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드된 jar 복사
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
