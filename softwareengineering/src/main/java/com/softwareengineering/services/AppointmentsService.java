package com.softwareengineering.services;

import java.util.List;
import java.util.Map;
import com.softwareengineering.models.Appointment;

public class AppointmentsService {
    public static List<Map<String, Object>> getDoctorAppointments (int doctorID) {
        
        List<Map<String, Object>> appointments = Appointment.where("doctorID = ?", doctorID).toMaps();
        
        return appointments;
    }

    public static List<Map<String, Object>> getPatientAppointments (int patientID) {
        
        List<Map<String, Object>> appointments = Appointment.where("patientID = ?", patientID).toMaps();
        
        return appointments;
    }
}
