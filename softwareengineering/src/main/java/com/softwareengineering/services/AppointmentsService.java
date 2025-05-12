package com.softwareengineering.services;

import java.util.List;
import java.util.Map;
import com.softwareengineering.models.Appointment;
import com.softwareengineering.models.enums.Status;

public class AppointmentsService {
    public static List<Map<String, Object>> getDoctorAppointments (int doctorID) {
        
        List<Map<String, Object>> appointments = Appointment.where("doctorID = ?", doctorID).toMaps();
        
        return appointments;
    }

    public static List<Map<String, Object>> getPatientAppointments (int patientID) {
        
        List<Map<String, Object>> appointments = Appointment.where("patientID = ?", patientID).toMaps();
        
        return appointments;
    }

    public static void setAppointment (int doctorID, int patientID, java.sql.Timestamp timeFrom, java.sql.Timestamp timeTo, java.sql.Timestamp date, Status status) {
        Appointment appointment = new Appointment();
        appointment.set("doctorID", doctorID);
        appointment.set("patientID", patientID);
        appointment.set("timeFrom", timeFrom);
        appointment.set("timeTo", timeTo);
        appointment.set("date", date);
        appointment.set("status", status.toString());
        appointment.saveIt();
    }
}
