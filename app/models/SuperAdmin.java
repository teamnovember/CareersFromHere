package models;

import views.forms.UserForm;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

/**
 * Created by biko on 05/02/15.
 */
@Entity
@DiscriminatorValue("superadmin")
public class SuperAdmin extends Admin {

    public SuperAdmin(String name, String password, String email, School school){
        super(name, password, email, school);
    }

    public static SuperAdmin makeInstance(UserForm data) {
        School s = (new SchoolDAO()).byName(data.school.getName());
        SuperAdmin admin = new SuperAdmin(data.name, data.password, data.email, s);
        return admin;
    }

    public void setActiveSchool(School school){
        super.setSchool(school);
    }

    public List<School> getSchools() {
        SchoolDAO dao = new SchoolDAO();
        return dao.getAllSchool();
    }
}
