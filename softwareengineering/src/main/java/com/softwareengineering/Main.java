
package com.softwareengineering;
import java.util.Map;

import com.softwareengineering.controllers.AppointmentsController;
import com.softwareengineering.controllers.AvailabilitiesController;
import com.softwareengineering.controllers.DiagnosesController;
import com.softwareengineering.controllers.RatingsController;
import com.softwareengineering.controllers.UserController;
import com.softwareengineering.controllers.PatientsController;
import com.softwareengineering.controllers.DoctorsController;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create();
        app.start(7070);
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
        DiagnosesController.init(app);
        DoctorsController.init(app);
        PatientsController.init(app);

        app.get("/", ctx -> ctx.result("Software Engineering Backend"));

        app.get("/health", ctx -> {
            // Simple health check endpoint
            ctx.status(200).json(Map.of("status", "UP"));
        });
    }
}
