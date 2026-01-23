#Build
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

#Copiamos todo
COPY . .

#Damos permiso y construimos
RUN ./gradlew clean build --exclude-task test

#Runtime
FROM eclipse-temurin:17-jre


WORKDIR /app

#Copiar Jar
COPY --from=builder /app/build/libs/*.jar app.jar

#Exponemos el puerto
EXPOSE 8080

#Ejecutamos
ENTRYPOINT ["java", "-jar","app.jar"]
