package com.rit.sentinel;

import com.rit.sentinel.controller.SentinelOpsController;
import com.rit.sentinel.model.ClusterTelemetry;
import com.rit.sentinel.model.PredictionRequest;
import com.rit.sentinel.model.PredictionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SentinelOpsApplicationTests {

    @Autowired
    private SentinelOpsController controller;

    @Test
    @DisplayName("Verify Health Endpoint Metadata & Student USN")
    void testHealthEndpointMetadata() {
        ResponseEntity<ClusterTelemetry> response = controller.getHealth();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        ClusterTelemetry telemetry = response.getBody();
        assertNotNull(telemetry);
        assertEquals("UP", telemetry.getStatus());
        assertEquals("sentinel-ops", telemetry.getService());
        assertEquals("1MS24CI026", telemetry.getStudentUsn());
        assertEquals("Aryan M Desai", telemetry.getStudentName());
        assertEquals("24CIAEC59", telemetry.getCourseCode());
        assertTrue(telemetry.getMetrics().containsKey("clusterHealthScore"));
    }

    @Test
    @DisplayName("Verify Anomaly Inference Engine under Critical Load")
    void testCriticalAnomalyInference() {
        PredictionRequest request = new PredictionRequest("sentinel-ops-worker-1", 92.5, 95.0, 4.2, 3);
        ResponseEntity<PredictionResponse> response = controller.predictAnomaly(request);
        assertNotNull(response);
        PredictionResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("CRITICAL", body.getRiskLevel());
        assertEquals("HIGH", body.getOomKilledRisk());
        assertEquals(5, body.getRecommendedReplicas());
        assertTrue(body.getAnomalyProbability() >= 0.70);
        assertTrue(body.getEvaluatedBy().contains("1MS24CI026"));
    }

    @Test
    @DisplayName("Verify Anomaly Inference Engine under Nominal Thresholds")
    void testNominalThresholdEvaluation() {
        PredictionRequest request = new PredictionRequest("sentinel-ops-worker-2", 15.0, 25.0, 0.0, 3);
        ResponseEntity<PredictionResponse> response = controller.predictAnomaly(request);
        assertNotNull(response);
        PredictionResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("LOW", body.getRiskLevel());
        assertEquals("NEGLIGIBLE", body.getOomKilledRisk());
        assertEquals(3, body.getRecommendedReplicas());
        assertTrue(body.getAnomalyProbability() < 0.40);
    }

    @Test
    @DisplayName("Verify Intermediate Workload Triggers Proactive Autoscaling")
    void testMediumAnomalyEvaluation() {
        PredictionRequest request = new PredictionRequest("sentinel-ops-worker-3", 65.0, 60.0, 1.5, 3);
        ResponseEntity<PredictionResponse> response = controller.predictAnomaly(request);
        assertNotNull(response);
        PredictionResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("MEDIUM", body.getRiskLevel());
        assertEquals("MODERATE", body.getOomKilledRisk());
        assertEquals(4, body.getRecommendedReplicas());
    }

    @Test
    @DisplayName("Verify Telemetry Metrics and Regional Cluster Binding")
    void testTelemetryMetricsPayload() {
        ResponseEntity<Map<String, Object>> response = controller.getTelemetry();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        Map<String, Object> data = response.getBody();
        assertNotNull(data);
        assertEquals("sentinel-ops", data.get("service"));
        assertEquals("Aryan M Desai", data.get("student"));
        assertEquals("1MS24CI026", data.get("usn"));
        assertEquals(3, data.get("activePods"));
        assertEquals(5, data.get("maxReplicas"));
        assertNotNull(data.get("region"));
    }
}
