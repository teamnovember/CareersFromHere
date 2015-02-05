package models;

import com.avaje.ebean.ExpressionList;

import java.util.List;

/**
 * Created by biko on 05/02/15.
 */
public class Admin extends User {

    public Admin(String name,String password,String email, School school){
        super(name,password,email);
        super.setSchool(school);
    }
    public List<Video> getUnapprovedVideos(){

        ExpressionList<Video> explist = Video.find.where().eq("approved",Video.class);//todo this is just a placeholder
        List<Video> videos = explist.findList();//todo find the results


        return videos;
    }

    public void addQuestion(String question, int duration){
        new Question(question,this.getSchool(),duration);
    }

}
