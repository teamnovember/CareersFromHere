package views.forms;

import models.Category;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louise on 11/02/2015.
 */
public class VideoForm {
    public String title = "";
    public String description = "";
    public List<Category> categories = new ArrayList<Category>();

    public VideoForm() {}

    public VideoForm(String title, String description, List<Category> categories) {
        this.title = title;
        this.description = description;
        for(Category c : categories) {
            this.categories.add(c);
        }
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        if (title == null || title.length() == 0) {
            errors.add(new ValidationError("title", "No video title was given"));
        }
        if (description == null || description.length() == 0) {
            errors.add(new ValidationError("description", "No video description was given"));
        }
        if (errors.size() > 0) {
            return errors;
        }

        return null;
    }
}
