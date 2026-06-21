package com.firstchatbot.backend.service;

import com.firstchatbot.backend.model.ChatMessage;
import com.firstchatbot.backend.model.ChatParameters;
import com.firstchatbot.backend.model.ChatResponse;

/**
 * LLMService interface for language model operations.
 * Implementations can use different LLM providers (HuggingFace, OpenAI, etc.)
 */
public interface LLMService {
    
    /**
     * Generate a response from the LLM for the given prompt.
     * 
     * @param prompt The input text to the model
     * @param parameters Optional parameters for controlling model behavior
     * @return ChatResponse with the generated text
     */
    ChatResponse generateResponse(String prompt, ChatParameters parameters);
    
    /**
     * Check if the LLM service is available and properly configured.
     * 
     * @return true if service is ready, false otherwise
     */
    boolean isAvailable();
    
    /**
     * Get information about the currently configured model.
     * 
     * @return String with model information
     */
    String getModelInfo();
}

