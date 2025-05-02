package com.softwareengineering.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

import com.softwareengineering.models.enums.UserType.UserTypeEnum;

@Table("users")
public class User extends Model {
    static {
        validatePresenceOf("email");
        validateEmailOf("email").message("Invalid email");
    }
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private UserTypeEnum userType;
    private int id;


    public User(String fullName, String email, String password, String phone, UserTypeEnum userType) {
        this.set("fullName", fullName);
        this.set("email", email);
        this.set("password", password);
        this.set("phone", phone);
        this.set("userType", userType);
    }
}
