FROM openjdk:21-jdk-bullseye
COPY target/*.jar employee-service.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/employee-service.jar"]