FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

WORKDIR /app
COPY ./pom.xml /app/pom.xml
COPY ./src /app/src
RUN mvn clean package -DskipTests -Dcheckstyle.skip=true

FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S mygroup && adduser -S -G mygroup myuser
USER myuser

WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080/tcp
ENTRYPOINT ["java", "-jar", "app.jar"]