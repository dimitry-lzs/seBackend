package com.softwareengineering.controllers;

import io.javalin.Javalin;

import com.softwareengineering.dto.DiagnosisBody;

import java.util.List;
import java.util.Map;
import io.javalin.Context;
import com.softwareengineering.services.DiagnosesService;
public class DiagnosesController {
    public static void init(Javalin app) {
        app.get("/patient-diagnoses", DiagnosesController::getDiagnoses);
        app.post("/set-diagnosis", DiagnosesController::setDiagnosis);
        app.get("/get-diagnoses", DiagnosesController::getDiagnoses);
    }

    public static void getDiagnoses(Context context) {
        int patientID = context.sessionAttribute("id");
        List<Map<String, Object>> diagnoses = DiagnosesService.getDiagnoses(patientID);
        if(diagnoses == null || diagnoses.isEmpty()) {
            context.status(404).json(Map.of("error", "No diagnoses found for this patient"));
            return;
        }else{
        context.json(diagnoses);
        }
    }

    public static void setDiagnosis(Context context) {
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
    }

}
