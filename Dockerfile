FROM bellsoft/liberica-openjdk-alpine:17
MAINTAINER https://github.com/QAcuma
ARG JAR_FILE=build/libs/*.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
COPY .env .env
ENTRYPOINT ["java","-jar","app.jar"]
