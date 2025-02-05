FROM openjdk:23-slim AS build
RUN apt-get update && apt-get install -y maven
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:23-slim
WORKDIR /app
COPY --from=build /build/target/quizprez-quiz-0.0.1-SNAPSHOT.jar app.jar
EXPOSE ${QUIZ_BACKEND_PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]
