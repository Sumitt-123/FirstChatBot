package com.firstchatbot.backend.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.firstchatbot.backend.model.ChatParameters;
import com.firstchatbot.backend.model.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import okhttp3.*;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * HuggingFace LLM Service Implementation
 * 
 * Uses Hugging Face Inference API for free text generation.
 * Get free API token: https://huggingface.co/settings/tokens
 */
@Service
public class HuggingFaceLLMService implements LLMService {
    private static final Logger log = LoggerFactory.getLogger(HuggingFaceLLMService.class);
    
    private final String apiEndpoint;
    private final String apiToken;
    private final String model;
    private final Integer maxLength;
    private final OkHttpClient httpClient;
    private final Gson gson;
    
    public HuggingFaceLLMService(
            @Value("${llm.api.endpoint}") String apiEndpoint,
            @Value("${llm.api.token}") String apiToken,
            @Value("${llm.api.model}") String model,
            @Value("${llm.api.max-length}") Integer maxLength) {
        
        this.apiEndpoint = apiEndpoint;
        this.apiToken = apiToken;
        this.model = model;
        this.maxLength = maxLength;
        this.gson = new Gson();
        
        // Create HTTP client with timeout
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        
        log.info("HuggingFaceLLMService initialized with model: {}", model);
    }
    
    @Override
    public ChatResponse generateResponse(String prompt, ChatParameters parameters) {
        long startTime = System.currentTimeMillis();
        
        try {
            // If a mock is requested via env var, return a canned response immediately.
            String mockEnv = System.getenv("LLM_API_MOCK");
            if (mockEnv != null && (mockEnv.equalsIgnoreCase("1") || mockEnv.equalsIgnoreCase("true"))) {
                long processingTime = System.currentTimeMillis() - startTime;
                ChatResponse mock = new ChatResponse();
                mock.setStatus("success");
                mock.setMessage("This is a mock response because LLM_API_MOCK is enabled.");
                mock.setModel(model + " (mock)");
                mock.setProcessingTime(processingTime);
                return mock;
            }

            if (!isAvailable()) {
                ChatResponse err = new ChatResponse();
                err.setStatus("error");
                err.setError("LLM service is not available or API token is not configured");
                err.setProcessingTime(System.currentTimeMillis() - startTime);
                return err;
            }
            
            // Prepare request parameters
            ChatParameters params = parameters != null ? parameters : new ChatParameters();
            
            // Build request JSON
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("inputs", prompt);
            
            JsonObject parameters_obj = new JsonObject();
            parameters_obj.addProperty("max_new_tokens", params.getMaxLength());
            parameters_obj.addProperty("temperature", params.getTemperature());
            parameters_obj.addProperty("top_p", params.getTopP());
            requestBody.add("parameters", parameters_obj);
            
            // Create HTTP request
            // Router generation endpoint expects a query parameter specifying the model:
            // e.g. https://router.huggingface.co/hf-inference/generation?model=repo_id
            String url = apiEndpoint + "?model=" + URLEncoder.encode(model, StandardCharsets.UTF_8.name());
            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.parse("application/json")
            );
            
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + apiToken)
                    .post(body)
                    .build();
            
            log.debug("Sending request to HuggingFace API: {}", url);
            
            // Execute request
            try (Response response = httpClient.newCall(request).execute()) {
                long processingTime = System.currentTimeMillis() - startTime;
                
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    log.error("HuggingFace API error ({}): {}", response.code(), errorBody);
                    ChatResponse err = new ChatResponse();
                    err.setStatus("error");
                    err.setError("API returned status " + response.code());
                    err.setProcessingTime(processingTime);
                    return err;
                }
                
                String responseBody = response.body().string();
                log.debug("HuggingFace API response: {}", responseBody);
                
                // Parse response
                JsonArray responseArray = gson.fromJson(responseBody, JsonArray.class);
                
                if (responseArray != null && responseArray.size() > 0) {
                    JsonObject firstResult = responseArray.get(0).getAsJsonObject();
                    String generatedText = firstResult.get("generated_text").getAsString();

                    // Remove the original prompt from the response
                    String botResponse = generatedText.replace(prompt, "").trim();

                    ChatResponse ok = new ChatResponse();
                    ok.setMessage(botResponse);
                    ok.setStatus("success");
                    ok.setModel(model);
                    ok.setProcessingTime(processingTime);
                    return ok;
                }
                
                ChatResponse empty = new ChatResponse();
                empty.setStatus("error");
                empty.setError("Empty response from model");
                empty.setProcessingTime(processingTime);
                return empty;
                
            }
        } catch (IOException e) {
            long processingTime = System.currentTimeMillis() - startTime;
            log.error("Error calling HuggingFace API", e);
            ChatResponse err = new ChatResponse();
            err.setStatus("error");
            // If this was a DNS/host resolution issue, give a clearer message
            if (e instanceof UnknownHostException) {
                err.setError("LLM host not reachable: " + e.getMessage() + ". Check DNS/network/proxy settings.");
            } else {
                err.setError("IOException: " + e.getMessage());
            }
            err.setProcessingTime(processingTime);
            return err;
        } catch (Exception e) {
            long processingTime = System.currentTimeMillis() - startTime;
            log.error("Unexpected error in LLM service", e);
            ChatResponse err = new ChatResponse();
            err.setStatus("error");
            err.setError("Exception: " + e.getMessage());
            err.setProcessingTime(processingTime);
            return err;
        }
    }
    
    @Override
    public boolean isAvailable() {
        // Check if API token is configured (not default test-token)
        if (apiToken == null || apiToken.equals("test-token") || apiToken.isEmpty()) {
            log.warn("API token not configured. Set HUGGINGFACE_API_TOKEN environment variable");
            return false;
        }
        return true;
    }
    
    @Override
    public String getModelInfo() {
        return String.format("Model: %s, Endpoint: %s, Max Length: %d tokens", 
                model, apiEndpoint, maxLength);
    }
}

