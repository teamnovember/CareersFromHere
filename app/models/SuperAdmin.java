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
        SuperAdmin admin = new SuperAdmin(data.name,data.email,data.password,data.school);
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
