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
        this.set("userType", userType.toString());
    }

    public UserTypeEnum getType() {
        String userTypeStr = (String) this.get("userType");
        if (userTypeStr == null) {
            return null;
        }
        return UserTypeEnum.valueOf(userTypeStr);
    }

    public Boolean getIsDark() {
        Object isDark = this.get("is_dark");
        if (isDark == null) {
            return false; // Default to false if not set
        }
        if (isDark instanceof Boolean) {
            return (Boolean) isDark;
        }
        if (isDark instanceof Integer) {
            return ((Integer) isDark) != 0; // SQLite stores boolean as INTEGER (0/1)
        }
        // Handle string values from database
        return Boolean.valueOf(isDark.toString());
    }

    public void setIsDark(Boolean isDark) {
        // Store as INTEGER for SQLite compatibility
        this.set("is_dark", isDark != null && isDark ? 1 : 0);
    }
}
