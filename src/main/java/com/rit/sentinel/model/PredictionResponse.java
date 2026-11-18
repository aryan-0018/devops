package com.rit.sentinel.model;

public class PredictionResponse {
    private String podName;
    private double anomalyProbability;
    private String riskLevel; // LOW, MEDIUM, CRITICAL
    private String oomKilledRisk;
    private int recommendedReplicas;
    private String recommendationReason;
    private String evaluatedBy;

    public PredictionResponse() {}

    public PredictionResponse(String podName, double anomalyProbability, String riskLevel,
                              String oomKilledRisk, int recommendedReplicas,
                              String recommendationReason, String evaluatedBy) {
        this.podName = podName;
        this.anomalyProbability = anomalyProbability;
        this.riskLevel = riskLevel;
        this.oomKilledRisk = oomKilledRisk;
        this.recommendedReplicas = recommendedReplicas;
        this.recommendationReason = recommendationReason;
        this.evaluatedBy = evaluatedBy;
    }

    public String getPodName() { return podName; }
    public void setPodName(String podName) { this.podName = podName; }

    public double getAnomalyProbability() { return anomalyProbability; }
    public void setAnomalyProbability(double anomalyProbability) { this.anomalyProbability = anomalyProbability; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public String getOomKilledRisk() { return oomKilledRisk; }
    public void setOomKilledRisk(String oomKilledRisk) { this.oomKilledRisk = oomKilledRisk; }

    public int getRecommendedReplicas() { return recommendedReplicas; }
    public void setRecommendedReplicas(int recommendedReplicas) { this.recommendedReplicas = recommendedReplicas; }

    public String getRecommendationReason() { return recommendationReason; }
    public void setRecommendationReason(String recommendationReason) { this.recommendationReason = recommendationReason; }

    public String getEvaluatedBy() { return evaluatedBy; }
    public void setEvaluatedBy(String evaluatedBy) { this.evaluatedBy = evaluatedBy; }
}
