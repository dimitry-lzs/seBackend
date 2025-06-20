package com.softwareengineering.controllers;

import io.javalin.Javalin;

import com.softwareengineering.dto.DiagnosisBody;
import com.softwareengineering.models.Diagnosis;
import com.softwareengineering.utils.AuthUtils;
import com.softwareengineering.utils.AuthUtils.UnauthorizedException;

import java.util.List;
import java.util.Map;
import io.javalin.http.Context;
import com.softwareengineering.services.DiagnosesService;

public class DiagnosesController {
    public static void init(Javalin app) {
        app.get("/patient-diagnoses", DiagnosesController::getDiagnoses);
        app.post("/set-diagnosis", DiagnosesController::setDiagnosis);
        app.get("/get-diagnoses", DiagnosesController::getDiagnoses);
        app.get("/view-diagnosis", DiagnosesController::viewDiagnosis);
    }

    public static void getDiagnoses(Context context) {
        try {
            // Only patients can view their own diagnoses
            int patientID = AuthUtils.validatePatientAndGetId(context);
            List<Map<String, Object>> diagnoses = DiagnosesService.getDiagnoses(patientID);
            if (diagnoses == null || diagnoses.isEmpty()) {
                context.status(404).json(Map.of("error", "No diagnoses found for this patient"));
                return;
            } else {
                context.json(diagnoses);
            }
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

    public static void viewDiagnosis(Context context) {
        try {
            // Both doctors and patients can view diagnosis details - just validate authentication
            Integer userId = context.sessionAttribute("id");
            String userType = context.sessionAttribute("userType");
            if (userId == null || userId == 0 || userType == null) {
                throw new UnauthorizedException("No valid session found");
            }

            int appointmentID = Integer.parseInt(context.queryParam("appointmentID"));
            Diagnosis diagnosis = DiagnosesService.viewDiagnosis(appointmentID);
            if (diagnosis == null) {
                context.status(404).json(Map.of("message", "No diagnosis found for this appointment"));
                return;
            } else {
                context.json(new DiagnosisBody(diagnosis));
            }
        } catch (UnauthorizedException e) {
            AuthUtils.handleUnauthorized(context, e);
        }
    }

}
