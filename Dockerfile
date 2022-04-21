FROM adoptopenjdk/openjdk11:alpine-jre
MAINTAINER https://github.com/QAcuma
ARG JAR_FILE=build/libs/*.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]