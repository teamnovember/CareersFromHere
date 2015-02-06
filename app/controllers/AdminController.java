package controllers;

import models.User;
import models.Admin;
import models.School;
import models.Video;
import play.mvc.*;
import views.html.admin.*;

import java.util.ArrayList;
import java.util.List;

public class AdminController extends Controller {
    //TODO: actually put some useful stuff here
    public static Result index() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        return ok(dashboard.render(u));
    }

    public static Result users() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        return ok(users.render(u));
    }

    public static Result videos() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        return ok(videos.render(u));
    }

    public static Result questions() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        return ok(questions.render(u));
    }
}
