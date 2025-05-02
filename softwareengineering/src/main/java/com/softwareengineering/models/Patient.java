package com.softwareengineering.models;

import com.softwareengineering.models.enums.UserType.UserTypeEnum;

public class Patient extends User{

    private String amka;

    public Patient(String fullName, String email, String password, String phone, String amka) {
        super(fullName, email, password, phone, UserTypeEnum.PATIENT);
        this.set("amka", amka);
    }

    public void setAmka(String amka) {
        this.set("amka", amka);
    }

    public String getAmka() {
        return amka;
    }
    
}
