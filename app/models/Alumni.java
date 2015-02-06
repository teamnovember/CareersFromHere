package models;

import javax.persistence.*;
import java.util.List;


/**
 * Created by biko on 05/02/15.
 */
public class Alumni extends User {

    public Alumni(String name,String password,String email, School school){
        super(name,password,email);
        super.setSchool(school);
    }

    private String profile;
    @OneToMany
    List<Video> videos;

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
