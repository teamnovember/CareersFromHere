package controllers;

import models.*;
import play.libs.mailer.*;
import play.Play;

import play.mvc.*;

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
        mail.setSubject("Approve your Email for CareersFromHere");
        mail.setFrom("Careers From Here FROM <careersfromhere@gmail.com>");
        mail.addTo("TO <"+email+">");
        mail.setBodyText("");//todo, have a message with the links to approve email or send a request to delete information
        String id = MailerPlugin.send(mail);
        return ok("Email "+id+" sent!");

    }

    public void emailVerification(User user){
        //todo, from verification link



    }

    public void bulkRegister(){
        //todo, do this method
    }

    public void login(String email, String password){
        //todo, logs in
    }

    public void delete(User user){
        //todo,deletes a user (in their school)
    }

    public void edit(User prevDetails, User newDetails){
        //todo,to edit user information - invoked by admins or users
    }

    public void changePassword(User user, String password){
        //todo, change password

        user.setPassword(password);
    }

}
