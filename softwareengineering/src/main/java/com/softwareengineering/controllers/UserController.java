package com.softwareengineering.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.softwareengineering.services.UserService;
import com.softwareengineering.dto.LoginBody;
import com.softwareengineering.dto.RegisterBody;
import com.softwareengineering.models.User;

import io.javalin.Context;
import io.javalin.Javalin;

public class UserController {
    public static void init(Javalin app) {
        app.get("/users", UserController::getUsers);
        app.post("/register", UserController::registerUser);
        app.post("/login", UserController::loginUser);
    }

    private static void registerUser(Context context) {
        RegisterBody body = context.bodyAsClass(RegisterBody.class);
        if (UserService.registerUser(body)) {
            context.status(201).json(Map.of("message", "User registered successfully"));
        } else {
            context.status(400).json(Map.of("message", "User registration failed"));
        }
    }

    private static void loginUser(Context context) {
        LoginBody body = context.bodyAsClass(LoginBody.class);
        if (body.email == null || body.password == null) {
            context.status(400).json(Map.of("message", "Email and password are required"));
            return;
        }
        User loggedInUser = UserService.loginUser(body.email, body.password);
        context.sessionAttribute("userType", loggedInUser.get("userType"));
        context.sessionAttribute("userEmail", loggedInUser.get("email"));

        Map<String, Object> responseMap = new HashMap<>();
        addIfNotNull(responseMap, "userType", loggedInUser.get("userType"));
        addIfNotNull(responseMap, "email", loggedInUser.get("email"));
        addIfNotNull(responseMap, "fullName", loggedInUser.get("fullName"));
        addIfNotNull(responseMap, "phone", loggedInUser.get("phone"));
        addIfNotNull(responseMap, "amka", loggedInUser.get("amka"));
        addIfNotNull(responseMap, "licenceID", loggedInUser.get("licenceID"));
        addIfNotNull(responseMap, "speciality", loggedInUser.get("speciality"));

        context.status(200).json(responseMap);
    }

    private static void getUsers(Context context) {
        List<Map<String, Object>> users = UserService.getUsers();
        context.json(users);
    }
    private static void addIfNotNull(Map<String, Object> map, String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
    }
}
