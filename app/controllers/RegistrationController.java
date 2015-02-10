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

    public void register(String email,String password, School school){
        //todo, invoked by users from registration page - sends a verification email to user email

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
    }

}
