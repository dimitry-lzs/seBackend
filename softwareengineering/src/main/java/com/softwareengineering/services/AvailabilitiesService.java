package com.softwareengineering.services;

import java.util.List;
import java.util.Map;
import com.softwareengineering.models.Availability;

public class AvailabilitiesService {
    public static List<Map<String, Object>> getDoctorAvailabilities (int DoctorID) {
        List<Map<String, Object>> availabilities = Availability.where("doctorID = ?", DoctorID).toMaps();
        return availabilities;
    }

    public static void setAvailability (java.sql.Timestamp date, java.sql.Timestamp timeFrom, java.sql.Timestamp timeTo, int doctorID) {
        Availability availability = new Availability();
        availability.set("date", date);
        availability.set("timeFrom", timeFrom);
        availability.set("timeTo", timeTo);
        availability.set("doctorID", doctorID);
        availability.saveIt();
    }
}
