package com.rit.sentinel.model;

public class PredictionRequest {
    private String podName;
    private double cpuThrottlePercent;
    private double memorySaturationPercent;
    private double errorRatePercent;
    private int activeReplicas;

    public PredictionRequest() {}

    public PredictionRequest(String podName, double cpuThrottlePercent, double memorySaturationPercent,
                             double errorRatePercent, int activeReplicas) {
        this.podName = podName;
        this.cpuThrottlePercent = cpuThrottlePercent;
        this.memorySaturationPercent = memorySaturationPercent;
        this.errorRatePercent = errorRatePercent;
        this.activeReplicas = activeReplicas;
    }

    public String getPodName() { return podName; }
    public void setPodName(String podName) { this.podName = podName; }

    public double getCpuThrottlePercent() { return cpuThrottlePercent; }
    public void setCpuThrottlePercent(double cpuThrottlePercent) { this.cpuThrottlePercent = cpuThrottlePercent; }

    public double getMemorySaturationPercent() { return memorySaturationPercent; }
    public void setMemorySaturationPercent(double memorySaturationPercent) { this.memorySaturationPercent = memorySaturationPercent; }

    public double getErrorRatePercent() { return errorRatePercent; }
    public void setErrorRatePercent(double errorRatePercent) { this.errorRatePercent = errorRatePercent; }

    public int getActiveReplicas() { return activeReplicas; }
    public void setActiveReplicas(int activeReplicas) { this.activeReplicas = activeReplicas; }
}
