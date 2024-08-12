FROM openjdk:21-jdk-bullseye
COPY target/*.jar auth-service.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/auth-service.jar"]