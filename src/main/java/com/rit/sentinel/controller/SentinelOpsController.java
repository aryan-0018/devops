package com.rit.sentinel.controller;

import com.rit.sentinel.model.ClusterTelemetry;
import com.rit.sentinel.model.PredictionRequest;
import com.rit.sentinel.model.PredictionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class SentinelOpsController {

    @Value("${sentinel.student.name:Aryan M Desai}")
    private String studentName;

    @Value("${sentinel.student.usn:1MS24CI026}")
    private String studentUsn;

    @Value("${sentinel.course.code:24CIAEC59}")
    private String courseCode;

    @Value("${sentinel.environment:production}")
    private String environment;

    @Value("${sentinel.region:ap-south-1}")
    private String region;

    @Value("${sentinel.cluster.name:sentinel-k8s}")
    private String clusterName;

    @GetMapping({"/health", "/api/v1/health"})
    public ResponseEntity<ClusterTelemetry> getHealth() {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000;
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("jvmAvailableProcessors", Runtime.getRuntime().availableProcessors());
        metrics.put("freeMemoryBytes", Runtime.getRuntime().freeMemory());
        metrics.put("totalMemoryBytes", Runtime.getRuntime().totalMemory());
        metrics.put("clusterHealthScore", 98.6);
        metrics.put("activeReplicas", 3);

        ClusterTelemetry telemetry = new ClusterTelemetry(
                "UP",
                "sentinel-ops",
                "1.0.0",
                studentName,
                studentUsn,
                courseCode,
                environment,
                region,
                clusterName,
                uptime,
                metrics
        );
        return ResponseEntity.ok(telemetry);
    }

    @GetMapping("/api/v1/telemetry")
    public ResponseEntity<Map<String, Object>> getTelemetry() {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000;
        Map<String, Object> response = new HashMap<>();
        response.put("service", "sentinel-ops");
        response.put("student", studentName);
        response.put("usn", studentUsn);
        response.put("cluster", clusterName);
        response.put("region", region);
        response.put("uptimeSeconds", uptime);
        response.put("avgCpuUtilizationPercent", 68.4);
        response.put("memorySaturationPercent", 72.1);
        response.put("errorRatePercent", 0.04);
        response.put("anomalyIndex", 0.18);
        response.put("activePods", 3);
        response.put("minReplicas", 3);
        response.put("maxReplicas", 5);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/v1/predict")
    public ResponseEntity<PredictionResponse> predictAnomaly(@RequestBody PredictionRequest request) {
        double cpu = Math.max(0.0, Math.min(100.0, request.getCpuThrottlePercent()));
        double mem = Math.max(0.0, Math.min(100.0, request.getMemorySaturationPercent()));
        double err = Math.max(0.0, Math.min(100.0, request.getErrorRatePercent()));
        int replicas = request.getActiveReplicas() > 0 ? request.getActiveReplicas() : 3;

        // Weighted anomaly probability heuristic
        double score = (0.45 * (cpu / 100.0)) + (0.40 * (mem / 100.0)) + (0.15 * (err / 10.0));
        score = Math.min(1.0, Math.max(0.0, score));

        String riskLevel;
        String oomRisk;
        int targetReplicas;
        String reason;

        if (score >= 0.70) {
            riskLevel = "CRITICAL";
            oomRisk = "HIGH";
            targetReplicas = Math.max(replicas + 2, 5);
            reason = "High memory saturation & severe CPU throttle detected. Immediate cluster scale-out to " 
                     + targetReplicas + " pods required to prevent OOMKilled cascade.";
        } else if (score >= 0.40) {
            riskLevel = "MEDIUM";
            oomRisk = "MODERATE";
            targetReplicas = Math.max(replicas + 1, 4);
            reason = "Elevated resource consumption. Recommending proactive expansion to " 
                     + targetReplicas + " pods.";
        } else {
            riskLevel = "LOW";
            oomRisk = "NEGLIGIBLE";
            targetReplicas = 3;
            reason = "Workload metrics within nominal operational thresholds. Cluster baseline maintained.";
        }

        PredictionResponse response = new PredictionResponse(
                request.getPodName() != null ? request.getPodName() : "sentinel-worker-pod",
                Math.round(score * 1000.0) / 1000.0,
                riskLevel,
                oomRisk,
                targetReplicas,
                reason,
                "SentinelOps AI Engine (" + studentName + " - " + studentUsn + ")"
        );

        return ResponseEntity.ok(response);
    }
}
