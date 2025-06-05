package com.softwareengineering.controllers;

import com.softwareengineering.models.User;
import java.util.List;
import java.util.Map;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.softwareengineering.services.DoctorsService;

public class DoctorsController {
    public static void init(Javalin app) {
        app.get("/doctors", DoctorsController::getDoctors);
        app.get("/doctors/{id}", DoctorsController::getDoctorByID);
        app.get("/get-doctor-specialities", DoctorsController::getDoctorSpecialities);
        app.get("/get-doctor-locations", DoctorsController::getDoctorLocations);
        app.get("/find-doctors", DoctorsController::findDoctors);
    }

    private static void getDoctors(Context context) {
        List<Map<String, Object>> doctors = DoctorsService.getDoctors();
        context.json(doctors);
    }

    private static void getDoctorByID(Context context) {
        int doctorId = Integer.parseInt(context.pathParam("id"));
        User doctorModel = DoctorsService.getDoctorById(doctorId);
        if (doctorModel != null) {
            context.json(doctorModel.toMap());
        } else {
            context.status(404).json(Map.of("error", "Doctor not found"));
        }
    }

    private static void getDoctorSpecialities(Context context) {
        List<String> specialities = DoctorsService.getDoctorSpecialities();
        context.json(specialities);
    }

    private static void getDoctorLocations(Context context) {
        List<String> locations = DoctorsService.getDoctorLocations();
        context.json(locations);
    }

    private static void findDoctors(Context context) {
        String speciality = context.queryParam("speciality");
        String location = context.queryParam("officeLocation");
        List<Map<String, Object>> doctors = DoctorsService.findDoctors(speciality, location);
        context.json(doctors);
    }
}
