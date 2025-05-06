package com.softwareengineering.controllers;

import io.javalin.Javalin;
import com.softwareengineering.services.AvailabilitiesService;
import java.util.List;
import java.util.Map;
import io.javalin.Context;

public class AvailabilitiesController {
    public static void init(Javalin app) {
        app.get("doctor-availabilities", AvailabilitiesController::getDoctorAvailabilities);
    }

    private static void getDoctorAvailabilities(Context context) {
        int doctorID = Integer.parseInt(context.queryParam("doctorID"));
        List<Map<String, Object>> availabilities = AvailabilitiesService.getDoctorAvailabilities(doctorID);
        context.json(availabilities);
    }
}
