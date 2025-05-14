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

    public static List<Map<String, Object>> getDoctorAppointments(int doctorID, java.sql.Timestamp date) {

        List<Map<String, Object>> appointments = Appointment.where("doctorID = ? AND date >= ?", doctorID, date).toMaps();

        return appointments;
    }

    public static List<Map<String, Object>> getPatientAppointments(int patientID) {

        List<Map<String, Object>> appointments = Appointment.where("patientID = ?", patientID).toMaps();

        return appointments;
    }

    public static List<Map<String, Object>> getPatientAppointments(int patientID, java.sql.Timestamp date) {

        List<Map<String, Object>> appointments = Appointment.where("patientID = ? AND date >= ?", patientID, date).toMaps();

        return appointments;
    }

    public static void setAppointment(int doctorID, int patientID, int slotID, Status status) {
        Appointment appointment = new Appointment();
        appointment.set("doctorID", doctorID);
        appointment.set("patientID", patientID);
        appointment.set("slotID", slotID);
        appointment.set("status", status.toString());
        appointment.saveIt();
    }

    public static void setAppointment(int doctorID, int patientID, int slotID, Status status, String reason) {
        Appointment appointment = new Appointment();
        appointment.set("doctorID", doctorID);
        appointment.set("patientID", patientID);
        appointment.set("slotID", slotID);
        appointment.set("status", status.toString());
        appointment.set("reason", reason);
        appointment.saveIt();
    }

    public static void updateAppointmentStatus(int appointmentID, Status status) {
        int rowsUpdated = Appointment.update("status = ?", "appointmentID = ?", status.toString(), appointmentID);
        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("Appointment with ID " + appointmentID + " not found.");
        }
    }
}
