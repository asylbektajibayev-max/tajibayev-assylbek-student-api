FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace
COPY gradlew gradlew.bat build.gradle settings.gradle ./
COPY gradle gradle
RUN chmod +x gradlew
COPY src src
RUN --mount=type=cache,id=midterm-38288-gradle,target=/root/.gradle ./gradlew --no-daemon clean bootJar
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
RUN groupadd --system app && useradd --system --gid app app
COPY --from=build --chown=app:app /workspace/build/libs/student-api.jar /app/app.jar
USER app
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
