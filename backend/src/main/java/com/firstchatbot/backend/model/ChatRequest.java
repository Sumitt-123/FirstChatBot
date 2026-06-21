package com.firstchatbot.backend.model;

import java.util.List;

/**
 * ChatRequest represents the request body for chat endpoints.
 */
public class ChatRequest {
    private String message;
    private String sessionId;
    private List<ChatMessage> history;
    private ChatParameters parameters;

    public ChatRequest() {
    }

    public ChatRequest(String message, String sessionId, List<ChatMessage> history, ChatParameters parameters) {
        this.message = message;
        this.sessionId = sessionId;
        this.history = history;
        this.parameters = parameters;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<ChatMessage> getHistory() {
        return history;
    }

    public void setHistory(List<ChatMessage> history) {
        this.history = history;
    }

    public ChatParameters getParameters() {
        return parameters;
    }

    public void setParameters(ChatParameters parameters) {
        this.parameters = parameters;
    }
}

