package com.softwareengineering.models;

import java.security.Timestamp;
import java.sql.Time;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("availabilities")
public class Availability extends Model {
    private Timestamp date;
    private Time timeFrom;
    private Time timeTo;
    private int doctorID;

    public Availability(Timestamp date, Time timeFrom, Time timeTo, int doctorID) {
        this.set("timeTo", timeTo);
        this.set("timeFrom", timeFrom);
        this.set("date", date);
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
        this.set("timeTo", timeTo);
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.set("doctorID", doctorID);
    }

}
