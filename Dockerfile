# The application is packaged by the pipeline first (./mvnw package),
# this file only puts the finished package into an image.
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
