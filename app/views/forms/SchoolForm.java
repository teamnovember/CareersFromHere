package views.forms;

import models.SchoolDAO;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louise on 11/02/2015.
 */
public class SchoolForm {
    public String name = "";
    public SchoolForm() {}

    public SchoolForm(String name) {
        this.name = name;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        SchoolDAO sdao = new SchoolDAO();

        if (name == null || name.length() == 0) {
            errors.add(new ValidationError("name", "No name was given"));
        } if (sdao.byName(name) != null) {
            errors.add(new ValidationError("name","A school with that name already exists"));
        }

        if (errors.size() > 0) {
            return errors;
        }

        return null;
    }
}
