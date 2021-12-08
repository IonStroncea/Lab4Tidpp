FROM openjdk:8-jdk-alpine
RUN ./mvn clean install
COPY target/videos-retrieval-service-server-0.0.1-SNAPSHOT.jar videos-service.jar
ENTRYPOINT ["java","-jar","/videos-service.jar","-web -webAllowOthers -tcp -tcpAllowOthers -browser"]
EXPOSE 8081
