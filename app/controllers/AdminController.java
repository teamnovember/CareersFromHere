package controllers;

import models.*;
import play.data.Form;
import play.mvc.*;
import views.forms.UserForm;
import views.html.admin.*;

import java.util.ArrayList;
import java.util.List;

public class AdminController extends Controller {
    //TODO: will need to authenticate the current user in these methods (replace new user creation)
    public static Result index() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        return ok(dashboard.render(u));
    }

    public static Result users() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis", "blahblah", "el398@cam.ac.uk", s);
        List<User> usrs = new ArrayList<>();
        usrs.add(new Student("Amazing Person 1", "muchpassword", "cl@cam.ac.uk", s));
        usrs.add(new Student("Amazing Person 2", "muchpassword", "cl@cam.ac.uk", s));
        usrs.add(new Student("Amazing Person 3", "muchpassword", "cl@cam.ac.uk", s));
        return ok(users.render(u, usrs));
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

    public static Result getNewUser() {
        return getUser(null);
    }

    public static Result getUser(Long id) {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        UserForm data;
        if (id == null) {
            data = new UserForm();
        } else {
            UserDAOImpl dao = new UserDAOImpl();
            User user = dao.getUser(id);
            data = new UserForm(user.getName(),user.getPassword(),user.getEmail(),user.getSchool(),user.getDiscriminator());
        }
        Form<UserForm> formdata = Form.form(UserForm.class).fill(data);
        return ok(edit_user.render(u, formdata));
    }

    public static Result postNewUser() {
        return postUser(null);
    }

    public static Result postUser(Long id) {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        Form<UserForm> data = Form.form(UserForm.class).bindFromRequest();

        if (data.hasErrors()) {
            flash("error", "Please correct errors above.");
            return badRequest(edit_user.render(u,data));
        }
        else {
            UserForm formData = data.get();
            User formUser = null;
            switch(formData.discriminator) {
                case "alumni":
                    formUser = Alumni.makeInstance(formData);
                    break;
                case "admin": //TODO: add validation so only (Super/)Admin can do this one
                    formUser = Admin.makeInstance(formData);
                    break;
                case "superadmin": //TODO: add validation so only SuperAdmin can do this one
                    formUser = SuperAdmin.makeInstance(formData);
                    break;
                default: //"student"
                    formUser = Student.makeInstance(formData);
                    break;

            }

            return ok(edit_user.render(u, data));
        }
    }
}
