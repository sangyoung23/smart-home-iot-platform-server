FROM eclipse-temurin:17-jdk
WORKDIR /app
RUN mkdir -p /app/logs
COPY . .
RUN ./gradlew clean build -x test
CMD ["java", "-jar", "build/libs/smart-home-iot-0.0.1-SNAPSHOT.jar"]
