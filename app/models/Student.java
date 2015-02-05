package models;

import javax.persistence.Entity;

/**
 * Created by biko on 05/02/15.
 */

@Entity
public class Student extends User{

    public Student(String name, String email, String password, School school){
        super(name,email,password);
        super.setSchool(school);
    }

    public static Finder<Long,Student> find = new Finder<>(Long.class,Student.class);
}
