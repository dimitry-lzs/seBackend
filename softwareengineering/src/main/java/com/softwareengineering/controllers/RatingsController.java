package com.softwareengineering.controllers;

import io.javalin.Javalin;
import io.javalin.Context;
import com.softwareengineering.services.RatingsService;
import java.util.List;
import java.util.Map;

public class RatingsController {
    public static void init(Javalin app) {
        app.get("/doctor-ratings", RatingsController::getRatings);
    }


    private static void getRatings(Context context) {
        System.out.println("HELLOOOOOOOOOOOOOOOOOO");
        int doctorID = Integer.parseInt(context.queryParam("doctorID"));
        List<Map<String, Object>> ratings = RatingsService.getRatings(doctorID);
        context.json(ratings);
    }
}
