
package com.softwareengineering;
import java.util.Map;

import com.softwareengineering.controllers.UserController;
import com.softwareengineering.models.User;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.start(7070);
        app.before(ctx -> Db.connect());
        app.after(ctx -> Db.close());

        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.json(Map.of("error", e.getMessage()));
        });

        app.exception(Exception.class, (e, ctx) -> {
            ctx.status(500);
            ctx.json(Map.of("error", e.getMessage()));
        });

        UserController.init(app);
        app.get("/", ctx -> ctx.result("Software Engineering Backend"));
        app.get("/test", ctx -> ctx.json(User.findAll().toMaps()));
    }
}
