package com.softwareengineering.services;

import java.util.List;
import java.util.Map;

import com.softwareengineering.models.User;

public class UserService {
    public static List<Map<String, Object>> getUsers() {
        return User.findAll().toMaps();
    }
}
