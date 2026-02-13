# Stage 1: Build - Compilar la aplicación con Maven
# ============================================================================
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copiar solo pom.xml para cachear dependencias
COPY pom.xml .

# Descargar dependencias (ejecutar primero para cachear)
RUN mvn dependency:go-offline -q

# Copiar código fuente
COPY src src

# Empaquetar sin tests y con perfil prod
RUN mvn clean package -DskipTests -P prod -q

# ============================================================================
# Stage 2: Runtime - Imagen mínima para producción
# ============================================================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Metadata
LABEL maintainer="Antonio Mata"
LABEL version="1.0"
LABEL description="Spring Boot Demo Application"

# Copiar JAR compilado desde stage anterior
COPY --from=builder /app/target/*.jar app.jar

# Usuario no-root por seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup && \
    chown -R appuser:appgroup /app

USER appuser

# Puerto de exposición
EXPOSE 8080

# Variables de entorno por defecto (pueden ser sobreescritas)
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/actuator/health || exit 1

# Ejecutar aplicación
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]