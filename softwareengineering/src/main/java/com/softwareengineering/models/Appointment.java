package com.softwareengineering.models;

import java.security.Timestamp;
import java.sql.Time;
import com.softwareengineering.models.enums.Status;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("appointments")
public class Appointment extends Model {
    
    private Timestamp date;
    private Time timeFrom;
    private Time timeTo;
    private Status status;
    private int patientID;
    private int doctorID;

    public Appointment(Timestamp date, Time timeFrom, Time timeTo, Status status, int patientID, int doctorID) {
        this.set("date", date);
        this.set("timeFrom", timeFrom);
        this.set("timeTo", timeTo);
        this.set("status", status);
        this.set("patientID", patientID);
        this.set("doctorID", doctorID);
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.set("date", date);
    }

    public Time getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Time timeFrom) {
        this.set("timeFrom", timeFrom);
    }

    public Time getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Time timeTo) {
        this.set("TimeTo", timeTo);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.set("status", status);
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.set("patientID", patientID);
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.set("doctorID", doctorID);
    }

}
