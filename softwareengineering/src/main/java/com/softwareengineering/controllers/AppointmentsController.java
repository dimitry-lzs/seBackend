package com.softwareengineering.controllers;

import io.javalin.Javalin;
import java.util.List;
import java.util.Map;

import com.softwareengineering.dto.AppointmentBody;
import com.softwareengineering.models.enums.Status;
import com.softwareengineering.services.AppointmentsService;

import io.javalin.http.Context;

public class AppointmentsController {
    public static void init(Javalin app) {
        app.get("/doctor-appointments", AppointmentsController::getDoctorAppointments);
        app.get("/patient-appointments", AppointmentsController::getPatientAppointments);
        app.post("set-appointment", AppointmentsController::setAppointment);
        app.patch("cancel-appointment", AppointmentsController::cancelAppointment);
    }

    private static void getDoctorAppointments(Context context) {
        int doctorID = context.sessionAttribute("id");

        String statusParam = context.queryParam("status");

        if (statusParam != null && Status.valueOf(statusParam.toUpperCase()) == null) {
            context.status(400).json(Map.of("error", "Invalid status parameter"));
            return;
        }

        List<Map<String, Object>> appointments;

        if (statusParam != null) {
            appointments = AppointmentsService.getDoctorAppointments(doctorID,
                    Status.valueOf(statusParam.toUpperCase()));
        } else {
            appointments = AppointmentsService.getDoctorAppointments(doctorID);
        }

        context.json(appointments);
        return;
    }

    private static void getPatientAppointments(Context context) {
        int patientID = context.sessionAttribute("id");
        String statusParam = context.queryParam("status");

        if (statusParam != null && Status.valueOf(statusParam.toUpperCase()) == null) {
            context.status(400).json(Map.of("error", "Invalid status parameter"));
            return;
        }

        List<Map<String, Object>> appointments;

        if (statusParam != null) {
            appointments = AppointmentsService.getPatientAppointments(patientID,
                    Status.valueOf(statusParam.toUpperCase()));
        } else {
            appointments = AppointmentsService.getPatientAppointments(patientID);
        }

        context.json(appointments);
        return;
    }

    private static void setAppointment(Context context) {
        AppointmentBody body = context.bodyAsClass(AppointmentBody.class);
        int patientID = context.sessionAttribute("id");
        if (body.doctorID == null) {
            context.status(400).json(Map.of("error", "Doctor ID cannot be null"));
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
        AppointmentsService.setAppointment(patientID, body.doctorID, body.slotID, body.status, body.reason);
        context.status(201);
        return;
    }

    private static void cancelAppointment(Context context) {
        AppointmentBody body = context.bodyAsClass(AppointmentBody.class);
        if (body.appointmentID == null) {
            context.status(400).json(Map.of("error", "Appointment ID cannot be null"));
            return;
        } else {
            AppointmentsService.cancelAppointment(body.appointmentID);
            context.status(200);
        }
    }
}