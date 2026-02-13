# 🚀 Despliegue de Spring Boot - Guía Completa

## 📋 Tabla de Contenidos

1. [Arquitectura](#arquitectura)
2. [Configuración Local](#configuración-local)
3. [Despliegue con Docker](#despliegue-con-docker)
4. [Despliegue en Koyeb (Producción)](#despliegue-en-koyeb-producción)
5. [Variables de Entorno](#variables-de-entorno)
6. [Troubleshooting](#troubleshooting)

---

## Arquitectura

```
┌─────────────────────────────────────┐
│         VERCEL (Frontend)           │
│     (React/Next.js Frontend)        │
└────────────────┬────────────────────┘
                 │ (API calls)
                 ▼
┌─────────────────────────────────────┐
│    KOYEB (Backend - Producción)     │
│   Spring Boot + Docker Container    │
│     Puerto: 8080 (interno)          │
└────────────────┬────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────┐
│  CLEVER CLOUD (Base de datos)       │
│ MySQL: b3kpj0azhmjzwfhgphrn         │
└─────────────────────────────────────┘
```

---

## Configuración Local

### 1️⃣ Requisitos previos

- ✅ Docker Desktop instalado ([descargar](https://www.docker.com/products/docker-desktop))
- ✅ Git instalado
- ✅ Repo clonado: `git clone <tu-repo>`

### 2️⃣ Variables de entorno

Tu archivo `.env.local` contiene:

```bash
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8000
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/bbdd_pdau?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=Antomola1@
SPRING_JPA_HIBERNATE_DDL_AUTO=none
APP_AUTH_PASSWORD=1234
```

⚠️ **NO commitear `.env.local`** - Solo para desarrollo local

### 3️⃣ Iniciar desarrollo local

**Opción A: Con Docker Compose (recomendado)**

```bash
# Iniciar app + DB
docker-compose up -d

# Ver logs
docker-compose logs -f app

# Detener
docker-compose down
```

**Opción B: Sin Docker (BD local con HeidiSQL)**

```bash
# Maven
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Gradle
gradle bootRun --args='--spring.profiles.active=dev'
```

### 4️⃣ Verificar que funciona

```bash
# Health check
curl http://localhost:8000/api/actuator/health

# Logs en tiempo real
docker-compose logs -f app
```

---

## Despliegue con Docker

### Estructura de archivos

```
proyecto/
├── Dockerfile                 ← Multi-stage build (dev + prod)
├── docker-compose.yml         ← Desarrollo local (app + BD)
├── .dockerignore              ← Archivos a excluir del build
├── .dockerignore-prod         ← Versión optimizada para producción
├── docker-build.sh            ← Script Linux/Mac
├── docker-build.bat           ← Script Windows
├── init-db.sql                ← Script BD (opcional)
└── DOCKER_COMMANDS.md         ← Referencia de comandos
```

### Build de Docker

**Desarrollo local:**
```bash
# Con docker-compose (TODO automático)
docker-compose up -d

# O build manual
docker build -t demo-app:dev .
docker run -it -p 8000:8080 demo-app:dev
```

**Producción (Koyeb):**
```bash
# Usar perfil prod
docker build -t demo-app:prod .

# Koyeb construirá automáticamente desde GitHub
```

### Etapas del Dockerfile

```dockerfile
Stage 1: Builder (Compilación)
  - FROM maven:3.9-eclipse-temurin-17-alpine
  - mvn clean package -DskipTests -P prod
  - Resultado: JAR compilado

Stage 2: Runtime (Producción)
  - FROM eclipse-temurin:17-jre-alpine
  - Copia JAR del Stage 1
  - Usuario no-root por seguridad
  - Health check incluido
  - Tamaño final: ~300MB
```

---

## Despliegue en Koyeb (Producción)

### Paso 1: Preparar Repo en GitHub

```bash
# Asegurarse de que todo esté commiteado
git status

# Agregar archivos Docker
git add Dockerfile docker-compose.yml .dockerignore koyeb.yml
git add .env.example application-prod.properties

# ⚠️ NO agregar .env.local
git add .gitignore  # Verificar que excluye .env.local

git commit -m "Add Docker and production configuration"
git push origin main
```

### Paso 2: Registrarse en Koyeb

1. Ir a https://www.koyeb.com
2. Sign up con GitHub
3. Autorizar acceso a repositorio

### Paso 3: Crear Secreto para Base de Datos

En Dashboard de Koyeb:
```
Settings → Secrets
Crear secreto:
  Name: db-password
  Value: ReHxYooYj57iilGPTUvC
```

### Paso 4: Desplegar Aplicación

```
Dashboard → New Service
Seleccionar:
  - GitHub Repository
  - Branch: main
  - Usar Dockerfile existente
  - O usar koyeb.yml

Environment Variables:
  SPRING_DATASOURCE_PASSWORD: ${db-password}  (usa secreto)
  Las demás desde koyeb.yml

Deploy!
```

### Paso 5: Verificar despliegue

```bash
# Tu aplicación estará en:
https://demo-backend-XXXXX.koyeb.app

# Health check:
https://demo-backend-XXXXX.koyeb.app/api/actuator/health

# Métricas:
https://demo-backend-XXXXX.koyeb.app/api/actuator/metrics
```

---

## Variables de Entorno

### Desarrollo Local (`.env.local`)

```bash
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8000
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/bbdd_pdau?...
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=Antomola1@
```

Cargadas por:
- `application.properties` (base)
- `application-dev.properties` (override)

### Producción (`.env.production`)

```bash
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
SPRING_DATASOURCE_URL=jdbc:mysql://b3kpj0azhmjzwfhgphrn-mysql.services.clever-cloud.com:3306/b3kpj0azhmjzwfhgphrn?...
SPRING_DATASOURCE_USERNAME=ur9ojuzggdbmcqgo
SPRING_DATASOURCE_PASSWORD=ReHxYooYj57iilGPTUvC
```

Cargadas por:
- `application.properties` (base)
- `application-prod.properties` (override)

### Sintaxis en Spring Boot

```properties
# Leer variable de entorno con default
server.port=${SERVER_PORT:8080}

# Leer variable de entorno (obligatoria)
spring.datasource.url=${SPRING_DATASOURCE_URL}
```

---

## Troubleshooting

### ❌ Error: "Cannot connect to database"

**Causa:** Contenedor MySQL no está corriendo o credenciales incorrectas

```bash
# Verificar que MySQL está activo
docker-compose ps

# Ver logs de MySQL
docker-compose logs db

# Reiniciar MySQL
docker-compose restart db
```

### ❌ Error: "Port 8000 already in use"

**Causa:** Otro proceso está usando el puerto

```bash
# Windows
netstat -ano | findstr :8000

# Mac/Linux
lsof -i :8000

# Solución: Cambiar puerto en docker-compose.yml
# Cambiar: - "8001:8080"
```

### ❌ Error: "App crashes after starting"

```bash
# Ver logs detallados
docker-compose logs -f app

# Aumentar memoria JVM en Dockerfile o .env.local
JAVA_OPTS="-Xmx512m -Xms256m"
```

### ❌ Error: "Build fails in Koyeb"

```bash
# Verificar que pom.xml existe
git ls-files pom.xml

# Verificar perfiles Maven
grep -A 5 "prod" pom.xml

# Hacer push con cambios
git commit -m "Fix: pom.xml" && git push
```

---

## 📚 Archivos de Referencia

- [`Dockerfile`](./Dockerfile) - Build multi-stage
- [`docker-compose.yml`](./docker-compose.yml) - Desarrollo local
- [`koyeb.yml`](./koyeb.yml) - Configuración Koyeb
- [`DOCKER_COMMANDS.md`](./DOCKER_COMMANDS.md) - Comandos útiles
- [`application.properties`](./src/main/resources/application.properties) - Base
- [`application-dev.properties`](./src/main/resources/application-dev.properties) - Desarrollo
- [`application-prod.properties`](./src/main/resources/application-prod.properties) - Producción

---

## 🔗 Enlaces útiles

- [Documentación Koyeb](https://docs.koyeb.com/)
- [Clever Cloud Add-ons](https://www.clever-cloud.com/doc/addons/mysql/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Spring Boot Docker](https://spring.io/guides/topicals/spring-boot-docker/)

---

## 📝 Próximos pasos

- [ ] Test local con `docker-compose up`
- [ ] Crear tablas en Clever Cloud (HeidiSQL)
- [ ] Desplegar en Koyeb
- [ ] Configurar CI/CD con GitHub Actions
- [ ] Configurar Nginx como proxy inverso
- [ ] Automatizar con HTTPS

---

**¿Dudas?** Consulta [`DOCKER_COMMANDS.md`](./DOCKER_COMMANDS.md) para más información.
