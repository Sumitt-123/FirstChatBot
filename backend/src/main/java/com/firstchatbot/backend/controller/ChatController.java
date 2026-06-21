package com.firstchatbot.backend.controller;

import com.firstchatbot.backend.model.ChatRequest;
import com.firstchatbot.backend.model.ChatResponse;
import com.firstchatbot.backend.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ChatController handles HTTP requests for chat functionality.
 * 
 * Endpoints:
 * - POST /api/chat - Send a message and get a response
 * - GET /api/health - Check API health and LLM availability
 * - DELETE /api/session/{sessionId} - Clear conversation history
 */
@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:5174", "http://localhost:5175"})
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    
    private final ChatService chatService;
    
    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    
    /**
     * Send a message to the chatbot and get a response.
     * 
     * @param chatRequest The chat request containing the user message
     * @return ResponseEntity with ChatResponse
     */
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
        log.info("Received chat request: {}", chatRequest.getMessage());
        
        try {
            ChatResponse response = chatService.chat(chatRequest);
            
            if ("success".equals(response.getStatus())) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("Error processing chat request", e);
            ChatResponse err = new ChatResponse();
            err.setStatus("error");
            err.setError("Internal server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }
    
    /**
     * Health check endpoint to verify API and LLM availability.
     * 
     * @return ResponseEntity with health status
     */
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        boolean llmAvailable = chatService.isLLMAvailable();
        
        HealthResponse res = new HealthResponse();
        res.setStatus("up");
        res.setLlmAvailable(llmAvailable);
        res.setTimestamp(System.currentTimeMillis());
        return ResponseEntity.ok(res);
    }
    
    /**
     * Clear conversation history for a specific session.
     * 
     * @param sessionId The session identifier
     * @return ResponseEntity with confirmation
     */
    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<MessageResponse> clearSession(@PathVariable String sessionId) {
        log.info("Clearing session: {}", sessionId);
        chatService.clearSession(sessionId);

        MessageResponse msg = new MessageResponse();
        msg.setMessage("Session cleared successfully");
        return ResponseEntity.ok(msg);
    }
    
    /**
     * Simple message response DTO.
     */
    public static class MessageResponse {
        private String message;

        public MessageResponse() {
        }

        public MessageResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
    
    /**
     * Health response DTO.
     */
    public static class HealthResponse {
        private String status;
        private boolean llmAvailable;
        private Long timestamp;

        public HealthResponse() {
        }

        public HealthResponse(String status, boolean llmAvailable, Long timestamp) {
            this.status = status;
            this.llmAvailable = llmAvailable;
            this.timestamp = timestamp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isLlmAvailable() {
            return llmAvailable;
        }

        public void setLlmAvailable(boolean llmAvailable) {
            this.llmAvailable = llmAvailable;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }
    }
}

