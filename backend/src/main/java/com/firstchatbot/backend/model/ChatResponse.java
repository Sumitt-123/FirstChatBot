package com.firstchatbot.backend.model;

/**
 * ChatResponse represents the response body from chat endpoints.
 */
public class ChatResponse {
    private String message;
    private String status;
    private String sessionId;
    private String error;
    private String model;
    private Long processingTime;

    public ChatResponse() {
    }

    public ChatResponse(String message, String status, String sessionId, String error, String model, Long processingTime) {
        this.message = message;
        this.status = status;
        this.sessionId = sessionId;
        this.error = error;
        this.model = model;
        this.processingTime = processingTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
    }
}

