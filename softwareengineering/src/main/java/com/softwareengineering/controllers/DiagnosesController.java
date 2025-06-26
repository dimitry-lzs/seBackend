package com.softwareengineering.controllers;

import io.javalin.Javalin;

import com.softwareengineering.dto.DiagnosisBody;
import com.softwareengineering.utils.AuthUtils;
import com.softwareengineering.utils.AuthUtils.UnauthorizedException;

import java.util.List;
import java.util.Map;
import io.javalin.http.Context;
import com.softwareengineering.services.DiagnosesService;

public class DiagnosesController {
    public static void init(Javalin app) {
        app.get("/diagnoses", DiagnosesController::getDiagnoses);
        app.post("/diagnosis", DiagnosesController::setDiagnosis);
    }

    public static void getDiagnoses(Context context) {
        try {
            int patientID = AuthUtils.validatePatientAndGetId(context);
            List<Map<String, Object>> diagnoses = DiagnosesService.getDiagnoses(patientID);
            context.json(diagnoses);
        } catch (UnauthorizedException e) {
            AuthUtils.handleUnauthorized(context, e);
        }
    }

    public static void setDiagnosis(Context context) {
        try {
            // Only doctors can set diagnoses
            AuthUtils.validateDoctorAndGetId(context);

            DiagnosisBody body = context.bodyAsClass(DiagnosisBody.class);
            if (body.appointmentID == null) {
                context.status(400).json(Map.of("error", "Appointment ID cannot be null"));
                return;
            }
            if (body.decease == null) {
                context.status(400).json(Map.of("error", "Decease cannot be null"));
                return;
            }
            DiagnosesService.setDiagnosis(body.appointmentID, body.decease, body.details);
            context.status(201);
        } catch (UnauthorizedException e) {
            AuthUtils.handleUnauthorized(context, e);
        }
    }
}
