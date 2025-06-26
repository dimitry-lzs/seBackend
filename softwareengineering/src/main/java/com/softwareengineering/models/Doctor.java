package com.softwareengineering.models;

import com.softwareengineering.models.enums.Speciality;
import com.softwareengineering.models.enums.UserTypeEnum;
import com.softwareengineering.models.interfaces.DoctorInterface;

public class Doctor extends UserWrapper implements DoctorInterface {

    // Default constructor
    public Doctor() {
        super(new User());
        set("userType", UserTypeEnum.DOCTOR.toString());
    }

    public Doctor(String fullName, String email, String password, String phone, String licenceID,
            Speciality speciality,
            String bio, String officeLocation) {
        this();
        set("licenceID", licenceID);
        set("speciality", speciality);
        set("bio", bio);
        set("officeLocation", officeLocation);
    }

    public Doctor(User user) {
        super(user);
        if (!UserTypeEnum.DOCTOR.equals(user.getType())) {
            throw new IllegalArgumentException("User is not a doctor");
        }
    }

    public static Doctor findByEmail(String email) {
        User user = User.findFirst("userType = ? AND email = ?", UserTypeEnum.DOCTOR.toString(), email);
        if (user == null) {
            return null;
        }
        if (!UserTypeEnum.DOCTOR.equals(user.getType())) {
            throw new IllegalArgumentException("User is not a doctor");
        }
        return new Doctor(user);
    }

    public String getLicenceID() {
        return (String) get("licenceID");
    }

    public void setLicenceID(String licenceID) {
        set("licenceID", licenceID);
    }

    public Speciality getSpeciality() {
        return (Speciality) get("speciality");
    }

    public void setSpeciality(Speciality speciality) {
        set("speciality", speciality);
    }

    public String getBio() {
        return (String) get("bio");
    }

    public void setBio(String bio) {
        set("bio", bio);
    }

    public String getOfficeLocation() {
        return (String) get("officeLocation");
    }

    public void setOfficeLocation(String officeLocation) {
        set("officeLocation", officeLocation);
    }
}
