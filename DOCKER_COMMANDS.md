# ============================================================================
# DOCKER_COMMANDS.md - Guía de comandos Docker para el proyecto
# ============================================================================

## 🚀 Inicio Rápido

### Con Docker Compose (RECOMENDADO - Todo automático)

```bash
# Iniciar aplicación + base de datos (desarrollo)
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f app

# Detener contenedores
docker-compose down

# Detener y eliminar volúmenes (limpiar todo)
docker-compose down -v
```

### Con Scripts (Windows/Linux)

**Windows:**
```batch
docker-build.bat
```

**Linux/Mac:**
```bash
chmod +x docker-build.sh
./docker-build.sh
```

---

## 🐳 Comandos Docker útiles

### Buildear imagen
```bash
# Build básico
docker build -t demo-app:latest .

# Build con output detallado (no silencioso)
docker build -t demo-app:latest . --progress=plain

# Build especificando stage
docker build -t demo-app:latest --target builder .
```

### Ejecutar contenedor
```bash
# Ejecutar con puertos y variables de entorno
docker run -d \
  --name demo-app \
  -p 8000:8080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/bbdd_pdau \
  demo-app:latest

# Ejecutar en modo interactivo
docker run -it demo-app:latest /bin/sh
```

### Gestionar contenedores
```bash
# Ver contenedores activos
docker ps

# Ver todos los contenedores (incluyendo parados)
docker ps -a

# Logs de un contenedor
docker logs demo-app
docker logs -f demo-app  # Seguir logs

# Ejecutar comando dentro del contenedor
docker exec -it demo-app /bin/sh

# Copiar archivo del contenedor
docker cp demo-app:/app/app.jar ./app.jar

# Parar contenedor
docker stop demo-app

# Reiniciar contenedor
docker restart demo-app

# Eliminar contenedor
docker rm demo-app
```

### Gestionar imágenes
```bash
# Listar imágenes
docker images

# Ver tamaño de imagen
docker images --no-trunc demo-app

# Eliminar imagen
docker rmi demo-app:latest

# Tag de imagen
docker tag demo-app:latest demo-app:v1.0
```

### Docker Compose específico

```bash
# Iniciar servicios específicos
docker-compose up -d app
docker-compose up -d db

# Rebuild imagen en compose
docker-compose up -d --build

# Ejecutar comando en servicio
docker-compose exec app /bin/sh

# Ver logs de servicio específico
docker-compose logs db

# Estadísticas de memoria/CPU
docker stats

# Limpiar volúmenes no usados
docker volume prune
```

---

## 🔍 Testing y Debugging

### Health Check
```bash
# Verificar salud de la aplicación
curl http://localhost:8000/api/actuator/health
curl http://localhost:8000/api/actuator/metrics
```

### Base de datos
```bash
# Acceder a MySQL dentro del contenedor
docker-compose exec db mysql -u root -pAntomola1@ bbdd_pdau

# Ejecutar query
docker-compose exec db mysql -u root -pAntomola1@ bbdd_pdau -e "SELECT * FROM usuarios;"
```

### Inspeccionar contenedor
```bash
# Ver información detallada del contenedor
docker inspect demo-app

# Ver red del contenedor
docker inspect demo-app | grep "NetworkSettings" -A 20
```

---

## 🎯 Troubleshooting

### Error: "Cannot connect to database"
```bash
# Verificar que MySQL está corriendo
docker ps | grep mysql

# Ver logs de MySQL
docker-compose logs db

# Reiniciar MySQL
docker-compose restart db
```

### Error: "Port already in use"
```bash
# Ver qué proceso usa el puerto 8000
lsof -i :8000  # Mac/Linux
netstat -ano | findstr :8000  # Windows

# Cambiar puerto en docker-compose.yml
# Línea: - "8001:8080" (cambiar el primer número)
```

### Error: "Out of memory"
```bash
# Aumentar memoria en Dockerfile
# Cambiar línea ENV JAVA_OPTS="-Xmx512m -Xms256m"
```

---

## 📦 Producción (Koyeb)

### Build para producción
```bash
# Usar .dockerignore-prod para omitir archivos innecesarios
cp .dockerignore-prod .dockerignore
docker build -t demo-app:prod .

# Cambiar back a .dockerignore original
git checkout .dockerignore
```

### Desplegar en Koyeb
```bash
# Opción 1: Desde GitHub (recomendado)
# - Push código a GitHub
# - Koyeb detecta Dockerfile
# - Deploy automático

# Opción 2: Desplegar imagen local
docker tag demo-app:prod your-registry/demo-app
docker push your-registry/demo-app
# Luego en Koyeb elegir esa imagen
```

---

## 📊 Monitoreo

### Ver métricas de contenedores
```bash
docker stats

# Salida en un archivo
docker stats --no-stream > stats.txt
```

### Limpiar espacio
```bash
# Eliminar imágenes no usadas
docker image prune

# Eliminar contenedores parados
docker container prune

# Eliminar volúmenes no usados
docker volume prune

# Eliminar TODO (peligroso)
docker system prune -a
```

---

## 🛠️ Utilidades

### Crear archivo .env
```bash
cp .env.example .env.local
# Editar y llenar valores
```

### Ver estructura de imagen
```bash
docker inspect demo-app:latest | jq '.ContainerConfig.Env'
```

### Comparar imágenes
```bash
docker images --digests | grep demo-app
```
