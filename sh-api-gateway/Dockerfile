FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

VOLUME ["/root/.m2"]

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests 

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8090  

ENTRYPOINT ["java", "-jar", "app.jar"]