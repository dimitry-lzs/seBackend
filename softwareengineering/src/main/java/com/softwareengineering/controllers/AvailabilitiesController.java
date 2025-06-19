package com.softwareengineering.controllers;

import io.javalin.Javalin;

import com.softwareengineering.services.AvailabilitiesService;
import com.softwareengineering.dto.AvailabilityBatchBody;
import com.softwareengineering.utils.AuthUtils;
import com.softwareengineering.utils.AuthUtils.UnauthorizedException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import io.javalin.http.Context;

public class AvailabilitiesController {
    public static void init(Javalin app) {
        app.get("/doctor-availabilities", AvailabilitiesController::getDoctorAvailabilities);
        app.post("/set-availability", AvailabilitiesController::setAvailability);
        app.patch("/cancel-availability", AvailabilitiesController::cancelAvailability);
    }

    private static void getDoctorAvailabilities(Context context) {
        try {
            int doctorId = AuthUtils.validateDoctorAndGetId(context);
            List<Map<String, Object>> availabilities = AvailabilitiesService.getDoctorAvailabilities(doctorId);
            context.json(availabilities);
        } catch (UnauthorizedException e) {
            AuthUtils.handleUnauthorized(context, e);
        }
    }

    private static void setAvailability(Context context) {
        try {
            int doctorId = AuthUtils.validateDoctorAndGetId(context);
            AvailabilityBatchBody body = context.bodyAsClass(AvailabilityBatchBody.class);
            if (body.slots == null || body.slots.isEmpty()) {
                context.status(400).json(Map.of("error", "Invalid request body"));
                return;
            }

            // Process each slot and collect any duplicate errors
            java.util.List<String> duplicateSlots = new java.util.ArrayList<>();
            java.util.List<String> successfulSlots = new java.util.ArrayList<>();

            for (Timestamp slot : body.slots) {
                try {
                    AvailabilitiesService.setAvailability(slot, doctorId);
                    successfulSlots.add(slot.toString());
                } catch (IllegalArgumentException e) {
                    if (e.getMessage().contains("already exists")) {
                        duplicateSlots.add(slot.toString());
                    } else {
                        // Re-throw other IllegalArgumentExceptions
                        throw e;
                    }
                }
            }

            // Prepare response based on results
            if (duplicateSlots.isEmpty()) {
                context.status(201).json(Map.of("message", "All availability slots set successfully"));
            } else if (successfulSlots.isEmpty()) {
                context.status(409).json(Map.of(
                    "error", "All slots already exist",
                    "duplicateSlots", duplicateSlots
                ));
            } else {
                context.status(207).json(Map.of(
                    "message", "Partially successful",
                    "successfulSlots", successfulSlots,
                    "duplicateSlots", duplicateSlots,
                    "warning", "Some slots already existed and were skipped"
                ));
            }
        } catch (UnauthorizedException e) {
            AuthUtils.handleUnauthorized(context, e);
        } catch (IllegalArgumentException e) {
            context.status(400).json(Map.of("error", e.getMessage()));
        }
    }

    private static void cancelAvailability(Context context) {
        int availabilityID = Integer.parseInt(context.queryParam("availabilityID"));
        AvailabilitiesService.updateAvailability(availabilityID, false);
        context.status(200).json(Map.of("message", "Availability cancelled successfully"));
    }
}
