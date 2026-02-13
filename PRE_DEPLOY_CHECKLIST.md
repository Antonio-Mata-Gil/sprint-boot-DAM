# ✅ Pre-Deploy Checklist

Antes de desplegar en Koyeb, verifica todo esto:

## 🔧 Configuración Local

- [ ] **Docker Desktop activo**
  ```bash
  docker --version
  ```

- [ ] **Archivo `.env.local` correcto**
  ```bash
  cat .env.local
  # Debe tener: SPRING_DATASOURCE_URL, USERNAME, PASSWORD
  ```

- [ ] **pom.xml con perfil `prod`**
  ```bash
  grep -A 5 "<id>prod</id>" pom.xml
  ```

- [ ] **application-prod.properties existe**
  ```bash
  ls src/main/resources/application-prod.properties
  ```

---

## 🐳 Docker

- [ ] **Dockerfile construye sin errores**
  ```bash
  docker build -t demo-app:test .
  ```

- [ ] **docker-compose.yml es válido**
  ```bash
  docker-compose config
  ```

- [ ] **Contenedor arranca correctamente**
  ```bash
  docker-compose up -d
  docker-compose logs app | head -50
  curl http://localhost:8000/api/actuator/health
  docker-compose down
  ```

---

## 📦 Dependencias

- [ ] **Sin dependencias de test en producción**
  ```bash
  grep -i "scope.*test" pom.xml | wc -l
  # Deben estar en <scope>test</scope>
  ```

- [ ] **Ningún SNAPSHOT en pom.xml**
  ```bash
  grep -i "SNAPSHOT" pom.xml
  # NO debe haber líneas (excepto comentarios)
  ```

---

## 🗄️ Base de Datos

- [ ] **Credenciales Clever Cloud correctas**
  ```bash
  # Verificar en .env.production
  cat .env.production | grep SPRING_DATASOURCE
  ```

- [ ] **Tablas creadas en Clever Cloud**
  ```
  Conectar con HeidiSQL:
  Host: b3kpj0azhmjzwfhgphrn-mysql.services.clever-cloud.com
  User: ur9ojuzggdbmcqgo
  Pass: ReHxYooYj57iilGPTUvC
  DB: b3kpj0azhmjzwfhgphrn
  ```

- [ ] **DDL-AUTO=validate en producción**
  ```bash
  grep "ddl-auto=validate" src/main/resources/application-prod.properties
  ```

---

## 📝 Git Repository

- [ ] **Todo commiteado**
  ```bash
  git status
  # No debe haber cambios sin committear
  ```

- [ ] **Archivos sensibles NO versionados**
  ```bash
  git ls-files | grep -E "\.env\.|\.local" | wc -l
  # Debe ser 0 (excepto .env.example)
  ```

- [ ] **.gitignore actualizado**
  ```bash
  cat .gitignore | grep -E "\.env|\.local"
  # Debe excluir .env.local y similares
  ```

- [ ] **Branch main actualizado**
  ```bash
  git status
  git push origin main
  ```

---

## 🌐 Koyeb Setup

- [ ] **Cuenta Koyeb creada** (sin tarjeta)
  ```
  https://www.koyeb.com
  ```

- [ ] **Secreto `db-password` creado**
  ```
  Koyeb Dashboard → Settings → Secrets
  Name: db-password
  Value: ReHxYooYj57iilGPTUvC
  ```

- [ ] **Repository conectado a Koyeb**
  ```
  Dashboard → Service → GitHub Repository
  ```

- [ ] **Dockerfile visible en Koyeb**
  ```
  Koyeb debería detectar automáticamente
  ```

---

## 🔐 Variables de Entorno

- [ ] **SPRING_PROFILES_ACTIVE=prod en Koyeb**
  ```bash
  # En koyeb.yml:
  # - name: SPRING_PROFILES_ACTIVE
  #   value: prod
  ```

- [ ] **SPRING_DATASOURCE_PASSWORD usa secreto**
  ```bash
  # En koyeb.yml:
  # - name: SPRING_DATASOURCE_PASSWORD
  #   valueFrom:
  #     secretKeyRef:
  #       name: db-password
  #       key: password
  ```

- [ ] **Todas las variables definidas**
  ```
  SERVER_PORT, SPRING_PROFILES_ACTIVE
  SPRING_DATASOURCE_URL, USERNAME, PASSWORD
  SPRING_JPA_HIBERNATE_DDL_AUTO
  ```

---

## 🧪 Pre-Deploy Final

- [ ] **Maven build local sin errores**
  ```bash
  mvn clean package -P prod -DskipTests
  ```

- [ ] **JAR se puede ejecutar**
  ```bash
  java -jar target/demo-*.jar --spring.profiles.active=prod
  # Ctrl+C para detener
  ```

- [ ] **Health endpoint responde**
  ```bash
  curl -X GET http://localhost:8080/api/actuator/health
  # Debe devolver: {"status":"UP"}
  ```

---

## 📋 Koyeb Deploy Checklist

1. [ ] Push final a GitHub
   ```bash
   git add .
   git commit -m "Ready for production deployment"
   git push origin main
   ```

2. [ ] Crear nuevo servicio en Koyeb
   ```
   Dashboard → New Service → Select GitHub Repo
   ```

3. [ ] Configurar ambiente
   ```
   - Seleccionar main branch
   - Dockerfile: auto-detect
   - Environment: usar variables de koyeb.yml
   ```

4. [ ] Deploy!
   ```
   Click "Deploy" y esperar 2-5 minutos
   ```

5. [ ] Verificar que funciona
   ```bash
   https://demo-backend-XXXXX.koyeb.app/api/actuator/health
   ```

---

## 🚨 Si algo falla

1. **Ver logs en Koyeb**
   ```
   Dashboard → Service → Logs
   ```

2. **Ver eventos**
   ```
   Dashboard → Service → Events
   ```

3. **Rollback**
   ```
   Dashboard → Service → Deployments → Seleccionar anterior
   ```

4. **Contactar soporte**
   ```
   Koyeb Discord: https://discord.gg/koyeb
   ```

---

## ✨ Éxito!

Si llegaste aquí, tu aplicación está lista para producción:

- ✅ Docker configurado y testeado
- ✅ Base de datos en Clever Cloud
- ✅ Variables de entorno correctas
- ✅ Koyeb preparado
- ✅ GitHub sincronizado

**¡Dale al Deploy en Koyeb! 🚀**
