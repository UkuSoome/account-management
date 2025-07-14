FROM amazoncorretto:21 as builder
WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
COPY src ./src

RUN chmod +x gradlew
RUN ./gradlew clean bootJar --no-daemon

FROM amazoncorretto:21
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]