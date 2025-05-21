FROM openjdk:23-slim AS build
RUN apt-get update && apt-get install -y maven
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:23-slim
RUN apt-get update && apt-get install -y libreoffice
RUN apt-get update && apt-get install -y poppler-utils
WORKDIR /app
COPY --from=build /build/target/quizprez-pptx-parsing-0.0.1-SNAPSHOT.jar app.jar
EXPOSE ${PPTX_BACKEND_PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]
