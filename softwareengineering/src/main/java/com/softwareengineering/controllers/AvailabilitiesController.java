package com.softwareengineering.controllers;

import io.javalin.Javalin;

import com.softwareengineering.services.AvailabilitiesService;
import com.softwareengineering.dto.AvailabilityBatchBody;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import io.javalin.Context;

public class AvailabilitiesController {
    public static void init(Javalin app) {
        app.get("doctor-availabilities", AvailabilitiesController::getDoctorAvailabilities);
        app.post("set-availability", AvailabilitiesController::setAvailability);
    }

    private static void getDoctorAvailabilities(Context context) {
        int doctorID = Integer.parseInt(context.queryParam("doctorID"));
        List<Map<String, Object>> availabilities = AvailabilitiesService.getDoctorAvailabilities(doctorID);
        context.json(availabilities);
    }

    private static void setAvailability(Context context) {
        AvailabilityBatchBody body = context.bodyAsClass(AvailabilityBatchBody.class);
        if (body.slots == null || body.slots.isEmpty()) {
            context.status(400).json(Map.of("error", "Invalid request body"));
            return;
        }
        for (Timestamp slot : body.slots) {
            AvailabilitiesService.setAvailability(slot, body.doctorID);
            context.status(201).json(Map.of("message", "Availability set successfully"));
        }
        
    }
}
