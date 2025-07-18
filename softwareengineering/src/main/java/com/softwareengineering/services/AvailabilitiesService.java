package com.softwareengineering.services;

import java.util.List;
import java.util.Map;

import com.softwareengineering.models.Availability;
import java.sql.Timestamp;

public class AvailabilitiesService {
    public static List<Map<String, Object>> getDoctorAvailabilities(int DoctorID) {
        List<Map<String, Object>> availabilities = Availability
                .where("doctorID = ?", DoctorID)
                .orderBy("timeFrom ASC")
                .toMaps();
        return availabilities;
    }

    public static List<Map<String, Object>> getDoctorAvailabilities(int DoctorID, boolean onlyFree) {
        if (onlyFree) {
            List<Map<String, Object>> availabilities = Availability
                    .where("doctorID = ? AND free = ?", DoctorID, true)
                    .orderBy("timeFrom ASC")
                    .toMaps();
            return availabilities;
        } else {
            // Show all slots
            return getDoctorAvailabilities(DoctorID);
        }
    }

    public static void setAvailability(Timestamp slot, int doctorID) {
        // Check if availability already exists for this doctor at this time slot
        Availability existingAvailability = Availability.findFirst("timeFrom = ? AND doctorID = ?", slot.toString(), doctorID);

        if (existingAvailability != null) {
            throw new IllegalArgumentException("Availability slot already exists for this doctor at " + slot.toString());
        }

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
    public static void deleteAvailability(int availabilityID, int doctorID) {
        Availability availability = Availability.findFirst("availabilityID = ?", availabilityID);
        if (availability == null) {
            throw new IllegalArgumentException("Slot with ID " + availabilityID + " not found.");
        }
        if (availability.getDoctorID() != doctorID) {
            throw new IllegalArgumentException("You do not have permission to delete this slot.");
        }
        availability.delete();
    }
}
