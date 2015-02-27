package views.forms;

import models.School;
import models.SchoolDAO;
import models.User;
import models.UserDAOImpl;
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
    public User user = null;
    /**
        Discriminator string for mapping form data to class it belongs to:
        "student" for Student,
        "alumni" for Alumni,
        "admin" for Admin,
        "superadmin" for SuperAdmin
    */
    public String discriminator = "student";
    public String profile = null;

    public UserForm() {}

    public UserForm(School school) {
        this.school = school;
    }

    public UserForm(String name, String password, String email, School school, String discriminator, String profile,User user) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.school = school;
        this.discriminator = discriminator;
        this.profile = profile;
        this.user = user;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        UserDAOImpl udao = new UserDAOImpl();

        if (name == null || name.length() == 0) {
            errors.add(new ValidationError("name", "No name was given"));
        }

        //TODO: add password checks? (e.g. password too short etc.)

        //found a nice email regex :D credit: http://www.regular-expressions.info/email.html. It's one near the bottom :P
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile("\\b[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\b");

        if (email == null || email.length() == 0) {
            errors.add(new ValidationError("email","No email was given"));
        } else if (!regex.matcher(email).matches()) {
            errors.add(new ValidationError("email","Invalid email was given"));
            //TODO: do better email validation
        } else if (user != null && !user.getEmail().equals(email) && udao.getUserByEmail(email) != null) {
            errors.add(new ValidationError("email","A user already exists with that email address"));
        } else if (user == null && udao.getUserByEmail(email) != null) {
            errors.add(new ValidationError("email","A user already exists with that email address"));
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
        if (school != null) {
            if(sdao.byName(school.getName()) == null) {
                errors.add(new ValidationError("school", "Invalid school provided"));
            }
        }

        if (errors.size() > 0) {
            return errors;
        }

        return null;
    }
}
