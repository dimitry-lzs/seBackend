package com.softwareengineering.controllers;

import io.javalin.Javalin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.softwareengineering.dto.AppointmentBody;
import com.softwareengineering.services.AppointmentsService;
import io.javalin.Context;

public class AppointmentsController {
    public static void init(Javalin app) {
        app.get("/doctor-appointments", AppointmentsController::getDoctorAppointments);
        app.get("/patient-appointments", AppointmentsController::getPatientAppointments);
        app.post("set-appointment", AppointmentsController::setAppointment);
        app.patch("update-appointment-status", AppointmentsController::updateAppointmentStatus);
    }

    private static void getDoctorAppointments(Context context) {
        int doctorID = context.sessionAttribute("id");
        if (context.queryParam("dateFrom") == null) {
            List<Map<String, Object>> appointments = AppointmentsService.getDoctorAppointments(doctorID);
            context.json(appointments);
            return;
        } else {
            Timestamp dateFrom = Timestamp.valueOf(context.queryParam("dateFrom"));
            List<Map<String, Object>> appointments = AppointmentsService.getDoctorAppointments(doctorID, dateFrom); //needs fixing
            context.json(appointments);
        }
    }

    private static void getPatientAppointments(Context context) {
        int patientID = context.sessionAttribute("id");
        if (context.queryParam("dateFrom") == null) {
            List<Map<String, Object>> appointments = AppointmentsService.getPatientAppointments(patientID);
            context.json(appointments);
            return;
        } else {
            Timestamp dateFrom = Timestamp.valueOf(context.queryParam("dateFrom"));
            List<Map<String, Object>> appointments = AppointmentsService.getPatientAppointments(patientID, dateFrom); //needs fixing
            context.json(appointments);
        }
    }

    private static void setAppointment(Context context) {
        AppointmentBody body = context.bodyAsClass(AppointmentBody.class);
        if (body.doctorID == null || body.patientID == null) {
            context.status(400).json(Map.of("error", "Doctor ID and Patient ID cannot be null"));
            return;
        }
        if (body.slotID == null) {
            context.status(400).json(Map.of("error", "slotID cannot be null"));
            return;
        }
        if (body.status == null) {
            context.status(400).json(Map.of("error", "Status cannot be null"));
            return;
        }
        if (body.reason == null) {
            AppointmentsService.setAppointment(body.doctorID, body.patientID, body.slotID, body.status);
            context.status(201);
            return;
        } else {
            AppointmentsService.setAppointment(body.doctorID, body.patientID, body.slotID, body.status, body.reason);
            context.status(201);
        }
    }

    private static void updateAppointmentStatus(Context context) {
        AppointmentBody body = context.bodyAsClass(AppointmentBody.class);
        if (body.appointmentID == null) {
            context.status(400).json(Map.of("error", "Appointment ID cannot be null"));
            return;
        }
        if (body.status == null) {
            context.status(400).json(Map.of("error", "Status cannot be null"));
            return;
        }
        AppointmentsService.updateAppointmentStatus(body.appointmentID, body.status);
        context.status(200);
    }
}