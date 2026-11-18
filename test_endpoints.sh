#!/usr/bin/env bash
# test_endpoints.sh — Automated verification for SentinelOps AI
TARGET_URL=${1:-"http://localhost:8088"}

echo "=========================================================="
echo "Testing SentinelOps AI Endpoints at ${TARGET_URL}"
echo "Student: Aryan M Desai | USN: 1MS24CI026"
echo "=========================================================="

echo -e "\n1. Testing GET /health:"
curl -s "${TARGET_URL}/health" | jq . || curl -s "${TARGET_URL}/health"

echo -e "\n\n2. Testing GET /api/v1/telemetry:"
curl -s "${TARGET_URL}/api/v1/telemetry" | jq . || curl -s "${TARGET_URL}/api/v1/telemetry"

echo -e "\n\n3. Testing POST /api/v1/predict (Nominal Workload):"
curl -s -X POST "${TARGET_URL}/api/v1/predict" \
  -H "Content-Type: application/json" \
  -d '{"podName":"sentinel-worker-1","cpuThrottlePercent":20.0,"memorySaturationPercent":35.0,"errorRatePercent":0.2,"activeReplicas":3}' | jq . || true

echo -e "\n\n4. Testing POST /api/v1/predict (Critical Workload / Autoscaling Trigger):"
curl -s -X POST "${TARGET_URL}/api/v1/predict" \
  -H "Content-Type: application/json" \
  -d '{"podName":"sentinel-worker-2","cpuThrottlePercent":94.0,"memorySaturationPercent":96.5,"errorRatePercent":4.8,"activeReplicas":3}' | jq . || true

echo -e "\n\nVerification complete."
