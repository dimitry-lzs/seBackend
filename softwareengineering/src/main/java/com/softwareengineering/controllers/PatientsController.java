package com.softwareengineering.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;
import java.util.Map;
import com.softwareengineering.models.User;
import com.softwareengineering.services.PatientsService;
import com.softwareengineering.utils.AuthUtils;
import com.softwareengineering.utils.AuthUtils.UnauthorizedException;

public class PatientsController {
    public static void init(Javalin app) {
        app.get("/patients", PatientsController::getPatients);
        app.get("/patients/{id}", PatientsController::getPatientByID);
    }

    private static void getPatients(Context context) {
        try {
            // Only doctors can access patient list
            AuthUtils.validateDoctorAndGetId(context);
            List<Map<String, Object>> patients = PatientsService.getPatients();
            context.json(patients);
        } catch (UnauthorizedException e) {
            AuthUtils.handleUnauthorized(context, e);
        }
    }

    private static void getPatientByID(Context context) {
        try {
            // Only doctors can access patient details
            AuthUtils.validateDoctorAndGetId(context);

            int patientId = Integer.parseInt(context.pathParam("id"));
            User patientModel = PatientsService.getPatientById(patientId);
            if (patientModel != null) {
                context.json(patientModel.toMap());
            } else {
                context.status(404).json(Map.of("error", "Patient not found"));
            }
        } catch (UnauthorizedException e) {
            AuthUtils.handleUnauthorized(context, e);
        }
    }
}
