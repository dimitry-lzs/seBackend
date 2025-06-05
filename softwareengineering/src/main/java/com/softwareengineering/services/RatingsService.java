package com.softwareengineering.services;

import java.util.List;
import java.util.Map;
import com.softwareengineering.models.Rating;
import com.softwareengineering.models.User;

public class RatingsService {
    public static List<Map<String, Object>> getRatings(int doctorID) {
        List<Map<String, Object>> ratings = Rating.where("doctorID = ?", doctorID).toMaps();
        return ratings;
    }

    public static void setRating(int stars, String comments, int patientID, int doctorID) {
        Rating rating = new Rating();
        rating.set("stars", stars);
        rating.set("comments", comments);
        rating.set("patientID", patientID);
        rating.set("doctorID", doctorID);
        rating.saveIt();
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