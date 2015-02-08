package views.forms;

import models.School;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louise on 08/02/2015.
 */
public class UserForm {
    public String username = "";
    public String password = "";
    public String email = "";
    public School school = null;
    public int type = 0;

    public UserForm() {}

    public UserForm(String name, String password, String email, School school, int type) {
        this.username = name;
        this.password = password;
        this.email = email;
        this.school = school;
        this.type = type; //1 for Student, 2 for Alumni, 3 for Admin, 4 for SuperAdmin (3 & 4 only accessible to SuperAdmin)
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();

        if (username == null || username.length() == 0) {
            errors.add(new ValidationError("ussername","No name was given"));
        }

        if (password == null || password.length() == 0) {
            errors.add(new ValidationError("password","No password was given"));
        } //TODO: add more password checks

        if (email == null || email.length() == 0) {
            errors.add(new ValidationError("email","No email was given"));
        } else if (!(email.contains("@")) || !(email.contains("."))) {
            errors.add(new ValidationError("email","Invalid email was given"));
            //TODO: do better email validation
        }

        if (school == null) {
            errors.add(new ValidationError("school","No school was given"));
        }

        if (type ==0) {
            errors.add(new ValidationError("type","No user type was given"));
        } else if (type > 4) {
            errors.add(new ValidationError("type","Invalid user type given"));
        }

        if (errors.size() > 0) {
            return errors;
        }

        return null;
    }
}
