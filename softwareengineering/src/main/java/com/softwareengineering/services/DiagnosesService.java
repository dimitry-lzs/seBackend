package com.softwareengineering.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.softwareengineering.models.Appointment;
import com.softwareengineering.models.Diagnosis;
import com.softwareengineering.models.enums.Status;

public class DiagnosesService {

    public static void setDiagnosis(int appointmentID, String decease, String details) {
        // First, check if the appointment exists and is in PENDING status
        Appointment appointment = Appointment.findFirst("appointmentID = ?", appointmentID);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment with ID " + appointmentID + " not found.");
        }

        String currentStatus = appointment.getString("status");
        if (!Status.PENDING.toString().equals(currentStatus)) {
            throw new IllegalStateException("Can only diagnose pending appointments. Current status: " + currentStatus);
        }

        // Create the diagnosis
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.set("appointmentID", appointmentID);
        diagnosis.set("decease", decease);
        diagnosis.set("details", details);
        diagnosis.saveIt();
    }

    public static List<Map<String, Object>> getDiagnoses(int patientID) {
        List<Appointment> appointments = Appointment.where("patientID = ?", patientID);
        if (!appointments.isEmpty()) {
            List<Integer> appointmentIDs = new ArrayList<>();
            for (Appointment appointment : appointments) {
                appointmentIDs.add(appointment.getInteger("appointmentID"));
            }

            String idList = appointmentIDs.stream()
                    .map(String::valueOf)
                    .collect(java.util.stream.Collectors.joining(", "));

            List<Map<String, Object>> diagnoses = Diagnosis.where("appointmentID IN (" + idList + ")").toMaps();
            return diagnoses;
        } else {
            // If no appointments found for the patient, return an empty list
            return new ArrayList<>();
        }
    }

    public static Diagnosis viewDiagnosis(int appointmentID) {
        Diagnosis diagnosis = Diagnosis.findFirst("appointmentID = ?", appointmentID);
        if (diagnosis == null) {
            return null; // No diagnosis found for this appointment
        } else {
            return diagnosis; // Return the first diagnosis found
        }
    }

    public static List<Diagnosis> getAppointmentDiagnoses(int appointmentID) {
        List<Diagnosis> diagnoses = Diagnosis.where("appointmentID = ?", appointmentID);
        return diagnoses != null ? diagnoses : new ArrayList<>();
    }
}
