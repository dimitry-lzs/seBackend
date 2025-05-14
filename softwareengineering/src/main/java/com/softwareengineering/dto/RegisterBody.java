package com.softwareengineering.dto;

import com.softwareengineering.models.enums.Speciality;
import com.softwareengineering.models.enums.UserTypeEnum;
import com.softwareengineering.models.enums.City;

public class RegisterBody {
    public UserTypeEnum userType;
    public String fullName;
    public String email;
    public String password;
    public String phone;
    public String amka;
    public String licenceID;
    public Speciality speciality;
    public City officeLocation;
    public String bio;
}
