package com.softwareengineering.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.softwareengineering.dto.SummaryBody;
import com.softwareengineering.services.SummaryService;

import java.util.Map;

public class SummaryController {
    public static void init(Javalin app) {
        app.post("/generate-summary", SummaryController::generateSummary);
    }

    private static void generateSummary(Context context) {
        try {
            SummaryBody body = context.bodyAsClass(SummaryBody.class);

            if (body.prompt == null || body.prompt.trim().isEmpty()) {
                context.status(400).json(Map.of("error", "Prompt cannot be null or empty"));
                return;
            }

            Map<String, Object> result = SummaryService.generateSummary(body.prompt);
            context.status(200).json(result);

        } catch (IllegalStateException e) {
            context.status(500).json(Map.of("error", "Configuration error: " + e.getMessage()));
        } catch (Exception e) {
            context.status(500).json(Map.of("error", "Failed to generate summary: " + e.getMessage()));
        }
    }
}
