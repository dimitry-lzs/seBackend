package com.softwareengineering.dto;

import java.sql.Timestamp;

import com.softwareengineering.models.enums.Status;

public class AppointmentBody {
    public Integer appointmentID;
    public Integer doctorID;
    public Integer patientID;
    public Status status;
    public Timestamp date;
}
