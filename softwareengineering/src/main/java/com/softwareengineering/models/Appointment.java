package com.softwareengineering.models;

import java.sql.Time;
import java.sql.Timestamp;
import com.softwareengineering.models.enums.Status;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;


@Table("appointments")
public class Appointment extends Model {
    public Appointment(Timestamp date, Time timeFrom, Time timeTo, Status status, int patientID, int doctorID) {
        this.set("date", date);
        this.set("timeFrom", timeFrom);
        this.set("timeTo", timeTo);
        this.set("status", status);
        this.set("patientID", patientID);
        this.set("doctorID", doctorID);
    }

    public Timestamp getDate() {
        return (Timestamp) this.get("date");
    }

    public Time getTimeFrom() {
        return (Time) this.get("timeFrom");
    }
    public Time getTimeTo() {
        return (Time) this.get("timeTo");
    }
    public Status getStatus() {
        return (Status) this.get("status");
    }
    public int getPatientID() {
        return (int) this.get("patientID");
    }
    public int getDoctorID() {
        return (int) this.get("doctorID");
    }
    public void setDate(Timestamp date) {
        this.set("date", date);
    }
    public void setTimeFrom(Time timeFrom) {
        this.set("timeFrom", timeFrom);
    }
    public void setTimeTo(Time timeTo) {
        this.set("timeTo", timeTo);
    }
    public void setStatus(Status status) {
        this.set("status", status);
    }
    public void setPatientID(int patientID) {
        this.set("patientID", patientID);
    }
    public void setDoctorID(int doctorID) {
        this.set("doctorID", doctorID);
    }
}
