package controllers;

import helpers.AdminHelpers;
import models.*;
import play.data.*;
import static play.data.Form.*;
import play.libs.mailer.*;
import play.Play;
import views.forms.LoginForm;
import views.forms.UserForm;
import views.html.edit_self;
import views.html.emails.*;
import views.html.login;
import play.mvc.*;

import java.util.List;
import java.util.Map;

public class RegistrationController extends Controller {

    public static void newRegistrationEmail(Alumni newUser) {
        // Send email to user
        Email mail = new Email();
        mail.setSubject("Welcome to Careers From Here");
        mail.setFrom("Careers From Here <careersfromhere@gmail.com>");
        mail.addTo(newUser.getName() + " <" + newUser.getEmail() + ">");
        mail.setBodyHtml(registration_welcome.render(newUser).toString());
        MailerPlugin.send(mail);

        // Inform all admins
        List<Admin> admins = newUser.getSchool().getAdmins();
        for (Admin x : admins){
            Email adminMail = new Email();
            adminMail.setSubject("Careers From Here: New User Registration");
            adminMail.setFrom("Careers From Here <careersfromhere@gmail.com>");
            adminMail.addTo(x.getName() + " <" + x.getEmail() + ">");
            adminMail.setBodyHtml(registration_notify.render(newUser).toString());//todo
            MailerPlugin.send(adminMail);
        }
    }

    public static void userApprovedEmail(Alumni newUser) {
        // Send email to user
        Email mail = new Email();
        mail.setSubject("Careers From Here: Account Approved");
        mail.setFrom("Careers From Here <careersfromhere@gmail.com>");
        mail.addTo(newUser.getName() + " <" + newUser.getEmail() + ">");
        mail.setBodyHtml(registration_approved.render(newUser).toString());
        MailerPlugin.send(mail);
    }

    public Result invite(String email,School school, String discriminator){
        //todo, invoked by Admin to invite users via email
        Email mail = new Email();
        mail.setSubject("Invitation to CareersFromHere");
        mail.setFrom("Careers From Here FROM <careersfromhere@gmail.com>");
        mail.addTo("TO <" + email + ">");
        mail.setBodyText(""); //TODO: create a nice looking invitation email and provide registration link depending on school + discriminator
        String id = MailerPlugin.send(mail);
        return ok("Email " + id + " sent!");
    }

    public static Result register() {
        School s = new School("New high school");
        Alumni a = new Alumni("Test Alumni", "zaqwsxcde", "edgaras.liberis+cfh@gmail.com", s);
        userApprovedEmail(a);
        return ok("SUCCESS");
    }


    public static Result login(){
        Form<LoginForm> form = form(LoginForm.class).fill(new LoginForm());
        return ok(login.render(form));
    }

    public static Result logout() {
        session().remove("email");
        session().clear();
        return redirect("/");
    }

    public static Result authenticate(){
        Form<LoginForm> loginForm = form(LoginForm.class).bindFromRequest();

        if (loginForm.hasErrors()){
            return badRequest(login.render(loginForm));
        } else {
            LoginForm lf = loginForm.get();
            if(User.authenticate(lf.login, lf.password) == null) {
                flash("error", "Incorrect email and/or password.");
                return badRequest(login.render(loginForm));
            } else {
                session().clear();
                //todo do we want to be more secure than just storing the email in the session?
                session("email", lf.login);
                return redirect("/");
            }
        }
    }

    public static void changePassword(User user, String password){
        user.setPassword(password);
    }

    //TODO: might be a better place to put this but idc. also need to add stuff for alumni profile editing etc
    public static Result getUserDetails() {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        School s = user.getSchool();
        UserForm data = new UserForm(user.getName(),"",user.getEmail(),user.getSchool(),user.getDiscriminator(),user.getAlumniProfile());
        Form<UserForm> formdata = Form.form(UserForm.class).fill(data);

        boolean auth = false;
        if (user.getDiscriminator().equals("superadmin")) {
            auth = true;
        }

        Map<String, Boolean> schoolMap = AdminHelpers.ConstructSchoolMap(data.school.getName());

        return ok(edit_self.render(user, formdata,schoolMap,auth));
    }

    public static Result postUserDetails() {
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

        if (data.hasErrors()) {
            flash("error", "Please correct errors below.");
            return badRequest(edit_self.render(user, data,schoolMap,auth));
        }
        else {
            UserForm formData = data.get();
            user.setName(formData.name);
            user.setEmail(formData.email);
            if (!formData.password.equals("")) {
                user.setPassword(formData.password);
            }
            if (user.getDiscriminator().equals("superadmin")) {
                SchoolDAO sdao = new SchoolDAO();
                user.setSchool(sdao.byName(formData.school.getName()));
            }
            user.setAlumniProfile(formData.profile);
            user.update();

        }
        return redirect("/");
    }
}
