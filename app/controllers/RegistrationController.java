package controllers;

import models.*;
import play.data.*;
import static play.data.Form.*;
import play.libs.mailer.*;
import play.Play;
import views.forms.LoginForm;
import views.forms.UserForm;
import views.html.admin.edit_self;
import views.html.login;
import play.mvc.*;

import java.util.List;

/**
 * Created by biko on 09/02/15.
 */
public class RegistrationController extends Controller {

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

    public Result register(String name,String email,String password, School school,String discrim){
        if (discrim.equals("student")) {
            Student s = new Student(name, email, password, school);
            s.setPassword(password);
        }else if (discrim.equals("alumni")){
            Alumni a = new Alumni(name,password,email,school);
            a.setPassword(password);
        }else if (discrim.equals("admin")){
            Admin a = new Admin(name,password,email,school);
            a.setPassword(password);
        }else if (discrim.equals("superadmin")){
            SuperAdmin sa = new SuperAdmin(name,password,email,school);
            sa.setPassword(password);
        }

        Email mail = new Email();
        mail.setSubject("Welcome to CareersFromHere");
        mail.setFrom("Careers From Here FROM <careersfromhere@gmail.com>");
        mail.addTo("TO <"+email+">");
        mail.setBodyText("");//todo, nice looking welcome email
        String id = MailerPlugin.send(mail);


        List<Admin> admins = school.getAdmins();
        for (Admin x:admins){
            Email adminMail = new Email();
            adminMail.setSubject("CareersFromHere: New User has registered");
            adminMail.setFrom("Careers From Here FROM <careersfromhere@gmail.com>");
            adminMail.addTo("TO <"+x.getEmail()+">");
            adminMail.setBodyText("");//todo
            String id2 = MailerPlugin.send(adminMail);


        }
        return ok("Email "+id+" sent! and admins notified");
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
        UserForm data = new UserForm(user.getName(),"",user.getEmail(),user.getSchool(),user.getDiscriminator());
        Form<UserForm> formdata = Form.form(UserForm.class).fill(data);

        return ok(edit_self.render(user, formdata));
    }

    public static Result postUserDetails() {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        Form<UserForm> data = Form.form(UserForm.class).bindFromRequest();

        if (data.hasErrors()) {
            flash("error", "Please correct errors below.");
            return badRequest(edit_self.render(user, data));
        }
        else {
            UserForm formData = data.get();
            user.setName(formData.name);
            user.setEmail(formData.email);
            if (!formData.password.equals("")) {
                user.setPassword(formData.password);
            }
            user.update();

        }
        return redirect("/");
    }
}
