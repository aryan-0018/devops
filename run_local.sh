#!/usr/bin/env bash
# run_local.sh — Run SentinelOps AI locally on port 8088
set -e

echo "=== Building SentinelOps AI Microservice (1MS24CI026) ==="
mvn clean package -DskipTests

echo "=== Starting SentinelOps AI on port 8088 ==="
java -jar target/sentinel-aiops-1.0.0.jar
