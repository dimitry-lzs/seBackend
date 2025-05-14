package com.softwareengineering.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.softwareengineering.services.UserService;
import com.softwareengineering.dto.AvatarBody;
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
        app.get("/login", UserController::getLogin);
        app.get("/logout", UserController::logoutUser);
        app.post("/update-avatar", UserController::updateAvatar);
        app.get("/avatar", UserController::getAvatar);
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
        context.sessionAttribute("id", loggedInUser.get("id"));
        context.status(200).json(getUserData(loggedInUser));
    }

    private static void getLogin(Context context) {
        String email = context.sessionAttribute("userEmail");
        int id = context.sessionAttribute("id");
        String userType = context.sessionAttribute("userType");

        if (email == null || userType == null || id == 0) {
            context.status(401).json(Map.of("message", "Unauthorized"));
            return;
        }

        User loggedInUser = User.findFirst("id = ?", id);
        context.status(200).json(getUserData(loggedInUser));
    }

    private static void updateAvatar(Context context) {
        int id = context.sessionAttribute("id");
        if (id == 0) {
            context.status(401).json(Map.of("message", "Unauthorized"));
            return;
        }
        boolean updated = UserService.updateAvatar(id, context.bodyAsClass(AvatarBody.class).avatar);
        if (!updated) {
            context.status(400).json(Map.of("message", "Failed to update avatar"));
            return;
        }
        context.status(200).json(Map.of("message", "Avatar updated successfully"));
    }

    private static void getAvatar(Context context) {
        int id = context.sessionAttribute("id");
        if (id == 0) {
            context.status(401).json(Map.of("message", "Unauthorized"));
            return;
        }

        String avatar = UserService.getAvatar(id);

        if (avatar == null) {
            avatar = "";
        }
        context.status(200).json(Map.of("avatar", avatar));
    }

    private static void logoutUser(Context context) {
        context.sessionAttribute("userType", "");
        context.sessionAttribute("id", 0);
        context.sessionAttribute("userEmail", "");
        context.status(200).json(Map.of("message", "Logged out successfully"));
    }

    private static Map<String, Object> getUserData(User user) {
        Map<String, Object> userData = new HashMap<>();
        addIfNotNull(userData, "userType", user.get("userType"));
        addIfNotNull(userData, "email", user.get("email"));
        addIfNotNull(userData, "fullName", user.get("fullName"));
        addIfNotNull(userData, "phone", user.get("phone"));
        addIfNotNull(userData, "amka", user.get("amka"));
        addIfNotNull(userData, "licenceID", user.get("licenceID"));
        addIfNotNull(userData, "speciality", user.get("speciality"));

        return userData;
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
