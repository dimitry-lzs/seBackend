package com.softwareengineering.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class SummaryService {
    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String DEFAULT_MODEL = "meta-llama/llama-4-scout-17b-16e-instruct";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    // Default parameters - configurable in backend
    private static final double DEFAULT_TEMPERATURE = 1.0;
    private static final int DEFAULT_MAX_TOKENS = 1024;
    private static final double DEFAULT_TOP_P = 1.0;

    public static Map<String, Object> generateSummary(String prompt) throws IOException {
        String apiKey = getApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("GROQ_API_KEY environment variable not set");
        }

        JsonObject requestBody = createRequestBody(prompt);

        Request request = new Request.Builder()
                .url(GROQ_API_URL)
                .post(RequestBody.create(requestBody.toString(), JSON))
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API request failed: " + response.code() + " " + response.message());
            }

            String responseBody = response.body().string();
            @SuppressWarnings("unchecked")
            Map<String, Object> result = gson.fromJson(responseBody, Map.class);
            return result;
        }
    }

    private static JsonObject createRequestBody(String prompt) {
        JsonObject requestBody = new JsonObject();

        JsonArray messages = new JsonArray();
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);
        messages.add(message);

        requestBody.add("messages", messages);
        requestBody.addProperty("model", DEFAULT_MODEL);
        requestBody.addProperty("temperature", DEFAULT_TEMPERATURE);
        requestBody.addProperty("max_completion_tokens", DEFAULT_MAX_TOKENS);
        requestBody.addProperty("top_p", DEFAULT_TOP_P);
        requestBody.addProperty("stream", false);

        return requestBody;
    }

    private static String getApiKey() {
        // Load .env file for development mode
        com.softwareengineering.utils.EnvLoader.loadEnv();

        // Try environment variable first, then system property
        String apiKey = System.getenv("GROQ_API_KEY");
        if (apiKey == null) {
            apiKey = System.getProperty("GROQ_API_KEY");
        }
        return apiKey;
    }
}
