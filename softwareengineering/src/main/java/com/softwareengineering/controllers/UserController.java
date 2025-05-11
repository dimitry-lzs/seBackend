package com.softwareengineering.controllers;

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

        context.status(200).json(Map.of(
                "message", "User logged in successfully",
                "userType", loggedInUser.get("userType"),
                "userId", loggedInUser.getId(),
                "userEmail", loggedInUser.get("email"),
                "fullName", loggedInUser.get("fullName"),
                "phone", loggedInUser.get("phone"),
                "amka", loggedInUser.get("amka"),
                "licenceID", loggedInUser.get("licenceID"),
                "speciality", loggedInUser.get("speciality")));
    }

    private static void getUsers(Context context) {
        List<Map<String, Object>> users = UserService.getUsers();
        context.json(users);
    }
}
