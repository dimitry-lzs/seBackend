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

    public static List<Map<String, Object>> getPatientAppointments(int patientID) {

        List<Map<String, Object>> appointments = Appointment.where("patientID = ?", patientID).toMaps();

        return appointments;
    }

    public static void setAppointment(int doctorID, int patientID, java.sql.Timestamp date, Status status) {
        Appointment appointment = new Appointment();
        appointment.set("doctorID", doctorID);
        appointment.set("patientID", patientID);
        appointment.set("date", date);
        appointment.set("status", status.toString());
        appointment.saveIt();
    }

    public static void updateAppointmentStatus(int appointmentID, Status status) {
        int rowsUpdated = Appointment.update("status = ?", "appointmentID = ?", status.toString(), appointmentID);
        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("Appointment with ID " + appointmentID + " not found.");
        }
    }
}
