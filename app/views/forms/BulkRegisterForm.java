package views.forms;

import models.School;
import models.SchoolDAO;
import models.UserDAOImpl;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louise on 25/02/2015.
 */
public class BulkRegisterForm {
    public School school = null;
    public String data = "";
    public String discriminator = "student";
    /**
     Discriminator string for mapping form data to class it belongs to:
     "student" for Student,
     "alumni" for Alumni,
     "admin" for Admin,
     "superadmin" for SuperAdmin
     */

    public BulkRegisterForm() {}

    public BulkRegisterForm(School school) {
        this.school = school;
    }

    public BulkRegisterForm(String data, School school, String discriminator) {
        this.data = data;
        this.school = school;
        this.discriminator = discriminator;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();

        SchoolDAO sdao = new SchoolDAO();
        if (school != null) {
            if(sdao.byName(school.getName()) == null) {
                errors.add(new ValidationError("school", "Invalid school provided"));
            }
        }

        if (data == null || data.equals("")) {
            errors.add(new ValidationError("data","No email addresses given"));
        }

        if (discriminator == null || discriminator.equals("")) {
            errors.add(new ValidationError("discriminator", "No user type was given"));
        } else if (!discriminator.equals("student")
                && !discriminator.equals("alumni")
                && !discriminator.equals("admin")
                && !discriminator.equals("superadmin")) {
            errors.add(new ValidationError("discriminator", "Invalid user type given"));
        }

        if (errors.size() > 0) {
            return errors;
        }

        return null;
    }
}
