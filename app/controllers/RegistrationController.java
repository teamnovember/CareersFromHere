package controllers;

import models.*;

/**
 * Created by biko on 09/02/15.
 */
public class RegistrationController {

    public void invite(String email){
        //todo, invoked by Admin to invite users via email
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
