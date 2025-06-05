package com.softwareengineering.controllers;

import io.javalin.Javalin;
import io.javalin.Context;

import com.softwareengineering.dto.RatingBody;
import com.softwareengineering.services.RatingsService;
import java.util.List;
import java.util.Map;

public class RatingsController {
    public static void init(Javalin app) {
        app.get("/doctor-ratings", RatingsController::getRatings); // endpoint for a patient to get ratings of a doctor
        app.get("/get-my-ratings", RatingsController::getMyRatings); // endpoint for a doctor to get their own ratings
        app.post("/set-rating", RatingsController::setRating);
    }


    private static void getRatings(Context context) {
        int doctorID = Integer.parseInt(context.queryParam("doctorID"));
        List<Map<String, Object>> ratings = RatingsService.getRatings(doctorID);
        context.json(ratings);
    }

    private static void getMyRatings(Context context) {
        int doctorID = context.sessionAttribute("id");
        List<Map<String, Object>> ratings = RatingsService.getRatings(doctorID);
        context.json(ratings);
    }

    public static void setRating(Context context) {
        RatingBody body = context.bodyAsClass(RatingBody.class);
        int patientID = context.sessionAttribute("id");
        if (body.stars < 1 || body.stars > 5) {
            context.status(400).json(Map.of("error", "Stars must be between 1 and 5"));
            return;
        }
        if (body.doctorID == null || patientID == 0) {
            context.status(400).json(Map.of("error", "Doctor ID and Patient ID cannot be null"));
            return;
        }
        RatingsService.setRating(body.stars, body.comments, patientID, body.doctorID);
        RatingsService.calcRating(body.doctorID);
        context.status(201);
    }
}
