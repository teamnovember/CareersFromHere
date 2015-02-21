package views.forms;

import models.School;
import models.SchoolDAO;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louise on 08/02/2015.
 */
public class UserForm {
    public String name = "";
    public String password = "";
    public String email = "";
    public School school = null;
    /**
        Discriminator string for mapping form data to class it belongs to:
        "student" for Student,
        "alumni" for Alumni,
        "admin" for Admin,
        "superadmin" for SuperAdmin
    */
    public String discriminator = "student";

    public UserForm() {}

    public UserForm(School school) {
        this.school = school;
    }

    public UserForm(String name, String password, String email, School school, String discriminator) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.school = school;
        this.discriminator = discriminator;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();

        if (name == null || name.length() == 0) {
            errors.add(new ValidationError("name", "No name was given"));
        }

        if (password == null || password.length() == 0) {
            errors.add(new ValidationError("password", "No password was given"));
        } //TODO: add more password checks

        if (email == null || email.length() == 0) {
            errors.add(new ValidationError("email","No email was given"));
        } else if (!email.contains("@")) {
            errors.add(new ValidationError("email","Invalid email was given"));
            //TODO: do better email validation
        }

        if (school == null) {
            errors.add(new ValidationError("school","No school was given"));
        }

        if (discriminator == null || discriminator.equals("")) {
            errors.add(new ValidationError("discriminator", "No user type was given"));
        } else if (!discriminator.equals("student")
                && !discriminator.equals("alumni")
                && !discriminator.equals("admin")
                && !discriminator.equals("superadmin")) {
            errors.add(new ValidationError("discriminator", "Invalid user type given"));
        }

        SchoolDAO sdao = new SchoolDAO();
        if(sdao.byName(school.getName()) == null) {
            errors.add(new ValidationError("school", "Invalid school provided"));
        }

        if (errors.size() > 0) {
            return errors;
        }

        return null;
    }
}
