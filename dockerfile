FROM eclipse-temurin:21-jre
COPY app/build/libs/app-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]