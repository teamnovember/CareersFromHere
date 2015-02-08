package controllers;

import models.*;
import play.data.Form;
import play.mvc.*;
import views.forms.UserForm;
import views.html.admin.*;

public class AdminController extends Controller {
    //TODO: will need to authenticate the current user in these methods (replace new user creation)
    public static Result index() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        return ok(dashboard.render(u));
    }

    public static Result users() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        return ok(users.render(u,null));
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

    public static Result getUser(Long id) {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        UserForm data;
        if (id == 0) {
            data = new UserForm();
        } else {
            UserDAOImpl dao = new UserDAOImpl();
            User user = dao.getUser(id);
            if (user instanceof Student) {
                data = new UserForm(user.getName(),user.getPassword(),user.getEmail(),user.getSchool(),1);
            } else if (user instanceof Alumni) {
                data = new UserForm(user.getName(),user.getPassword(),user.getEmail(),user.getSchool(),2);
            } else if (user instanceof Admin) {
                data = new UserForm(user.getName(),user.getPassword(),user.getEmail(),user.getSchool(),3); //TODO: restrict
            } else {
                data = new UserForm(user.getName(),user.getPassword(),user.getEmail(),user.getSchool(),4); //TODO: restrict
            }
        }
        Form<UserForm> formdata = Form.form(UserForm.class).fill(data);
        return ok(users.render(u,formdata));
    }

    public static Result postUser() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        Form<UserForm> data = Form.form(UserForm.class).bindFromRequest();

        if (data.hasErrors()) {
            flash("error", "Please correct errors above.");
            return badRequest(users.render(u,data));
        }
        else {
            UserForm formdata = data.get();
            if (formdata.type == 1) {
                Student student = Student.makeInstance(formdata);
                flash("success", "Student instance created/edited: " + student);
            } else if (formdata.type == 2) {
                Alumni alumni = Alumni.makeInstance(formdata);
                flash("success","Alumni instance created/edited: " + alumni);
            } else if (formdata.type == 3) {
                Admin admin = Admin.makeInstance(formdata); //TODO: add validation so only SuperAdmin can do this one
                flash("success","Admin instance created/edited: " + admin);
            } else {
                SuperAdmin sadmin = SuperAdmin.makeInstance(formdata);
                flash("success","Admin instance created/edited: " + sadmin); //TODO: add validation so only SuperAdmin can do this one
            }
            return ok(users.render(u,data));
        }
    }
}
