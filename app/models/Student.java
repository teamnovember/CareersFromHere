package models;

import helpers.AppException;
import helpers.HashHelper;
import views.forms.UserForm;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by biko on 05/02/15.
 */

@Entity
@DiscriminatorValue("student")
public class Student extends User{

    public Student(String name, String password, String email, School school){
        super(name, password, email, school);
    }

    public static Student makeInstance(UserForm data) {
        School s = (new SchoolDAO()).byName(data.school.getName());
        String password = "";
        try {
            password = HashHelper.createPassword(data.password);
        } catch (AppException e) {
            //TODO: do something useful here maybe?
        }
        Student student = new Student(data.name, password, data.email, s);
        return student;
    }

    public static Finder<Long,Student> find = new Finder<>(Long.class,Student.class);

}
