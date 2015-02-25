package views.forms;

import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by el on 25/02/15.
 */
public class AlumniRegForm extends UserForm {

    @Override
    public List<ValidationError> validate() {
        List<ValidationError> perrors = super.validate();
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if(perrors != null) errors.addAll(perrors);

        if(password == null || password.equals(""))
            errors.add(new ValidationError("password", "No password provided."));

        if(school == null)
            errors.add(new ValidationError("school", "No school provided."));

        if(profile == null || profile.equals(""))
            errors.add(new ValidationError("profile", "No profile information provided."));

        if(!errors.isEmpty()) return errors;
        return null;
    }
}
