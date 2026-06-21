package com.firstchatbot.backend.service;

import com.firstchatbot.backend.model.ChatMessage;
import com.firstchatbot.backend.model.ChatParameters;
import com.firstchatbot.backend.model.ChatRequest;
import com.firstchatbot.backend.model.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ChatService handles chat operations and orchestrates LLM interactions.
 * 
 * This service manages:
 * - Chat message processing
 * - Session management
 * - Context tracking
 * - LLM service integration
 */
@Service
public class ChatService {
    private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    
    private final LLMService llmService;
    
    // Simple in-memory session storage (in production, use database)
    private final Map<String, StringBuilder> conversationHistory = new HashMap<>();
    
    @Autowired
    public ChatService(LLMService llmService) {
        this.llmService = llmService;
    }
    
    /**
     * Process a chat message and generate a response.
     * 
     * @param chatRequest The chat request containing the user message
     * @return ChatResponse with the bot's response
     */
    public ChatResponse chat(ChatRequest chatRequest) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Validate input
            if (chatRequest.getMessage() == null || chatRequest.getMessage().trim().isEmpty()) {
                ChatResponse err = new ChatResponse();
                err.setStatus("error");
                err.setError("Message cannot be empty");
                err.setProcessingTime(System.currentTimeMillis() - startTime);
                return err;
            }
            
            // Get or create session
            String sessionId = chatRequest.getSessionId();
            if (sessionId == null || sessionId.isEmpty()) {
                sessionId = generateSessionId();
            }
            
            // Build prompt with context
            String prompt = buildPrompt(chatRequest.getMessage(), sessionId, chatRequest.getHistory());
            log.debug("Processing message for session {}: {}", sessionId, chatRequest.getMessage());
            
            // Get parameters or use defaults
            ChatParameters parameters = chatRequest.getParameters() != null
                    ? chatRequest.getParameters()
                    : new ChatParameters();
            
            // Generate response from LLM
            ChatResponse llmResponse = llmService.generateResponse(prompt, parameters);
            
            // Update conversation history
            if ("success".equals(llmResponse.getStatus())) {
                updateConversationHistory(sessionId, chatRequest.getMessage(), llmResponse.getMessage());
                llmResponse.setSessionId(sessionId);
                log.info("Message processed successfully for session {}", sessionId);
            }
            
            llmResponse.setProcessingTime(System.currentTimeMillis() - startTime);
            return llmResponse;
            
        } catch (Exception e) {
            log.error("Error processing chat request", e);
            ChatResponse err = new ChatResponse();
            err.setStatus("error");
            err.setError("Internal server error: " + e.getMessage());
            err.setProcessingTime(System.currentTimeMillis() - startTime);
            return err;
        }
    }
    
    /**
     * Build a prompt with context from conversation history.
     * 
     * @param userMessage The current user message
     * @param sessionId The conversation session ID
     * @param history Optional conversation history
     * @return Formatted prompt string
     */
    private String buildPrompt(String userMessage, String sessionId, java.util.List<ChatMessage> history) {
        StringBuilder prompt = new StringBuilder();
        
        // Add context from conversation history if available
        if (history != null && !history.isEmpty()) {
            for (ChatMessage msg : history) {
                if ("user".equals(msg.getFrom())) {
                    prompt.append("User: ").append(msg.getText()).append("\n");
                } else if ("bot".equals(msg.getFrom())) {
                    prompt.append("Bot: ").append(msg.getText()).append("\n");
                }
            }
        }
        
        // Add current message
        prompt.append("User: ").append(userMessage).append("\n");
        prompt.append("Bot:");
        
        return prompt.toString();
    }
    
    /**
     * Update conversation history for a session.
     * 
     * @param sessionId The session identifier
     * @param userMessage The user's message
     * @param botResponse The bot's response
     */
    private void updateConversationHistory(String sessionId, String userMessage, String botResponse) {
        conversationHistory.computeIfAbsent(sessionId, k -> new StringBuilder())
                .append("User: ").append(userMessage).append("\n")
                .append("Bot: ").append(botResponse).append("\n");
    }
    
    /**
     * Generate a unique session ID.
     * 
     * @return UUID string
     */
    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Get conversation history for a session.
     * 
     * @param sessionId The session identifier
     * @return Conversation history string
     */
    public String getConversationHistory(String sessionId) {
        return conversationHistory.getOrDefault(sessionId, new StringBuilder()).toString();
    }
    
    /**
     * Clear conversation history for a session.
     * 
     * @param sessionId The session identifier
     */
    public void clearSession(String sessionId) {
        conversationHistory.remove(sessionId);
        log.info("Session {} cleared", sessionId);
    }
    
    /**
     * Check if LLM service is available.
     * 
     * @return true if service is ready
     */
    public boolean isLLMAvailable() {
        return llmService.isAvailable();
    }
}

