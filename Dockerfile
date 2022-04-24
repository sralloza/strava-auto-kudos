FROM ubuntu:20.04 AS base

RUN apt-get update && \
    apt-get install -y openjdk-11-jre-headless firefox-geckodriver && \
    apt-get clean;

FROM base as build

WORKDIR /home/gradle

COPY src/ /home/gradle/src/
COPY gradle/ /home/gradle/gradle/
COPY build.gradle settings.gradle gradlew /home/gradle/

RUN ./gradlew build
RUN ./gradlew test --scan
RUN ./gradlew fat -i

FROM base

RUN mkdir /app

COPY --from=build /home/gradle/build/libs/*.jar /app/strava-auto-kudos.jar
# COPY ./build/libs/*.jar /app/strava-auto-kudos.jar

ENTRYPOINT [ "java", "-jar", "/app/strava-auto-kudos.jar" ]
