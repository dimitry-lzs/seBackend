package com.softwareengineering.services;

import java.util.List;
import java.util.Map;
import com.softwareengineering.models.Appointment;
import com.softwareengineering.models.enums.Status;

public class AppointmentsService {
    public static List<Map<String, Object>> getDoctorAppointments(int doctorID) {

        List<Map<String, Object>> appointments = Appointment.where("doctorID = ?", doctorID).toMaps();

        return appointments;
    }

    public static List<Map<String, Object>> getDoctorAppointments(int doctorID, Status status) {

        List<Map<String, Object>> appointments = Appointment.where("doctorID = ? AND status >= ?", doctorID, status.toString())
                .toMaps();

        return appointments;
    }

    public static List<Map<String, Object>> getPatientAppointments(int patientID) {

        List<Map<String, Object>> appointments = Appointment.where("patientID = ?", patientID).toMaps();

        return appointments;
    }

    public static List<Map<String, Object>> getPatientAppointments(int patientID, Status status) {

        List<Map<String, Object>> appointments = Appointment.where("patientID = ? AND status >= ?", patientID, status.toString())
                .toMaps();

        return appointments;
    }

    public static void setAppointment(int doctorID, int patientID, int slotID, Status status) {
        Appointment appointment = new Appointment();
        appointment.set("doctorID", doctorID);
        appointment.set("patientID", patientID);
        appointment.set("slotID", slotID);
        appointment.set("status", status.toString());
        appointment.saveIt();
        AvailabilitiesService.updateAvailability(slotID, false);
    }

    public static void setAppointment(int doctorID, int patientID, int slotID, Status status, String reason) {
        Appointment appointment = new Appointment();
        appointment.set("doctorID", doctorID);
        appointment.set("patientID", patientID);
        appointment.set("slotID", slotID);
        appointment.set("status", status.toString());
        appointment.set("reason", reason);
        appointment.saveIt();
        AvailabilitiesService.updateAvailability(slotID, false);
    }

    public static void cancelAppointment(int appointmentID) {
        int rowsUpdated = Appointment.update("status = ?",
                "appointmentID = ?",
                Status.CANCELLED.toString(),
                appointmentID);
        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("Appointment with ID " + appointmentID + " not found.");
        } else {
            AvailabilitiesService.updateAvailability(
                    Appointment.findFirst("appointmentID = ?", appointmentID).getInteger("slotID"), true);
        }
    }
}
