#!/bin/zsh

echo "Cleaning and packaging with Maven..."
mvn clean install

echo "Building Docker images..."
  docker compose build --no-cache

echo "Starting Docker Compose services..."
docker compose up
