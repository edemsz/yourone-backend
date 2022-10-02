FROM maven:3-adoptopenjdk-14 AS build

COPY src /home/app/src
COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean compile  assembly:single package -DskipTests -Djar.finalName=yourone-backend -P production

RUN mv /home/app/target/yourone-backend-0.0.1-SNAPSHOT.jar /home/app/target/yourone-backend.jar

FROM openjdk:14-jdk-alpine

COPY --from=build /home/app/target/yourone-backend.jar /usr/local/lib/yourone-backend.jar

CMD ng serve --host 0.0.0.0 --disable-host-check

EXPOSE 8040
ENTRYPOINT ["java","-jar","/usr/local/lib/yourone-backend.jar"]
