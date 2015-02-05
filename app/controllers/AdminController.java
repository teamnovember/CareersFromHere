package controllers;

import models.User;
import models.Video;
import play.mvc.*;
import views.html.admin.*;

import java.util.ArrayList;
import java.util.List;

public class AdminController extends Controller {
    public static Result index() {
        User u = new User("Edgaras Liberis","blahblah","el398@cam.ac.uk");
        return ok(dashboard.render(u));
    }
}
