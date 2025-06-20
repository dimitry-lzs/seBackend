package com.softwareengineering.dto;

import com.softwareengineering.models.Diagnosis;

public class DiagnosisBody {
    public Integer appointmentID;
    public String decease;
    public String details;

    public DiagnosisBody() {
    }

    public DiagnosisBody(Diagnosis diagnosis) {
        this.appointmentID = diagnosis.getInteger("appointmentID");
        this.decease = diagnosis.getString("decease");
        this.details = diagnosis.getString("details");
    }
}