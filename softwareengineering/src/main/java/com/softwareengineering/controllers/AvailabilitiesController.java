package com.softwareengineering.controllers;

import io.javalin.Javalin;

import com.softwareengineering.services.AvailabilitiesService;
import com.softwareengineering.dto.AvailabilityBody;
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
        AvailabilityBody body = context.bodyAsClass(AvailabilityBody.class);
        if (body.date == null || body.timeFrom == null || body.timeTo == null) {
            context.status(400).json(Map.of("error", "Date, timeFrom, and timeTo cannot be null"));
            return;
        }
        if (body.doctorID == null) {
            context.status(400).json(Map.of("error", "Doctor ID cannot be null"));
            return;
        }
        AvailabilitiesService.setAvailability(body.date, body.timeFrom, body.timeTo, body.doctorID);
        context.status(201);
    }
}
