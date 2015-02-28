package views.forms;

import models.Category;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Louise on 11/02/2015.
 */
public class VideoForm {
    public String title = "";
    public String description = "";
    public List<Category> categories = new ArrayList<Category>();
    public String publicaccess = "";

    public VideoForm() {}

    public VideoForm(String title, String description, List<Category> categories, boolean publicaccess) {
        this.title = title;
        this.description = description;
        for(Category c : categories) {
            this.categories.add(c);
        }
        if (publicaccess) {
            this.publicaccess = "Yes";
        } else {
            this.publicaccess = "No";
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

        if (publicaccess == null || publicaccess.equals("")) {
            errors.add(new ValidationError("publicaccess", "No public access type was given"));
        } else if (!publicaccess.equals("Yes")
                && !publicaccess.equals("No")) {
            errors.add(new ValidationError("publicaccess", "Invalid public access type given"));
        }

        if (errors.size() > 0) {
            return errors;
        }

        return null;
    }
}
