package com.softwareengineering.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.HasMany;
import org.javalite.activejdbc.annotations.Table;

import com.softwareengineering.models.enums.UserTypeEnum;

@HasMany(foreignKeyName = "id", child = Availability.class)
@HasMany(foreignKeyName = "id", child = Appointment.class)
@Table("users")
public class User extends Model {
    static {
        validatePresenceOf("email");
        validatePresenceOf("userType");
        validateEmailOf("email").message("Invalid email");
        validatePresenceOf("password").message("Password is required");
    }

    public User() {
        super();
    }

    public User(String fullName, String email, String password, String phone, UserTypeEnum userType) {
        this.set("fullName", fullName);
        this.set("email", email);
        this.set("password", password);
        this.set("phone", phone);
        this.set("userType", userType);
    }

    public UserTypeEnum getType() {
        return (UserTypeEnum) this.get("userType");
    }
}
