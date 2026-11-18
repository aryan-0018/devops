# SentinelOps AI — Anomaly Mitigation & Cloud Telemetry Engine

**Author**: Aryan M Desai  
**USN**: 1MS24CI026  
**Department**: Computer Science & Engineering (Artificial Intelligence and Machine Learning)  
**Institution**: M. S. Ramaiah Institute of Technology, Bangalore  
**Course**: DEVOPS (Course Code: `24CIAEC59` / `25CIAEC59`)  
**Faculty Guide**: Mr. Prashanth Reddy C, Senior Lecturer  

---

## 🚀 Project Overview

**SentinelOps AI** is an enterprise-grade cloud telemetry and incident mitigation microservice architected for cloud-native infrastructure. Engineered with **Java 17** and **Spring Boot 3.2.5**, SentinelOps evaluates container runtime telemetry, models memory saturation and CPU throttle heuristics, and dynamically recommends Kubernetes horizontal pod autoscaling adjustments to mitigate Out-Of-Memory (`OOMKilled`) failures.

This repository implements the complete end-to-end DevOps assembly line covering:
1. **Component 1**: Version control with Git branching, Apache Maven build lifecycle, Jenkins Declarative CI Pipeline with automated Surefire test report archiving, and dual CI via GitHub Actions.
2. **Component 2**: Idempotent configuration management using Ansible roles and handlers, multi-stage Alpine Docker containerization, Kubernetes declarative orchestration (Deployments, Services, ConfigMaps, Probes), elastic horizontal scaling, and automated rollback resilience.

---

## 🛠 Technology Stack & Environment

| Domain | Technology / Specification |
|---|---|
| **Programming Language & Framework** | Java 17 (OpenJDK), Spring Boot 3.2.5 |
| **Build Automation** | Apache Maven 3.9.x with Maven Surefire Plugin |
| **Version Control** | Git 2.4x / Git 2.5x with non-fast-forward feature branch workflow |
| **CI Automation** | Jenkins 2.x Declarative Pipeline & GitHub Actions CI |
| **Configuration Management** | Ansible 2.16+ (Roles, Decoupled Variables, Systemd Handlers) |
| **Container Engine** | Docker Engine 24.x (Multi-stage Alpine Linux base) |
| **Container Orchestration** | Kubernetes v1.29+ (Kind / Minikube local cluster) |
| **Cluster Ingress / Service** | Kubernetes NodePort Service on Port `30088` (Target Port `8088`) |

---

## 📋 Microservice API Endpoints

| HTTP Method | Endpoint | Description |
|---|---|---|
| `GET` | `/health` / `/api/v1/health` | Comprehensive health probe and student metadata verification (`1MS24CI026`) |
| `GET` | `/api/v1/telemetry` | Live pod resource consumption, anomaly index, and cluster QoS metrics |
| `POST` | `/api/v1/predict` | Anomaly inference engine evaluating throttle, memory, and error rate heuristics |
| `GET` | `/` | Responsive dark-mode telemetry dashboard with live interactive controls |

### Sample Health Payload (`GET /health`):
```json
{
  "status": "UP",
  "service": "sentinel-ops",
  "version": "1.0.0",
  "studentName": "Aryan M Desai",
  "studentUsn": "1MS24CI026",
  "courseCode": "24CIAEC59",
  "environment": "production",
  "region": "ap-south-1",
  "clusterName": "sentinel-k8s",
  "uptimeSeconds": 1420,
  "metrics": {
    "clusterHealthScore": 98.6,
    "activeReplicas": 3,
    "jvmAvailableProcessors": 4
  }
}
```

---

## 🔄 Component 1: Continuous Integration Pipeline

### 1. Git Branching Strategy
- Primary Branch: `main` (production-ready release baseline)
- Feature Branch: `sentinel` (feature development and scaffold)
- Merged via non-fast-forward merge: `git merge --no-ff sentinel`
- Auditable Semantic Release Tag: `v1.0.0`

### 2. Jenkins Declarative Pipeline (`Jenkinsfile`)
- **Checkout SCM**: Automatically clones repository upon commit detection.
- **Compile**: Clean builds project classes via `mvn clean compile`.
- **Unit Tests**: Executes 5 automated JUnit 5 test suites and archives Surefire XML reports.
- **Package**: Generates production JAR (`sentinel-aiops-1.0.0.jar`) and archives with cryptographic MD5 fingerprinting.
- **Trigger**: SCM polling on schedule `H/5 * * * *`.

### 3. CI Enhancements
1. **Automated JUnit 5 Test Publishing**: Comprehensive test validation with Surefire XML archiving.
2. **Dual Cloud CI via GitHub Actions**: Automated cloud builds on Ubuntu runners via `.github/workflows/ci.yml`.

---

## ⚙️ Component 2: Configuration Management & Kubernetes Deployment

### 1. Ansible Infrastructure Automation (`ansible/`)
- Target inventory configured in `inventory.ini`.
- Structured role architecture in `roles/sentinel_deploy`.
- Deploys JRE, provisions unprivileged service user `sentineluser`, copies JAR artifact, templates systemd service, and triggers event-driven restart handlers.
- Proven idempotency: `ok=10 changed=0 failed=0`.

### 2. Multi-Stage Alpine Dockerfile
- Minimal Alpine Linux runtime (`eclipse-temurin:17-jre-alpine`).
- Dedicated unprivileged non-root user (`sentineluser`).
- JVM heap constraints: `-Xms128m -Xmx512m`.

### 3. Kubernetes Declarative Manifests (`k8s/`)
- **ConfigMap (`configmap.yaml`)**: Decouples environment variables (`APP_ENV`, `SERVER_PORT`, `DEFAULT_REGION`, `CLUSTER_NAME`).
- **Deployment (`deployment.yaml`)**:
  - Replicas: 3
  - Labels: `app: sentinel-ops`, `student: "1MS24CI026"`
  - Self-healing HTTP Liveness & Readiness Probes on `/health` (port 8088).
  - Explicit resource requests and limits.
- **Service (`service.yaml`)**: NodePort service exposing target port 8088 via node port 30088.

### 4. Elastic Scaling Demonstration
```bash
# Verify initial 3 pods
kubectl get pods -l app=sentinel-ops -o wide

# Scale out to 5 replicas
kubectl scale deployment sentinel-ops --replicas=5
kubectl get pods -l app=sentinel-ops -o wide

# Reconcile back to 3 replicas
kubectl scale deployment sentinel-ops --replicas=3
```

### 5. Deployment Enhancements
1. **Automated Rollback Resilience**: Intentionally deployed invalid image tag (`sentinel-ops:v2-broken`). Kubernetes preserved healthy pods during `ImagePullBackOff`. Executed `kubectl rollout undo deployment/sentinel-ops` for zero-downtime recovery.
2. **ConfigMap Configuration Decoupling**: Operational parameters dynamically loaded without rebuilding images.
3. **Self-Healing Probes**: Automated restart on deadlocks and traffic rerouting on unready pods.

---

## 🧪 Verification & Testing

Run the test suite locally:
```bash
mvn clean test
```

Build the container image:
```bash
docker build -t sentinel-ops:1.0.0 .
```

Deploy manifests to Kubernetes:
```bash
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```
