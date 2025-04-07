package com.softwareengineering.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("users")
public class User extends Model {
    static {
        validatePresenceOf("email");
        validateEmailOf("email").message("Invalid email");
    }
}
