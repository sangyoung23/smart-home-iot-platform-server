FROM eclipse-temurin:17-jdk

WORKDIR /app

RUN mkdir -p /app/logs

COPY build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=local

CMD java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar