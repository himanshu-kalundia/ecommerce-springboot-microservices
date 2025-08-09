FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
VOLUME /tmp
COPY --from=build /app/target/cartservice-*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
