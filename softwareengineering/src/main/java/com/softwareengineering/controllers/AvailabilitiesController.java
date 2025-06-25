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
        app.get("/get-doctor-availabilities", AvailabilitiesController::getSpecificDoctorAvailabilities);
        app.post("/set-availability", AvailabilitiesController::setAvailability);
        app.delete("/delete-availability/{availabilityID}", AvailabilitiesController::deleteAvailability);
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

    private static void getSpecificDoctorAvailabilities(Context context) {
        try {
            // Validate that the requester is a patient
            AuthUtils.validatePatientAndGetId(context);

            // Get doctorID from query parameter
            String doctorIdParam = context.queryParam("doctorID");
            if (doctorIdParam == null) {
                context.status(400).json(Map.of("error", "doctorID parameter is required"));
                return;
            }

            int doctorId;
            try {
                doctorId = Integer.parseInt(doctorIdParam);
            } catch (NumberFormatException e) {
                context.status(400).json(Map.of("error", "Invalid doctorID format"));
                return;
            }

            List<Map<String, Object>> availabilities = AvailabilitiesService.getDoctorAvailabilities(doctorId, true);
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

    private static void deleteAvailability(Context context) {
        try {
            int doctorId = AuthUtils.validateDoctorAndGetId(context);
            int availabilityId = Integer.parseInt(context.pathParam("availabilityID"));
            AvailabilitiesService.deleteAvailability(availabilityId, doctorId);
            context.status(200).json(Map.of("message", "Availability deleted successfully"));
        } catch (UnauthorizedException e) {
            AuthUtils.handleUnauthorized(context, e);
        } catch (NumberFormatException e) {
            context.status(400).json(Map.of("error", "Invalid availability ID format"));
        }
    }
}
