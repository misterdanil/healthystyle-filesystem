FROM maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /filesystem

COPY ../util/pom.xml /util/pom.xml
WORKDIR /util
RUN mvn dependency:go-offline -B

COPY ../util .
RUN mvn -f /util/pom.xml clean install -DskipTests

WORKDIR /filesystem

COPY ./filesystem/pom.xml .
COPY ./filesystem/filesystem-model/pom.xml ./filesystem-model/pom.xml
COPY ./filesystem/filesystem-repository/pom.xml ./filesystem-repository/pom.xml
COPY ./filesystem/filesystem-service/pom.xml ./filesystem-service/pom.xml
COPY ./filesystem/filesystem-web/pom.xml ./filesystem-web/pom.xml
COPY ./filesystem/filesystem-app/pom.xml ./filesystem-app/pom.xml

RUN mvn dependency:go-offline -B

COPY ./filesystem .
RUN mkdir ./files
COPY ./store ./files
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /filesystem
COPY --from=build /filesystem/filesystem-app/target/*.jar filesystem.jar
EXPOSE 3001
ENTRYPOINT ["java", "-jar", "filesystem.jar"]
