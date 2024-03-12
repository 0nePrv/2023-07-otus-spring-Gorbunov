FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

WORKDIR /app
COPY ./pom.xml /app/pom.xml
RUN mvn dependency:go-offline

COPY ./src /app/src
RUN mvn clean package -DskipTests -Dcheckstyle.skip=true

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /app/target/backend-*.jar /app/app.jar

EXPOSE 8080/tcp
ENTRYPOINT ["java", "-jar", "app.jar"]