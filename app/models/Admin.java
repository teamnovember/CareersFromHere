package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import helpers.AppException;
import helpers.HashHelper;
import views.forms.UserForm;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

/**
 * Created by biko on 05/02/15.
 */

/*
 * NOTE: If you're adding a function to Admin that
 * should also be accessible to SuperAdmins, remember
 * to copy it over to SuperAdmin as SuperAdmin no
 * longer inherits from Admin.
 */

@Entity
@DiscriminatorValue("admin")
public class Admin extends User {

    public Admin(String name,String password,String email, School school){
        super(name,password,email, school);
    }

    public static Admin makeInstance(UserForm data) {
        School s = (new SchoolDAO()).byName(data.school.getName());
        String password = "";
        try {
            password = HashHelper.createPassword(data.password);
        } catch (AppException e) {
            //TODO: do something useful here maybe?
        }
        Admin admin = new Admin(data.name, password, data.email, s);
        return admin;
    }

    public List<Video> getUnapprovedVideos(){
        ExpressionList<Video> expList = Video.find.where().eq("approved",0);
        List<Video> videos = expList.findList();

        return videos;
    }

    public List<Video> getApprovedVideos(){
        ExpressionList<Video> expList = Video.find.where().eq("approved",1);
        List<Video> videos = expList.findList();

        return videos;
    }

    public List<Video> getAllVideos(){
        List<Video> videos = Ebean.find(Video.class).select("*").orderBy("Video.approved, approved asc").findList();
        return videos;
    }

    public void addQuestion(String question, int duration){
        School school = this.getSchool();
        school.addQuestion(question,duration);
    }

    public List<Student> getStudents() {
        School school = this.getSchool();
        List<Student> students = Student.find.where().eq("school",school).findList();
        return students;
    }

    public List<Alumni> getAlumni() {
        School school = this.getSchool();
        List<Alumni> alumni = Alumni.find.where().eq("school",school).findList();
        return alumni;
    }
}
