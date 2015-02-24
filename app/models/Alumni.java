package models;

import helpers.AppException;
import helpers.HashHelper;
import views.forms.UserForm;

import javax.persistence.*;
import java.util.List;


/**
 * Created by biko on 05/02/15.
 */
@Entity
@DiscriminatorValue("alumni")
public class Alumni extends User {

    public Alumni(String name,String password,String email, School school){
        super(name,password,email, school);
    }

    public static Alumni makeInstance(UserForm data) {
        School s = (new SchoolDAO()).byName(data.school.getName());
        String password = "";
        try {
            password = HashHelper.createPassword(data.password);
        } catch (AppException e) {
            //TODO: do something useful here maybe?
        }
        Alumni alumni = new Alumni(data.name, password, data.email, s);
        alumni.setProfile(data.profile);
        return alumni;
    }

    private String profile;
    @OneToMany(cascade=CascadeType.ALL)
    private List<Video> videos;

    public static Finder<Long,Alumni> find = new Finder<>(Long.class,Alumni.class);

    public void setProfile(String profile){
        this.profile=profile;
    }
    public String getProfile(){
        return profile;
    }

    public void uploadVideo(Video video){
        //todo see how this will be done
    }


}
