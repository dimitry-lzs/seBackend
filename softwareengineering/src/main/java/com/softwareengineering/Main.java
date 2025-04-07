
package com.softwareengineering;

import com.softwareengineering.controllers.UserController;
import com.softwareengineering.models.User;
import com.softwareengineering.services.UserService;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.start(7070);
        app.before(ctx -> Db.connect());
        app.after(ctx -> Db.close());
        UserController.init(app);
        app.get("/", ctx -> ctx.result("Software Engineering Backend"));
        app.get("/test", ctx -> ctx.json(User.findAll().toMaps()));
    }
}
