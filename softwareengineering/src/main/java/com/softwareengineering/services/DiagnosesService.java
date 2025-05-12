package com.softwareengineering.services;

import com.softwareengineering.models.Diagnosis;

public class DiagnosesService {

    public static void setDiagnosis(int appointmentID, String decease, String details) {
        
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.set("appointmentID", appointmentID);
        diagnosis.set("decease", decease);
        diagnosis.set("details", details);
        diagnosis.saveIt();
    }
}
