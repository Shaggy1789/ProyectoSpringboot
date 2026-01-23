#Build
FROM eclipse-termurin:17-jdk AS Builder
WORKDIR /app

#Copiamos todo
COPY . .

#Damos permiso y construimos
RUN chmod +- gradlew && ./gradlew clean build -x test

#Runtime
From eclipse-termurin:17-jre


WORKDIR /app

#Copiar Jar
COPY --from=builder /app/build/libs/*.jar app.jar

#Exponemos el puerto
EXPOSE 8080

#Ejecutamos
ENTRYPOINT ["java", "-jar","app.jar"]
