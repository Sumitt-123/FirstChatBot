package com.firstchatbot.backend.model;

import java.util.Objects;

/**
 * ChatMessage represents a message in the chat conversation.
 */
public class ChatMessage {
    private String from;
    private String text;
    private Long timestamp;
    private Double confidence;
    private String model;

    public ChatMessage() {
    }

    public ChatMessage(String from, String text, Long timestamp, Double confidence, String model) {
        this.from = from;
        this.text = text;
        this.timestamp = timestamp;
        this.confidence = confidence;
        this.model = model;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "from='" + from + '\'' +
                ", text='" + text + '\'' +
                ", timestamp=" + timestamp +
                ", confidence=" + confidence +
                ", model='" + model + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return Objects.equals(from, that.from) && Objects.equals(text, that.text) && Objects.equals(timestamp, that.timestamp) && Objects.equals(confidence, that.confidence) && Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, text, timestamp, confidence, model);
    }
}

