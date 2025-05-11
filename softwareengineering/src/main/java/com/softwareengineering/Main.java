
package com.softwareengineering;
import java.util.Map;

import com.softwareengineering.controllers.AppointmentsController;
import com.softwareengineering.controllers.AvailabilitiesController;
import com.softwareengineering.controllers.RatingsController;
import com.softwareengineering.controllers.UserController;
import com.softwareengineering.services.UserService;
import com.softwareengineering.models.*;
import java.sql.Timestamp;

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
        AppointmentsController.init(app);
        RatingsController.init(app);
        AvailabilitiesController.init(app);

        app.get("/", ctx -> ctx.result("Software Engineering Backend"));
        app.get("/test", ctx -> ctx.json(UserService.getUsers()));
         app.get("/test2", ctx -> {
            Appointment appointment = new Appointment(null, null, null, null, 0, 0);
            appointment.set("date", new Timestamp(System.currentTimeMillis()));
            appointment.saveIt();
            ctx.json(Appointment.findAll().toMaps());
        });

    }
}
