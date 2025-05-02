package com.softwareengineering.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("ratings")
public class Rating extends Model {
    private int stars;
    private String comments;
    private int doctorId;
    private int patientId;

    public Rating(int stars, String comments, int doctorId, int patientId) {
        this.set("stars", stars);
        this.set("comments", comments);
        this.set("doctorId", doctorId);
        this.set("patientId", patientId);
    }

    public float getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.set("stars", stars);
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.set("comments", comments);
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.set("doctorId", doctorId);
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.set("patientId", patientId);
    }
}
