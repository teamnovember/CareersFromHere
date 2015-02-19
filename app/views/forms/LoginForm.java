package views.forms;

import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

public class LoginForm {
    public String login; // Email
    public String password; // Password

    public LoginForm() {}
    public LoginForm(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        if (login == null || login.length() == 0) {
            errors.add(new ValidationError("login", "No login was given"));
        }
        if (password == null || password.length() == 0) {
            errors.add(new ValidationError("password", "No password was given"));
        }

        /*if(!User.authenticate(login, password)) {
            errors.add(new ValidationError("", "Invalid user email or password"));
        }*/

        if (errors.size() > 0) {
            return errors;
        }

        return null;
    }
}
