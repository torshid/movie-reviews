FROM openjdk:8-jdk-alpine
COPY target/movie-reviews-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
