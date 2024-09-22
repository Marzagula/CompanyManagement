FROM openjdk:21-jdk-bullseye
COPY target/*.jar accounting-service.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/accounting-service.jar"]