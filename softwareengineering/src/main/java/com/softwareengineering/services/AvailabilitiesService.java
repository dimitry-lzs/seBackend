package com.softwareengineering.services;

import java.util.List;
import java.util.Map;
import com.softwareengineering.models.Availability;

public class AvailabilitiesService {
    public static List<Map<String, Object>> getDoctorAvailabilities (int DoctorID) {
        List<Map<String, Object>> availabilities = Availability.where("doctorID = ?", DoctorID).toMaps();
        return availabilities;
    }
}
