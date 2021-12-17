FROM maven:latest as builder
COPY pom.xml .
COPY ./src ./src
COPY ./pom.xml ./pom.xml
RUN mvn clean package
FROM openjdk:8-jdk-alpine
VOLUME /db
COPY target/videos-retrieval-service-server-0.0.1-SNAPSHOT.jar videos-service.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/videos-service.jar","-web -webAllowOthers -tcp -tcpAllowOthers -browser"]
