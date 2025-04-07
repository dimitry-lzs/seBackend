package com.softwareengineering.controllers;

import java.util.List;
import java.util.Map;

import com.softwareengineering.services.UserService;

import io.javalin.Context;
import io.javalin.Javalin;

public class UserController {
    public static void init(Javalin app) {
        app.get("/users", UserController::getUsers);
    }

    private static void getUsers(Context context) {
        List<Map<String, Object>> users = UserService.getUsers();
        context.json(users);
    }
}
