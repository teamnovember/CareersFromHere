package views.forms;

import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louise on 11/02/2015.
 */
public class CategoryForm {
    public String name = "";

    public CategoryForm() {}

    public CategoryForm(String name) {
        this.name = name;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        if (name == null || name.length() == 0) {
            errors.add(new ValidationError("name", "No name was given"));
        }

        if (errors.size() > 0) {
            return errors;
        }

        return null;
    }
}
