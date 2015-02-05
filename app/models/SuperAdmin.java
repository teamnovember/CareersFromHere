package models;

/**
 * Created by biko on 05/02/15.
 */
public class SuperAdmin extends Admin {

    public SuperAdmin(String name, String password, String email, School school){
        super(name,password,email,school);
    }

    public void setActiveSchool(School school){
        super.setSchool(school);
    }
}
