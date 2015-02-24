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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Security.Authenticated(AdminSecured.class)
public class AdminController extends Controller {
    //TODO: will need to authenticate the current user in these methods (replace new user creation)

    public static Result index() {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        return ok(dashboard.render(user));
    }

    public static Result users() {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        School s = user.getSchool();
        return ok(users.render(user, udao.getSchoolUsers(s))); //gets all users for a school
    }

    public static Result videos() {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        VideoDAO dao = new VideoDAO();
        return ok(videos.render(user, dao.getAllVideos())); //returns all videos
    }

    public static Result questions() {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        School s = user.getSchool();
        QuestionDAO dao = new QuestionDAO();
        /*dao.newQuestion(new Question("Question 1", 120.0, s));
        dao.newQuestion(new Question("Question 2", 120.0, s));
        dao.newQuestion(new Question("This question is not so long.", 120.0, s));
        dao.newQuestion(new Question("This is a much much longer question that may actually span across several lines in the question view in the admin panel.", 120.0, s));
        */
        List<Question> qs = dao.getActiveQuestions(s); //this gets all the active questions for a school
        return ok(questions.render(user, qs));
    }

    @Security.Authenticated(SuperAdminSecured.class)
    public static Result schools() {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        School s = user.getSchool();
        SchoolDAO dao = new SchoolDAO();
        List<School> ss = dao.getAllSchool();
        return ok(schools.render(user, ss));
    }

    public static Result getNewUser() {
        return getUser(null);
    }

    public static Result getUser(Long id) {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        School s = user.getSchool();
        UserForm data;
        if (id == null) {
            data = new UserForm(user.getSchool()); // Suggest same school as Admin by default
        } else {
            UserDAOImpl dao = new UserDAOImpl();
            User u = dao.getUser(id);
            data = new UserForm(u.getName(),"",u.getEmail(),u.getSchool(),u.getDiscriminator());
        }
        Form<UserForm> formdata = Form.form(UserForm.class).fill(data);

        Map<String, Boolean> schoolMap = AdminHelpers.ConstructSchoolMap(data.school.getName());
        Map<String, Boolean> discrMap = AdminHelpers.ConstructDiscriminatorMap(data.discriminator, user.getDiscriminator());

        boolean auth = false;
        if (user.getDiscriminator().equals("superadmin")) {
            auth = true;
        }
        return ok(edit_user.render(user, formdata, id, schoolMap, discrMap,auth));
    }

    public static Result postNewUser() {
        return postUser(null);
    }

    public static Result postUser(Long id) {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        Form<UserForm> data = Form.form(UserForm.class).bindFromRequest();

        boolean auth = false;
        if (user.getDiscriminator().equals("superadmin")) {
            auth = true;
        }

        String userSchoolName = data.data().get("school");
        if(userSchoolName == null) userSchoolName = "";  // "Please provide value" is "" too
        if(userSchoolName.equals("")) userSchoolName = user.getSchool().getName();
        Map<String, Boolean> schoolMap = AdminHelpers.ConstructSchoolMap(userSchoolName);
        String temp = data.data().get("discriminator");
        if(temp == null){
            temp = "student";
        }
        Map<String, Boolean> discrMap =
                AdminHelpers.ConstructDiscriminatorMap(
                        temp,user.getDiscriminator());

        if (data.hasErrors()) {
            flash("error", "Please correct errors below.");
            return badRequest(edit_user.render(user, data, id, schoolMap, discrMap,auth));
        }
        else {
            UserForm formData = data.get();
            User formUser = null;
            if (id == null) { //aka if we're making a new user, actually make a new one
                switch(formData.discriminator) {
                    case "alumni":
                        formUser = Alumni.makeInstance(formData);
                        break;
                    case "admin":
                        formUser = Admin.makeInstance(formData);
                        break;
                    case "superadmin": //only superadmins can possibly select this option
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
                if (!formData.password.equals("")) {
                    formUser.setPassword(formData.password);
                }
                formUser.setSchool(sdao.byName(formData.school.getName()));
                formUser.update();
            }
        }
        return redirect("/admin/users");
    }

    public static Result deleteUser() {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();

        DynamicForm requestData = Form.form().bindFromRequest();
        Long id = Long.parseLong(requestData.get("id"));
        UserDAOImpl dao = new UserDAOImpl();
        dao.deleteUser(id);

        flash("User deleted!");

        return redirect("/admin/users");
    }

    public static Result approveUser() {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();

        DynamicForm requestData = Form.form().bindFromRequest();
        Long id = Long.parseLong(requestData.get("id"));

        UserDAOImpl dao = new UserDAOImpl();
        dao.approveUser(id);

        flash("success", "User approved!");

        return redirect("/admin/users");
    }

    //dont need "new" video methods in AdminController
    public static Result getVideo(Long id) {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();

        VideoDAO dao = new VideoDAO();
        Video video = dao.getVideo(id);
        VideoForm data = new VideoForm(video.getTitle(),video.getDescription(),video.getCategories());
        Form<VideoForm> formdata = Form.form(VideoForm.class).fill(data);

        Map<String, Boolean> catMap = AdminHelpers.ConstructCategoryMap(video.getCategories());
        return ok(edit_video.render(user, formdata, id, catMap));
    }

    public static Result postVideo(Long id) {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        Form<VideoForm> data = Form.form(VideoForm.class).bindFromRequest();
        VideoDAO dao = new VideoDAO();
        Video video = dao.getVideo(id);

        if (data.hasErrors()) {
            Map<String, Boolean> catMap = AdminHelpers.ConstructCategoryMap(video.getCategories());
            flash("error", "Please correct errors below.");
            return badRequest(edit_video.render(user, data, id, catMap));
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
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();

        DynamicForm requestData = Form.form().bindFromRequest();
        Long id = Long.parseLong(requestData.get("id"));
        VideoDAO dao = new VideoDAO();
        dao.deleteVideo(id);

        flash("success", "Video deleted!");

        return redirect("/admin/videos");
    }

    public static Result approveVideo() {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();

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
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        QuestionForm data;

        if (id == null) {
            data = new QuestionForm();
        }
        else {
            QuestionDAO dao = new QuestionDAO();
            Question q = dao.getQuestion(id);
            data = new QuestionForm(q.getText(),q.getDuration());
        }
        Form<QuestionForm> formdata = Form.form(QuestionForm.class).fill(data);
        return ok(edit_question.render(user, formdata, id));
    }

    public static Result reorderQuestions() {
        DynamicForm requestData = Form.form().bindFromRequest();
        String data = requestData.get("order");

        UserDAOImpl udao = new UserDAOImpl();
        Http.Request r = request();
        QuestionDAO dao = new QuestionDAO();

        User user = udao.getUserFromContext();
        School s = user.getSchool();

        List<Question> qs = dao.getActiveQuestions(s);
        String[] orderStr = data.split(",");
        int[] order = new int[qs.size()];
        try {
            // Ensure same length
            if(orderStr.length != qs.size())
                throw new Exception("Wrong reorder size submitted.");

            // Ensure every element is a valid integer
            int[] orderTmp = new int[qs.size()];
            for(int i = 0; i < order.length; ++i) {
                order[i] =  orderTmp[i] = Integer.parseInt(orderStr[i]);
            }

            // Ensure it's a valid permutation from 1 to qs.size()
            // By sorting newOrderTemp
            Arrays.sort(orderTmp);
            for(int i = 0; i < orderTmp.length; ++i) {
                if(orderTmp[i] != i+1)
                    throw new Exception("Invalid ordering provided.");
            }
        } catch(Exception e) {
            flash("error", e.getMessage());
            return redirect("/admin/questions");
        }

        // Apply reordering
        for(int i = 0; i < qs.size(); ++i) {
            int oldOrder = order[i];
            int newOrder = i+1;
            Question q = qs.get(oldOrder-1);
            q.setOrder(newOrder);
            q.update();
        }
        return redirect("/admin/questions");
    }

    public static Result postNewQuestion() {return postQuestion(null);}

    public static Result postQuestion(Long id) {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        Form<QuestionForm> data = Form.form(QuestionForm.class).bindFromRequest();

        if (data.hasErrors()) {
            flash("error", "Please correct errors below.");
            return badRequest(edit_question.render(user, data, id));
        }
        else {
            QuestionForm formData = data.get();
            Question q = null;
            QuestionDAO dao = new QuestionDAO();
            if (id == null) { //if we are creating a new question
                q = Question.makeInstance(formData, user.getSchool());
                dao.newQuestion(q); //this ensures we get the ordering correct
            } else {
                q = dao.getQuestion(id);
                q.setText(formData.text);
                q.setDuration(formData.duration);
                //TODO: edit the question form to allow for reordering. or to reorder normally...
                q.update();
            }
            return redirect("/admin/questions");
        }
    }

    public static Result deleteQuestion() {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();

        DynamicForm requestData = Form.form().bindFromRequest();
        Long id = Long.parseLong(requestData.get("id"));
        QuestionDAO dao = new QuestionDAO();
        dao.deleteQuestion(id);

        return redirect("/admin/questions");
    }

    @Security.Authenticated(SuperAdminSecured.class)
    public static Result getNewSchool() { return getSchool(null);}

    @Security.Authenticated(SuperAdminSecured.class)
    public static Result getSchool(Long id) {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
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
        return ok(edit_school.render(user, formdata, id));
    }

    @Security.Authenticated(SuperAdminSecured.class)
    public static Result postNewSchool() {return postSchool(null);}

    @Security.Authenticated(SuperAdminSecured.class)
    public static Result postSchool(Long id) {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        Form<SchoolForm> data = Form.form(SchoolForm.class).bindFromRequest();

        if (data.hasErrors()) {
            flash("error", "Please correct errors below.");
            return badRequest(edit_school.render(user, data, id));
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
            return redirect("/admin/schools");
        }
    }

    public static Result deleteSchool() {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();

        DynamicForm requestData = Form.form().bindFromRequest();
        Long id = Long.parseLong(requestData.get("id"));
        SchoolDAO dao = new SchoolDAO();
        dao.deleteSchool(id);

        return redirect("/admin/schools");
    }

}
