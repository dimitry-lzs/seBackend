package com.softwareengineering.services;

import java.util.List;
import java.util.Map;

import com.softwareengineering.models.Availability;
import java.sql.Timestamp;

public class AvailabilitiesService {
    public static List<Map<String, Object>> getDoctorAvailabilities(int DoctorID, String dateNow) {
        List<Map<String, Object>> availabilities = Availability
                .where("doctorID = ? AND timeFrom >= ?", DoctorID, dateNow).toMaps();
        return availabilities;
        //na deixnei mono ta free slots
    }

    public static void setAvailability(Timestamp slot, int doctorID) {
        Availability availability = new Availability();
        availability.set("timeFrom", slot.toString());
        availability.set("doctorID", doctorID);
        availability.saveIt();
    }

    public static void updateAvailability(int availabilityID, boolean isFree) {
        int rowsUpdated = Availability.update("free = ?", "availabilityID = ?", isFree, availabilityID);
        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("Slot with ID " + availabilityID + " not found.");
        }
    }
}
