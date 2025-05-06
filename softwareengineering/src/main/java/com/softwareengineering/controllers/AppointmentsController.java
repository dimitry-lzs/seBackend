package com.softwareengineering.controllers;

import io.javalin.Javalin;

import java.util.List;
import java.util.Map;

import com.softwareengineering.services.AppointmentsService;
import io.javalin.Context;

public class AppointmentsController {
    public static void init(Javalin app) {
        app.get("/doctor-appointments", AppointmentsController::getDoctorAppointments);
        app.get("/patient-appointments", AppointmentsController::getPatientAppointments);
    }

    private static void getDoctorAppointments(Context context) {
        int doctorID = Integer.parseInt(context.queryParam("doctorID"));
        List<Map<String, Object>> appointments = AppointmentsService.getDoctorAppointments(doctorID);
        context.json(appointments);
    }

    private static void getPatientAppointments(Context context) {
        int patientID = Integer.parseInt(context.queryParam("patientID"));
        List<Map<String, Object>> appointments = AppointmentsService.getPatientAppointments(patientID);
        context.json(appointments);
    }
}