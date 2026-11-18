package com.rit.sentinel.model;

import java.time.Instant;
import java.util.Map;

public class ClusterTelemetry {
    private String status;
    private String service;
    private String version;
    private String studentName;
    private String studentUsn;
    private String courseCode;
    private String environment;
    private String region;
    private String clusterName;
    private long uptimeSeconds;
    private String timestamp;
    private Map<String, Object> metrics;

    public ClusterTelemetry() {
        this.timestamp = Instant.now().toString();
    }

    public ClusterTelemetry(String status, String service, String version, String studentName,
                            String studentUsn, String courseCode, String environment, String region,
                            String clusterName, long uptimeSeconds, Map<String, Object> metrics) {
        this.status = status;
        this.service = service;
        this.version = version;
        this.studentName = studentName;
        this.studentUsn = studentUsn;
        this.courseCode = courseCode;
        this.environment = environment;
        this.region = region;
        this.clusterName = clusterName;
        this.uptimeSeconds = uptimeSeconds;
        this.timestamp = Instant.now().toString();
        this.metrics = metrics;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentUsn() { return studentUsn; }
    public void setStudentUsn(String studentUsn) { this.studentUsn = studentUsn; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getClusterName() { return clusterName; }
    public void setClusterName(String clusterName) { this.clusterName = clusterName; }

    public long getUptimeSeconds() { return uptimeSeconds; }
    public void setUptimeSeconds(long uptimeSeconds) { this.uptimeSeconds = uptimeSeconds; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public Map<String, Object> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }
}
