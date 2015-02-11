package controllers;

import models.*;
import play.data.Form;
import play.mvc.*;
import views.forms.QuestionForm;
import views.forms.SchoolForm;
import views.forms.UserForm;
import views.forms.VideoForm;
import views.html.admin.*;

import java.util.ArrayList;
import java.util.List;

public class AdminController extends Controller {
    //TODO: will need to authenticate the current user in these methods (replace new user creation)

    //TODO: just realised makeInstance methods are creating new models rather than updating ones in the db. Need to change this

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

    public static Result schools() {
        School s = new School("Super High School");
        SuperAdmin u = new SuperAdmin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s); //TODO: restrict school functions to super admin
        return ok(schools.render(u));
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
        return ok(edit_user.render(u, formdata, id));
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
            return badRequest(edit_user.render(u, data, id));
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

            // Redirect to users page?
            return ok(edit_user.render(u, data, id));
        }
    }

    public static Result deleteUser(Long id) {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);

        UserDAOImpl dao = new UserDAOImpl();
        dao.deleteUser(id);

        return ok(users.render(u,dao.getSchoolUsers(s)));
    }

    //dont need "new" video methods in AdminController
    public static Result getVideo(Long id) {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        VideoForm data;
        VideoDAO dao = new VideoDAO();
        Video video = dao.getVideo(id);
        data = new VideoForm(video.getTitle(),video.getDescription(),video.getCategories());
        Form<VideoForm> formdata = Form.form(VideoForm.class).fill(data);
        return ok(edit_video.render(u,formdata,id));
    }

    public static Result postVideo(Long id) {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        Form<VideoForm> data = Form.form(VideoForm.class).bindFromRequest();
        VideoDAO dao = new VideoDAO();
        Video video = dao.getVideo(id);

        if (data.hasErrors()) {
            flash("error", "Please correct errors above.");
            return badRequest(edit_video.render(u, data, id));
        }
        else {
            VideoForm formData = data.get();
            video.setTitle(formData.title);
            video.setDescription(formData.description);
            for (Category c : formData.categories) {
                if (!video.getCategories().contains(c)) {
                    video.addCategory(c);
                }
            }
            return ok(edit_video.render(u,data,id));
        }
    }

    public static Result deleteVideo(Long id) {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);

        VideoDAO dao = new VideoDAO();
        dao.deleteVideo(id);

        return ok(videos.render(u));
    }

    public static Result approveVideo(Long id) {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);

        VideoDAO dao = new VideoDAO();
        Video v = dao.getVideo(id);
        v.approve(true);

        return ok(videos.render(u));
    }

    //TODO: need a method/way for admins to preview video before they decide to approve or delete

    public static Result getNewQuestion() { return getQuestion(null);}

    public static Result getQuestion(Long id) {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        QuestionForm data;

        if (id == null) {
            data = new QuestionForm();
        }
        else {
            QuestionDAO dao = new QuestionDAO();
            Question q = dao.getQuestion(id);
            data = new QuestionForm(q.getText(),q.getDuration(),q.getSchool());
        }
        Form<QuestionForm> formdata = Form.form(QuestionForm.class).fill(data);
        return ok(edit_question.render(u, formdata));
    }

    public static Result postNewQuestion() {return postQuestion(null);}

    public static Result postQuestion(Long id) {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        Form<QuestionForm> data = Form.form(QuestionForm.class).bindFromRequest();

        if (data.hasErrors()) {
            flash("error", "Please correct errors above.");
            return badRequest(edit_question.render(u,data));
        }
        else {
            QuestionForm formData = data.get();
            Question q = Question.makeInstance(formData);
            return ok(edit_question.render(u,data));
        }
    }

    public static Result deleteQuestion(Long id) {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);

        QuestionDAO dao = new QuestionDAO();
        dao.deleteQuestion(id);

        return ok(questions.render(u));
    }

    public static Result getNewSchool() { return getSchool(null);}

    public static Result getSchool(Long id) {
        School s = new School("Super High School");
        SuperAdmin u = new SuperAdmin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        SchoolForm data;

        if (id == null) {
            data = new SchoolForm();
        }
        else {
            SchoolDAO dao = new SchoolDAO();
            School sch = dao.getSchool(id);
            data = new SchoolForm(sch.getName());
        }
        Form<SchoolForm> formdata = Form.form(SchoolForm.class).fill(data);
        return ok(edit_school.render(u, formdata));
    }

    public static Result postNewSchool() {return postSchool(null);}

    public static Result postSchool(Long id) {
        School s = new School("Super High School");
        SuperAdmin u = new SuperAdmin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        Form<SchoolForm> data = Form.form(SchoolForm.class).bindFromRequest();

        if (data.hasErrors()) {
            flash("error", "Please correct errors above.");
            return badRequest(edit_school.render(u,data));
        }
        else {
            SchoolForm formData = data.get();
            School q = School.makeInstance(formData);
            return ok(edit_school.render(u,data));
        }
    }

    public static Result deleteSchool(Long id) {
        School s = new School("Super High School");
        SuperAdmin u = new SuperAdmin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);

        SchoolDAO dao = new SchoolDAO();
        dao.deleteSchool(id);

        return ok(schools.render(u));
    }

}
