# Build aşaması (Maven ile .jar üret)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY settings.xml /root/.m2/settings.xml
COPY . .
RUN mvn clean package -DskipTests


# Runtime aşaması (yalnızca jar ile çalışan minimalist image)
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
