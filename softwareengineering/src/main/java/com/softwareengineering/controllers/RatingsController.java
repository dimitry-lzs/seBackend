package com.softwareengineering.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.softwareengineering.dto.RatingBody;
import com.softwareengineering.models.Rating;
import com.softwareengineering.models.enums.UserTypeEnum;
import com.softwareengineering.services.RatingsService;
import com.softwareengineering.utils.AuthUtils;
import com.softwareengineering.utils.AuthUtils.UnauthorizedException;

import java.util.List;
import java.util.Map;

public class RatingsController {
    public static void init(Javalin app) {
        app.get("/ratings", RatingsController::getRatings);
        app.get("/ratings/{appointmentID}", RatingsController::getAppointmentRating);
        app.put("/ratings/{appointmentID}", RatingsController::updateRating);
        app.post("/set-rating", RatingsController::setRating);
    }

    private static void getRatings(Context context) {
        String doctorID = context.queryParam("doctorID");
        try {
            int doctorId = (doctorID == null || doctorID.isEmpty())
                    ? AuthUtils.validateDoctorAndGetId(context)
                    : getDoctorIdFromPatientRequest(context, doctorID);

            List<Map<String, Object>> ratings = RatingsService.getRatings(doctorId);
            context.json(ratings);
        } catch (UnauthorizedException exception) {
            AuthUtils.handleUnauthorized(context, exception);
        }
    }

    private static int getDoctorIdFromPatientRequest(Context context, String doctorID) throws UnauthorizedException {
        AuthUtils.validateUserAndGetId(context, UserTypeEnum.PATIENT);
        return Integer.parseInt(doctorID);
    }

    public static void setRating(Context context) {
        RatingBody body = context.bodyAsClass(RatingBody.class);
        try {
            int patientID = AuthUtils.validateUserAndGetId(context, UserTypeEnum.PATIENT);
            if (body.stars < 0 || body.stars > 5) {
                context.status(400).json(Map.of("error", "Stars must be between 1 and 5"));
                return;
            }
            Rating newRating = RatingsService.setRating(body.appointmentID, body.stars, body.comments, patientID);
            RatingsService.calcRating(newRating.getDoctorId());
            context.status(201);
        } catch (UnauthorizedException exception) {
            AuthUtils.handleUnauthorized(context, exception);
        } catch (NumberFormatException e) {
            context.status(400).json(Map.of("error", "Invalid doctor ID format"));
        } catch (Exception e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new IllegalStateException("Patient has already rated this appointment", e);
            }
            context.status(500).json(Map.of("error", "An error occurred while setting the rating"));
        }
    }

    public static void updateRating(Context context) {
        int appointmentID = Integer.parseInt(context.pathParam("appointmentID"));
        RatingBody body = context.bodyAsClass(RatingBody.class);
        try {
            int userId = AuthUtils.validatePatientAndGetId(context);

            if (body.stars < 0 || body.stars > 5) {
                context.status(400).json(Map.of("error", "Stars must be between 1 and 5"));
                return;
            }

            Rating updatedRating = RatingsService.updateRating(appointmentID, body.stars, body.comments, userId);
            RatingsService.calcRating(updatedRating.getDoctorId());
            context.json(updatedRating);
        } catch (UnauthorizedException exception) {
            AuthUtils.handleUnauthorized(context, exception);
        } catch (NumberFormatException e) {
            context.status(400).json(Map.of("error", "Invalid appointment ID format"));
        } catch (Exception e) {
            context.status(500).json(Map.of("error", "An error occurred while updating the rating"));
        }
    }

    public static void getAppointmentRating(Context context) {
        int appointmentID = Integer.parseInt(context.pathParam("appointmentID"));
        try {
            UserTypeEnum userType = AuthUtils.getUserTypeFromSession(context);
            int userId = AuthUtils.validateUserAndGetId(context, userType);

            Rating appointmentRating = RatingsService.getRatingByAppointmentID(appointmentID, userId, userType);

            if (appointmentRating == null) {
                context.json(Map.of("message", "No rating found for this appointment"));
                return;
            }

            context.json(appointmentRating);
        } catch (UnauthorizedException exception) {
            AuthUtils.handleUnauthorized(context, exception);
        }
    }
}
