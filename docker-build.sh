#!/bin/bash
# ============================================================================
# docker-build.sh - Script para buildear y ejecutar Docker localmente
# ============================================================================

set -e  # Exit on error

echo "=========================================="
echo "🔨 Docker Build Script - Demo Application"
echo "=========================================="

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Variables
IMAGE_NAME="demo-app"
IMAGE_TAG="latest"
CONTAINER_NAME="demo-app-container"

# Función para imprimir
print_step() {
    echo -e "${YELLOW}▶ $1${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

# Paso 1: Build imagen Docker
print_step "Building Docker image..."
docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
print_success "Docker image built: ${IMAGE_NAME}:${IMAGE_TAG}"

# Paso 2: Detener contenedor anterior si está corriendo
print_step "Stopping previous container (if running)..."
docker stop ${CONTAINER_NAME} 2>/dev/null || true
docker rm ${CONTAINER_NAME} 2>/dev/null || true
print_success "Previous container stopped/removed"

# Paso 3: Ejecutar contenedor
print_step "Starting Docker compose..."
docker-compose up -d

print_success "Docker containers started!"
echo ""
echo "=========================================="
echo "📱 Application Ready"
echo "=========================================="
echo "API endpoint: http://localhost:8000"
echo "Actuator:    http://localhost:8000/api/actuator"
echo "Health:      http://localhost:8000/api/actuator/health"
echo ""
echo "Database:"
echo "  Host:     localhost:3306"
echo "  DB:       bbdd_pdau"
echo "  User:     root"
echo ""
echo "View logs:"
echo "  docker-compose logs -f app"
echo ""
echo "Stop containers:"
echo "  docker-compose down"
echo "=========================================="
