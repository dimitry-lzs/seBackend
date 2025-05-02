package com.softwareengineering.models;
import com.softwareengineering.models.enums.Speciality;
import com.softwareengineering.models.enums.UserType.UserTypeEnum;

public class Doctor extends User {

    private String licenceID;
    private Speciality speciality;
    private String bio;
    private String officeLocation;
    private float rating;

    public Doctor(String fullName, String email, String password, String phone, String licenceID, Speciality speciality, String bio, String officeLocation, float rating) {
        super(fullName, email, password, phone, UserTypeEnum.DOCTOR);
        this.set("licenceID", licenceID);
        this.set("speciality", speciality);
        this.set("bio", bio);
        this.set("officeLocation", officeLocation);
        this.set("rating", rating);
    }

    public void setLicenceID(String licenceID) {
        this.set("licenceID", licenceID);
    }

    public String getLicenceID() {
        return licenceID;
    }

    public void setSpeciality(Speciality speciality) {
        this.set("speciality", speciality);
    }

    public Speciality getSpeciality() {
        return speciality;
    }   

    public void setBio(String bio) {
        this.set("bio", bio);
    }

    public String getBio() {
        return bio;
    }

    public void setOfficeLocation(String officeLocation) {
        this.set("officeLocation", officeLocation);
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setRating(float rating) {
        this.set("rating", rating);
    }

    public float getRating() {
        return rating;
    }

}
