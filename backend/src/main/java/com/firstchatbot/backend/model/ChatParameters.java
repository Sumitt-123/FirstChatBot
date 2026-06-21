package com.firstchatbot.backend.model;

/**
 * ChatParameters for controlling LLM behavior.
 */
public class ChatParameters {
    private Double temperature = 0.7;
    private Integer maxLength = 100;
    private Double topP = 0.9;
    private Integer topK = 50;

    public ChatParameters() {
    }

    public ChatParameters(Double temperature, Integer maxLength, Double topP, Integer topK) {
        this.temperature = temperature;
        this.maxLength = maxLength;
        this.topP = topP;
        this.topK = topK;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Double getTopP() {
        return topP;
    }

    public void setTopP(Double topP) {
        this.topP = topP;
    }

    public Integer getTopK() {
        return topK;
    }

    public void setTopK(Integer topK) {
        this.topK = topK;
    }
}

