package com.softwareengineering.services;

import java.util.List;
import java.util.Map;
import com.softwareengineering.models.User;

public class PatientsService {
    public static List<Map<String, Object>> getPatients() {
        return User.where("userType = ?", "PATIENT").toMaps();
    }

    public static User getPatientById(int patientId) {
        return User.findFirst("userType = ? AND id = ?", "PATIENT", patientId);
    }
}
