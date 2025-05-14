package com.softwareengineering.controllers;

import io.javalin.Javalin;
import io.javalin.Context;
import java.util.List;
import java.util.Map;
import com.softwareengineering.models.User;
import com.softwareengineering.services.PatientsService;

public class PatientsController {
    public static void init(Javalin app) {
        app.get("/patients", PatientsController::getPatients);
        app.get("/get-patient", PatientsController::getPatientByID);
    }

    private static void getPatients(Context context) {
        List<Map<String, Object>> patients = PatientsService.getPatients();
        context.json(patients);
    }

    private static void getPatientByID(Context context) {
        int patientId = Integer.parseInt(context.queryParam("patientID"));
        User patientModel = PatientsService.getPatientById(patientId);
        if (patientModel != null) {
            context.json(patientModel.toMap());
        } else {
            context.status(404).json(Map.of("error", "Patient not found"));
        }
    }
}
