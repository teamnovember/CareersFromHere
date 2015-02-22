package views.forms;

import models.School;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louise on 11/02/2015.
 */
public class QuestionForm {
    public String text = "";
    public double duration = 0;

    public QuestionForm() {}

    public QuestionForm(String text, double duration) {
        this.text = text;
        this.duration = duration;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        if (text == null || text.length() == 0) {
            errors.add(new ValidationError("text", "No question text was given"));
        }

        if (duration < 10.0) {
            errors.add(new ValidationError("duration","Duration is too small. Give at least 10 seconds."));
        }

        if (errors.size() > 0) {
            return errors;
        }

        return null;
    }
}
