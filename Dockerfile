FROM sralloza/openjdk:11-jre as build

WORKDIR /home/gradle

COPY gradle/ /home/gradle/gradle/
COPY build.gradle settings.gradle gradlew /home/gradle/
COPY src/ /home/gradle/src/

RUN ./gradlew build
RUN ./gradlew test --scan
RUN ./gradlew fat -i

FROM sralloza/openjdk:11-jre

RUN mkdir /app

COPY --from=build /home/gradle/build/libs/*.jar /app/strava-auto-kudos.jar

ENTRYPOINT [ "java", "-jar", "/app/strava-auto-kudos.jar" ]
