package controllers;

import helpers.AdminHelpers;
import models.*;
import play.data.Form;
import play.data.DynamicForm;
import play.mvc.*;
import views.forms.QuestionForm;
import views.forms.SchoolForm;
import views.forms.UserForm;
import views.forms.VideoForm;
import views.html.admin.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminController extends Controller {
    //TODO: will need to authenticate the current user in these methods (replace new user creation)

    public static Result index() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        return ok(dashboard.render(u));
    }

    public static Result users() {
        School s = School.find.all().get(0);
        Admin u = new Admin("Edgaras Liberis", "blahblah", "el398@cam.ac.uk", s);
        UserDAOImpl dao = new UserDAOImpl();
        return ok(users.render(u, dao.getSchoolUsers(s))); //gets all users for a school
    }

    public static Result videos() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        VideoDAO dao = new VideoDAO();
        return ok(videos.render(u, dao.getAllVideos())); //returns all videos
    }

    public static Result questions() {
        School s = new School("Super High School");
        s.save();
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        QuestionDAO dao = new QuestionDAO();
        List<Question> qs = dao.getActiveQuestions(s); //this gets all the active questions for a school
        return ok(questions.render(u));
    }

    public static Result schools() {
        School s = new School("Super High School");
        SuperAdmin u = new SuperAdmin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s); //TODO: restrict school functions to super admin
        SchoolDAO dao = new SchoolDAO();
        List<School> ss = dao.getAllSchool(); //list of all current schools
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
            data = new UserForm(u.getSchool()); // Suggest same school as Admin by default
        } else {
            UserDAOImpl dao = new UserDAOImpl();
            User user = dao.getUser(id);
            data = new UserForm(user.getName(),user.getPassword(),user.getEmail(),user.getSchool(),user.getDiscriminator());
        }
        Form<UserForm> formdata = Form.form(UserForm.class).fill(data);

        Map<String, Boolean> schoolMap = AdminHelpers.ConstructSchoolMap(data.school.getName());
        Map<String, Boolean> discrMap = AdminHelpers.ConstructDiscriminatorMap(data.discriminator);

        return ok(edit_user.render(u, formdata, id, schoolMap, discrMap));
    }

    public static Result postNewUser() {
        return postUser(null);
    }

    public static Result postUser(Long id) {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        Form<UserForm> data = Form.form(UserForm.class).bindFromRequest();

        String userSchoolName = data.data().getOrDefault("school", ""); // "Please provide value" is "" too
        if(userSchoolName.equals("")) userSchoolName = u.getSchool().getName();
        Map<String, Boolean> schoolMap = AdminHelpers.ConstructSchoolMap(userSchoolName);
        Map<String, Boolean> discrMap =
                AdminHelpers.ConstructDiscriminatorMap(
                        data.data().getOrDefault("discriminator", "student"));

        if (data.hasErrors()) {
            flash("error", "Please correct errors above.");
            return badRequest(edit_user.render(u, data, id, schoolMap, discrMap));
        }
        else {
            UserForm formData = data.get();
            User formUser = null;
            if (id == null) { //aka if we're making a new user, actually make a new one
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
                formUser.save();
            }
            else { //if we have an id (aka we're editing) we want to edit the details of the user in the database already
                UserDAOImpl dao = new UserDAOImpl();
                SchoolDAO sdao = new SchoolDAO();
                formUser = dao.getUser(id);
                formUser.setName(formData.name);
                formUser.setEmail(formData.email);
                formUser.setPassword(formData.password);
                formUser.setSchool(sdao.byName(formData.school.getName())); //do we need this? might want to restrict to Super Admin...

                //formUser.setDiscriminator(formData.discriminator); //upgrading users, again might either not want to restrict...
                formUser.update();
            }
        }
        return redirect("/admin/users");
    }

    public static Result deleteUser() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);

        DynamicForm requestData = Form.form().bindFromRequest();
        Long id = Long.parseLong(requestData.get("id"));
        UserDAOImpl dao = new UserDAOImpl();
        dao.deleteUser(id);

        flash("User deleted!");

        return redirect("/admin/users");
    }

    public static Result approveUser() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);

        DynamicForm requestData = Form.form().bindFromRequest();
        Long id = Long.parseLong(requestData.get("id"));

        UserDAOImpl dao = new UserDAOImpl();
        dao.approveUser(id);

        flash("success", "User approved!");

        return redirect("/admin/users");
    }

    //dont need "new" video methods in AdminController
    public static Result getVideo(Long id) {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);

        VideoDAO dao = new VideoDAO();
        Video video = dao.getVideo(id);
        VideoForm data = new VideoForm(video.getTitle(),video.getDescription(),video.getCategories());
        Form<VideoForm> formdata = Form.form(VideoForm.class).fill(data);

        Map<String, Boolean> catMap = AdminHelpers.ConstructCategoryMap(video.getCategories());
        return ok(edit_video.render(u, formdata, id, catMap));
    }

    public static Result postVideo(Long id) {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);
        Form<VideoForm> data = Form.form(VideoForm.class).bindFromRequest();
        VideoDAO dao = new VideoDAO();
        Video video = dao.getVideo(id);

        if (data.hasErrors()) {
            Map<String, Boolean> catMap = AdminHelpers.ConstructCategoryMap(video.getCategories());
            flash("error", "Please correct errors above.");
            return badRequest(edit_video.render(u, data, id, catMap));
        }
        else { //don't need to check for null id because we don't create videos here
            CategoryDAO cdao = new CategoryDAO(); // Has fully initialised Category objects

            VideoForm formData = data.get();
            video.setTitle(formData.title);
            video.setDescription(formData.description);
            video.categories.clear();

            for (Category c : cdao.getAllCategories()) {
                if (AdminHelpers.CategoryContains(formData.categories, c)) {
                    video.addCategory(c);
                }
            }
            video.update();
            return redirect("/admin/videos");
        }
    }

    public static Result deleteVideo() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);

        DynamicForm requestData = Form.form().bindFromRequest();
        Long id = Long.parseLong(requestData.get("id"));
        VideoDAO dao = new VideoDAO();
        dao.deleteVideo(id);

        flash("success", "Video deleted!");

        return redirect("/admin/videos");
    }

    public static Result approveVideo() {
        School s = new School("Super High School");
        Admin u = new Admin("Edgaras Liberis","blahblah","el398@cam.ac.uk",s);

        DynamicForm requestData = Form.form().bindFromRequest();
        Long id = Long.parseLong(requestData.get("id"));
        VideoDAO dao = new VideoDAO();
        Video v = dao.getVideo(id);
        v.approve(true);

        flash("success", "Video approved!");

        return redirect("/admin/videos");
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
            flash("error", "Please correct errors below.");
            return badRequest(edit_question.render(u,data));
        }
        else {
            QuestionForm formData = data.get();
            Question q = null;
            QuestionDAO dao = new QuestionDAO();
            if (id == null) { //if we are creating a new question
                q = Question.makeInstance(formData);
                dao.newQuestion(q); //this ensures we get the ordering correct
            } else {
                q = dao.getQuestion(id);
                q.setText(formData.text);
                q.setDuration(formData.duration);
                //TODO: edit the question form to allow for reordering. or to reorder normally...
                q.update();
            }
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
            flash("error", "Please correct errors below.");
            return badRequest(edit_school.render(u,data));
        }
        else {
            SchoolForm formData = data.get();
            School sch;
            if (id == null) {
                sch = School.makeInstance(formData);
                sch.save();

            } else {
                SchoolDAO dao = new SchoolDAO();
                sch = dao.getSchool(id);
                sch.setName(formData.name);
                sch.update();
            }
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
