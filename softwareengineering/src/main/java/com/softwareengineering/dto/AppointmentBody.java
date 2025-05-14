package com.softwareengineering.dto;

import com.softwareengineering.models.enums.Status;

public class AppointmentBody {
    public Integer appointmentID;
    public Integer doctorID;
    public Integer patientID;
    public Status status;
    public Integer slotID;
    public String reason;
}
