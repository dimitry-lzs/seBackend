package com.softwareengineering.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.softwareengineering.models.Appointment;
import com.softwareengineering.models.Diagnosis;

public class DiagnosesService {

    public static void setDiagnosis(int appointmentID, String decease, String details) {

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
}
