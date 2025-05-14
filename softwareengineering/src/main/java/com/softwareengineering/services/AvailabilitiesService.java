package com.softwareengineering.services;

import java.util.List;
import java.util.Map;
import com.softwareengineering.models.Availability;
import java.sql.Timestamp;

public class AvailabilitiesService {
    public static List<Map<String, Object>> getDoctorAvailabilities (int DoctorID) {
        List<Map<String, Object>> availabilities = Availability.where("doctorID = ?", DoctorID).toMaps();
        return availabilities;
    }

    public static void setAvailability (Timestamp slot, int doctorID) {
        Availability availability = new Availability();
        availability.set("timeFrom", slot.toString());
        availability.set("doctorID", doctorID);
        availability.saveIt();
    }
}
