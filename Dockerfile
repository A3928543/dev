# Usa una imagen base que incluya Maven y JDK
FROM maven:3.8.5-openjdk-17 AS build

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo pom.xml y las dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el código fuente
COPY src /app/src

# Compila el proyecto
RUN mvn package

# Usa una imagen base más ligera para la ejecución
FROM openjdk:17-jdk-slim

# Copia el archivo JAR construido desde la etapa de construcción
COPY --from=build /app/target/bootstrap-application-1.0.0.jar /app/app.jar

# Expone el puerto en el que la aplicación escuchará
EXPOSE 8080

# Define el comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
