package com.softwareengineering.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("ratings")
public class Rating extends Model {
    public Rating(int appointmentId, int stars, String comments, int doctorId, int patientId) {
        set("appointmentId", appointmentId);
        set("stars", stars);
        set("comments", comments);
        set("doctorId", doctorId);
        set("patientId", patientId);
    }

    public Rating() {
    }

    public int getStars() {
        return (int) get("stars");
    }

    public void setStars(int stars) {
        set("stars", stars);
    }

    public String getComments() {
        return (String) get("comments");
    }

    public void setComments(String comments) {
        set("comments", comments);
    }

    public int getDoctorId() {
        return (int) get("doctorId");
    }

    public void setDoctorId(int doctorId) {
        set("doctorId", doctorId);
    }

    public int getPatientId() {
        return (int) get("patientId");
    }

    public void setPatientId(int patientId) {
        set("patientId", patientId);
    }
}
