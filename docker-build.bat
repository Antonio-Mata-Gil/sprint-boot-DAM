@echo off
REM ============================================================================
REM docker-build.bat - Script para buildear y ejecutar Docker en Windows
REM ============================================================================

setlocal enabledelayedexpansion

echo.
echo ==========================================
echo 🔨 Docker Build Script - Demo Application
echo ==========================================
echo.

set IMAGE_NAME=demo-app
set IMAGE_TAG=latest
set CONTAINER_NAME=demo-app-container

REM Paso 1: Build imagen Docker
echo [*] Building Docker image...
docker build -t %IMAGE_NAME%:%IMAGE_TAG% .
if %errorlevel% equ 0 (
    echo [✓] Docker image built: %IMAGE_NAME%:%IMAGE_TAG%
) else (
    echo [X] Error building Docker image
    exit /b 1
)

REM Paso 2: Detener contenedor anterior
echo [*] Stopping previous container (if running)...
docker stop %CONTAINER_NAME% >nul 2>&1
docker rm %CONTAINER_NAME% >nul 2>&1
echo [✓] Previous container stopped/removed

REM Paso 3: Ejecutar docker-compose
echo [*] Starting Docker compose...
docker-compose up -d

if %errorlevel% equ 0 (
    echo.
    echo ==========================================
    echo 📱 Application Ready
    echo ==========================================
    echo.
    echo API endpoint: http://localhost:8000
    echo Actuator:    http://localhost:8000/api/actuator
    echo Health:      http://localhost:8000/api/actuator/health
    echo.
    echo Database:
    echo   Host:     localhost:3306
    echo   DB:       bbdd_pdau
    echo   User:     root
    echo.
    echo View logs:
    echo   docker-compose logs -f app
    echo.
    echo Stop containers:
    echo   docker-compose down
    echo ==========================================
    echo.
) else (
    echo [X] Error starting containers
    exit /b 1
)
