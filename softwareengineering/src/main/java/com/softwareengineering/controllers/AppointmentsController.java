package com.softwareengineering.controllers;

import io.javalin.Javalin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

import com.softwareengineering.dto.AppointmentBody;
import com.softwareengineering.services.AppointmentsService;
import com.softwareengineering.utils.AuthUtils;
import com.softwareengineering.utils.AuthUtils.UnauthorizedException;

import io.javalin.http.Context;

public class AppointmentsController {
    public static void init(Javalin app) {
        app.get("/doctor-appointments", AppointmentsController::getDoctorAppointments);
        app.get("/patient-appointments", AppointmentsController::getPatientAppointments);
        app.get("/view-appointment-details", AppointmentsController::viewAppointmentDetails);
        app.post("/set-appointment", AppointmentsController::setAppointment);
        app.patch("/cancel-appointment", AppointmentsController::cancelAppointment);
        app.patch("/complete-appointment", AppointmentsController::completeAppointment);
    }

    private static void getDoctorAppointments(Context context) {
        try {
            int doctorID = AuthUtils.validateDoctorAndGetId(context);
            String dateParam = context.queryParam("date");

            List<Map<String, Object>> appointments;

            if (dateParam != null && !dateParam.isEmpty()) {
                try {
                    // Try to parse ISO 8601 format (2023-05-15T12:00:00)
                    LocalDateTime localDateTime = LocalDateTime.parse(
                            dateParam, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    Timestamp date = Timestamp.valueOf(localDateTime);

                    appointments = AppointmentsService.getDoctorAppointmentsAfterDate(doctorID, date);
                } catch (Exception e) {
                    context.status(400).json(Map.of("error",
                            "Invalid date format. Use ISO 8601 format (YYYY-MM-DDThh:mm:ss)"));
                    return;
                }
            } else {
                // No date filter, get all appointments
                appointments = AppointmentsService.getDoctorAppointments(doctorID);
            }

            context.json(appointments);
        } catch (UnauthorizedException e) {
            AuthUtils.handleUnauthorized(context, e);
        }
    }

    private static void getPatientAppointments(Context context) {
        try {
            int patientID = AuthUtils.validatePatientAndGetId(context);
            String dateParam = context.queryParam("date");

            List<Map<String, Object>> appointments;

            if (dateParam != null && !dateParam.isEmpty()) {
                try {
                    // Try to parse ISO 8601 format (2023-05-15T12:00:00)
                    LocalDateTime localDateTime = LocalDateTime.parse(
                            dateParam, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    Timestamp date = Timestamp.valueOf(localDateTime);

                    appointments = AppointmentsService.getPatientAppointmentsAfterDate(patientID, date);
                } catch (Exception e) {
                    context.status(400).json(Map.of("error",
                            "Invalid date format. Use ISO 8601 format (YYYY-MM-DDThh:mm:ss)"));
                    return;
                }
            } else {
                // No date filter, get all appointments
                appointments = AppointmentsService.getPatientAppointments(patientID);
            }

            context.json(appointments);
        } catch (UnauthorizedException e) {
            AuthUtils.handleUnauthorized(context, e);
        }
    }

    private static void setAppointment(Context context) {
        try {
            int patientID = AuthUtils.validatePatientAndGetId(context);

            AppointmentBody body = context.bodyAsClass(AppointmentBody.class);
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
        } catch (UnauthorizedException e) {
            AuthUtils.handleUnauthorized(context, e);
        }
    }

    private static void viewAppointmentDetails(Context context) {
        try {
            // Either doctor or patient can view appointment details - just validate
            // authentication
            Integer userId = context.sessionAttribute("id");
            String userType = context.sessionAttribute("userType");
            if (userId == null || userId == 0 || userType == null) {
                throw new UnauthorizedException("No valid session found");
            }

            String appointmentIDParam = context.queryParam("appointmentID");
            if (appointmentIDParam == null) {
                context.status(400).json(Map.of("error", "Appointment ID cannot be null"));
                return;
            }
            int appointmentID = Integer.parseInt(appointmentIDParam);
            Map<String, Object> appointmentDetails = AppointmentsService.getAppointmentDetails(appointmentID);
            if (appointmentDetails == null) {
                context.status(404).json(Map.of("error", "Appointment not found"));
                return;
            }
            context.json(appointmentDetails);
        } catch (UnauthorizedException e) {
            AuthUtils.handleUnauthorized(context, e);
        }
    }

    private static void cancelAppointment(Context context) {
        try {
            // Either doctor or patient can cancel appointment - just validate
            // authentication
            Integer userId = context.sessionAttribute("id");
            String userType = context.sessionAttribute("userType");
            if (userId == null || userId == 0 || userType == null) {
                throw new UnauthorizedException("No valid session found");
            }

            AppointmentBody body = context.bodyAsClass(AppointmentBody.class);
            if (body.appointmentID == null) {
                context.status(400).json(Map.of("error", "Appointment ID cannot be null"));
                return;
            } else {
                AppointmentsService.cancelAppointment(body.appointmentID);
                context.status(200);
            }
        } catch (UnauthorizedException e) {
            AuthUtils.handleUnauthorized(context, e);
        }
    }

    private static void completeAppointment(Context context) {
        try {
            // Only doctors can mark appointments as completed
            AuthUtils.validateDoctorAndGetId(context);

            AppointmentBody body = context.bodyAsClass(AppointmentBody.class);
            if (body.appointmentID == null) {
                context.status(400).json(Map.of("error", "Appointment ID cannot be null"));
                return;
            } else {
                AppointmentsService.completeAppointment(body.appointmentID);
                context.status(200).json(Map.of("message", "Appointment marked as completed"));
            }
        } catch (UnauthorizedException e) {
            AuthUtils.handleUnauthorized(context, e);
        }
    }
}