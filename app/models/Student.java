package models;

import views.forms.UserForm;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by biko on 05/02/15.
 */

@Entity
@DiscriminatorValue("student")
public class Student extends User{

    public Student(String name, String email, String password, School school){
        super(name,email,password, school);
    }

    public static Student makeInstance(UserForm data) {
        Student student = new Student(data.name, data.email, data.password, data.school);
        return student;
    }

    public static Finder<Long,Student> find = new Finder<>(Long.class,Student.class);

}
