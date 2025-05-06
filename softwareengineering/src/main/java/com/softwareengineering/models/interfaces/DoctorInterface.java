package com.softwareengineering.models.interfaces;

import com.softwareengineering.models.enums.Speciality;

public interface DoctorInterface {
    String getLicenceID();

    void setLicenceID(String licenceID);

    Speciality getSpeciality();

    void setSpeciality(Speciality speciality);

    String getBio();

    void setBio(String bio);

    String getOfficeLocation();

    void setOfficeLocation(String officeLocation);
}
