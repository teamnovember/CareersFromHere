package controllers;

import helpers.AdminHelpers;
import models.*;
import org.apache.commons.lang3.RandomStringUtils;
import play.data.*;
import static play.data.Form.*;
import play.libs.mailer.*;
import play.Play;
import views.forms.AlumniRegForm;
import views.forms.LoginForm;
import views.forms.UserForm;
import views.html.edit_self;
import views.html.emails.*;
import views.html.login;
import play.mvc.*;
import views.html.reg_alumni;

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
            User u = User.authenticate(lf.login, lf.password);
            if(u == null) {
                flash("error", "Incorrect email and/or password.");
                return badRequest(login.render(loginForm));
            } else if (u.getApproved() == false) {
                flash("error", "Your account has not been approved yet.");
                return badRequest(login.render(loginForm));
            } else {
                    session().clear();
                    //todo do we want to be more secure than just storing the email in the session?
                    session("email", lf.login);
                    return redirect("/");
            }
        }
    }

    public static Result getAlumniRegForm() {
        AlumniRegForm data = new AlumniRegForm();
        Form<AlumniRegForm> formdata = Form.form(AlumniRegForm.class).fill(data);

        Map<String, Boolean> schoolMap = AdminHelpers.ConstructSchoolMap(null,false);
        return ok(reg_alumni.render(formdata, schoolMap));
    }

    public static Result resetPassword(User user){
        String newpw = RandomStringUtils.randomAlphanumeric(8);
        user.setPassword(newpw);

        //now for the emailing
        Email mail = new Email();
        mail.setSubject("CareersFromHere: Your password has been reset!");
        mail.setFrom("Careers From Here FROM <careersfromhere@gmail.com");
        mail.addTo("To <" + user.getEmail() +">");
        mail.setBodyText("You're password has been reset. You're new password is: "+newpw); //or something along those lines
        String id = MailerPlugin.send(mail);
        return ok("Email "+ id + " sent!");
    }

    public static Result postAlumniRegForm() {
        Form<AlumniRegForm> data = Form.form(AlumniRegForm.class).bindFromRequest();

        String userSchoolName = data.data().get("school");
        if(userSchoolName == null) userSchoolName = "";  // "Please provide value" is "" too
        Map<String, Boolean> schoolMap = AdminHelpers.ConstructSchoolMap(userSchoolName,false);

        if (data.hasErrors()) {
            flash("error", "Please correct errors below.");
            return badRequest(reg_alumni.render(data, schoolMap));
        }

        AlumniRegForm formData = data.get();
        Alumni formUser = Alumni.makeInstance(formData);
        formUser.save();

        newRegistrationEmail(formUser);
        flash("success", "Registered. Check your inbox!");
        return redirect("/");
    }

    //TODO: might be a better place to put this but idc. also need to add stuff for alumni profile editing etc
    public static Result getUserDetails() {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        School s = user.getSchool();
        UserForm data = new UserForm(user.getName(),"",user.getEmail(),user.getSchool(),user.getDiscriminator(),user.getAlumniProfile(),user);
        Form<UserForm> formdata = Form.form(UserForm.class).fill(data);

        boolean auth = false;
        if (user.getDiscriminator().equals("superadmin")) {
            auth = true;
        }

        Map<String, Boolean> schoolMap = AdminHelpers.ConstructSchoolMap(data.school.getName(),auth);

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
        Map<String, Boolean> schoolMap = AdminHelpers.ConstructSchoolMap(userSchoolName,auth);

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
