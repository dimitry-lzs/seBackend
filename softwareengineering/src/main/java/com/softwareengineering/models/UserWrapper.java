package com.softwareengineering.models;

import com.softwareengineering.models.enums.UserTypeEnum;

public class UserWrapper {
    protected final User user;

    protected UserWrapper(User user) {
        this.user = user;
    }

    public void setFullName(String fullName) {
        user.set("fullName", fullName);
    }

    public String getFullName() {
        return user.get("fullName").toString();
    }

    public void setEmail(String email) {
        user.set("email", email);
    }

    public String getEmail() {
        return user.get("email").toString();
    }

    public void setPassword(String password) {
        user.set("password", password);
    }

    public String getPassword() {
        return user.get("password").toString();
    }

    public void setPhone(String phone) {
        user.set("phone", phone);
    }

    public String getPhone() {
        return user.get("phone").toString();
    }

    public UserTypeEnum getType() {
        return user.getType();
    }

    public boolean saveIt() {
        if (this.getType() == null) {
            throw new IllegalArgumentException("User type is required");
        }
        if (this.getType() == UserTypeEnum.DOCTOR) {
            if (user.get("speciality") == null) {
                throw new IllegalArgumentException("Speciality is required for doctors");
            }
            if (user.get("licenceID") == null) {
                throw new IllegalArgumentException("Licence ID is required for doctors");
            }
        } else if (this.getType() == UserTypeEnum.PATIENT) {
            if (user.get("amka") == null) {
                throw new IllegalArgumentException("AMKA is required for patients");
            }
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
        return user.saveIt();
    }

    public int getId() {
        return (int) user.get("id");
    }

    protected void set(String key, Object value) {
        user.set(key, value);
    }

    protected Object get(String key) {
        return user.get(key);
    }
}