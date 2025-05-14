package com.softwareengineering.models;

import com.softwareengineering.models.interfaces.PatientInterface;
import com.softwareengineering.models.enums.UserTypeEnum;

public class Patient extends UserWrapper implements PatientInterface {

    public Patient() {
        super(new User());
        set("userType", UserTypeEnum.PATIENT);
    }

    public Patient(String fullName, String email, String password, String phone) {
        this();
        set("fullName", fullName);
        set("email", email);
        set("password", password);
        set("phone", phone);
    }

    public Patient(User user) {
        super(user);
        if (!UserTypeEnum.PATIENT.equals(user.get("userType"))) {
            throw new IllegalArgumentException("User is not a patient");
        }
    }

    public void setAmka(String amka) {
        set("amka", amka);
    }

    public String getAmka() {
        return get("amka").toString();
    }
}
