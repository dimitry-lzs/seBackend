package com.softwareengineering.services;

import java.util.List;
import java.util.Map;
import com.softwareengineering.models.Rating;

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
}