package com.softwareengineering.services;

import java.util.List;
import java.util.Map;

import com.softwareengineering.models.Appointment;
import com.softwareengineering.models.Rating;
import com.softwareengineering.models.User;
import com.softwareengineering.models.enums.UserTypeEnum;

public class RatingsService {
    public static List<Map<String, Object>> getRatings(int doctorID) {
        List<Map<String, Object>> ratings = Rating.where("doctorID = ?", doctorID).toMaps();
        return ratings;
    }

    public static Rating setRating(int appointmentID, int stars, String comments, int patientID) {
        Appointment appointment = Appointment.findFirst("appointmentID = ? AND patientID = ?", appointmentID,
                patientID);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found for the given patient ID");
        }
        int doctorID = appointment.getDoctorID();
        Rating rating = new Rating(appointmentID, stars, comments, doctorID, patientID);
        rating.saveIt();
        return rating;
    }

    public static Rating updateRating(int appointmentID, int stars, String comments, int userId) {
        int rowsUpdated = Rating.update(
                "stars = ?, comments = ?",
                "appointmentID = ?",
                stars, comments, appointmentID);

        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("Rating not found for the given appointment ID");
        }

        return Rating.findFirst("appointmentID = ?", appointmentID);
    }

    public static Rating getRatingByAppointmentID(int appointmentID, int userId, UserTypeEnum userType) {
        if (userType == UserTypeEnum.PATIENT) {
            return Rating.findFirst("appointmentID = ? AND patientID = ?", appointmentID, userId);
        } else if (userType == UserTypeEnum.DOCTOR) {
            return Rating.findFirst("appointmentID = ? AND doctorID = ?", appointmentID, userId);
        } else {
            throw new IllegalArgumentException("Invalid user type for rating retrieval");
        }
    }

    public static void calcRating(int doctorID) {
        List<Map<String, Object>> ratings = Rating.where("doctorID = ?", doctorID).toMaps();
        if (ratings.isEmpty()) {
            return; // No ratings available
        }
        double totalStars = 0;
        for (Map<String, Object> rating : ratings) {
            totalStars += (int) rating.get("stars");
        }
        double stars = totalStars / ratings.size();

        // Find the doctor in the users table and update their rating
        User doctor = User.findFirst("id = ?", doctorID);
        if (doctor != null) {
            doctor.set("rating", stars);
            doctor.saveIt();
        } else {
            System.out.println("Doctor with ID " + doctorID + " not found");
        }

    }
}