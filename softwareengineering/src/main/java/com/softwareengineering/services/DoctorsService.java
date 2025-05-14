package com.softwareengineering.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.softwareengineering.models.User;

public class DoctorsService {
    public static List<Map<String, Object>> getDoctors() {
        return User.where("userType = ?", "DOCTOR").toMaps();
    }

    public static User getDoctorById(int doctorId) {
    return User.findFirst("userType = ? AND id = ?", "DOCTOR", doctorId);
    }

    public static List<String> getDoctorSpecialities() {
    return User.findBySQL("SELECT DISTINCT speciality FROM users WHERE userType = ? AND speciality IS NOT NULL", "DOCTOR")
        .stream()
        .map(u -> u.getString("speciality"))
        .collect(Collectors.toList());
    }

    public static List<String> getDoctorLocations() {
        return User.findBySQL("SELECT DISTINCT officeLocation FROM users WHERE userType = ? AND officeLocation IS NOT NULL", "DOCTOR")
            .stream()
            .map(u -> u.getString("officeLocation"))
            .collect(Collectors.toList());
    }

    public static List<Map<String, Object>> findDoctors(String speciality, String officeLocation) {
        String sql = "SELECT * FROM users WHERE userType = ? AND speciality = ? AND officeLocation = ?";
        return User.findBySQL(sql, "DOCTOR", speciality, officeLocation).toMaps();
    }
}
