package com.softwareengineering.services;

import java.util.List;
import java.util.Map;

import com.softwareengineering.dto.RegisterBody;
import com.softwareengineering.models.Doctor;
import com.softwareengineering.models.Patient;
import com.softwareengineering.models.User;
import com.softwareengineering.models.enums.UserTypeEnum;

public class UserService {
    public static List<Map<String, Object>> getUsers() {
        return User.findAll().toMaps();
    }

    public static boolean registerUser(RegisterBody body) {
        User user = new User(body.fullName, body.email, body.password, body.phone, body.userType);
        User existingUser = User.findFirst("email = ?", body.email);
        if (existingUser != null) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        if (UserTypeEnum.PATIENT.equals(body.userType)) {
            Patient patient = new Patient(user);
            patient.setAmka(body.amka);
            return patient.saveIt();
        } else if (UserTypeEnum.DOCTOR.equals(body.userType)) {
            Doctor doctor = new Doctor(user);
            doctor.setLicenceID(body.licenceID);
            doctor.setSpeciality(body.speciality);
            doctor.setOfficeLocation(body.officeLocation.toString());
            doctor.setBio(body.bio);
            return doctor.saveIt();
        }
        return false;
    }

    public static User loginUser(String email, String password) {
        User user = User.findFirst("email = ?", email);

        if (user == null) {
            throw new IllegalArgumentException("Invalid email");
        }

        if (!password.equals(user.getString("password"))) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }

    public static boolean updateAvatar(int id, String avatar) {
        User user = User.findFirst("id = ?", id);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (avatar == null || avatar.isEmpty()) {
            throw new IllegalArgumentException("Avatar is required");
        }
        user.set("avatar", avatar);
        return user.saveIt();
    }

    public static boolean updateUser(int id, RegisterBody body) {
        User user = User.findFirst("id = ?", id);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Update common fields
        if (body.fullName != null && !body.fullName.isEmpty()) {
            user.set("fullName", body.fullName);
        }
        if (body.email != null && !body.email.isEmpty()) {
            User existingUser = User.findFirst("email = ?", body.email);
            if (existingUser != null && !user.get("id").equals(existingUser.get("id"))) {
                throw new IllegalArgumentException("Email already exists");
            }
            user.set("email", body.email);
        }
        if (body.phone != null && !body.phone.isEmpty()) {
            user.set("phone", body.phone);
        }

        // Use appropriate wrapper class based on user type
        UserTypeEnum userType = user.getType();
        if (UserTypeEnum.PATIENT.equals(userType)) {
            Patient patient = new Patient(user);
            if (body.amka != null && !body.amka.isEmpty()) {
                patient.setAmka(body.amka);
            }
            return patient.saveIt();
        } else if (UserTypeEnum.DOCTOR.equals(userType)) {
            Doctor doctor = new Doctor(user);
            if (body.licenceID != null && !body.licenceID.isEmpty()) {
                doctor.setLicenceID(body.licenceID);
            }
            if (body.speciality != null) {
                doctor.setSpeciality(body.speciality);
            }
            if (body.officeLocation != null) {
                doctor.setOfficeLocation(body.officeLocation.toString());
            }
            if (body.bio != null && !body.bio.isEmpty()) {
                doctor.setBio(body.bio);
            }
            return doctor.saveIt();
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
    }

    public static String getAvatar(int id) {
        User user = User.findFirst("id = ?", id);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user.getString("avatar");
    }
}
