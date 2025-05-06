package com.softwareengineering.models;

import java.sql.Timestamp;
import java.sql.Time;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
    @org.javalite.activejdbc.annotations.BelongsTo(parent = User.class, foreignKeyName = "doctorID"),
    @org.javalite.activejdbc.annotations.BelongsTo(parent = User.class, foreignKeyName = "patientID")
})
@Table("availabilities")
public class Availability extends Model {
    public Availability(Timestamp date, Time timeFrom, Time timeTo, int doctorID) {
        this.set("timeTo", timeTo);
        this.set("timeFrom", timeFrom);
        this.set("date", date);
        this.set("doctorID", doctorID);
    }

    public Availability(){}

    public void setDate(Timestamp date) {
        this.set("date", date);
    }

    public Timestamp getDate() {
        return (Timestamp) this.get("date");
    }

    public Time getTimeFrom() {
        return (Time) this.get("timeFrom");
    }

    public void setTimeFrom(Time timeFrom) {
        this.set("timeFrom", timeFrom);
    }

    public void setTimeTo(Time timeTo) {
        this.set("timeTo", timeTo);
    }

    public Time getTimeTo() {
        return (Time) this.get("timeTo");
    }

    public void setDoctorID(int doctorID) {
        this.set("doctorID", doctorID);
    }

    public int getDoctorID() {
        return (int) this.get("doctorID");
    }
}
