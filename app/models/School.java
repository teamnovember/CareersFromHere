package models;

import javax.persistence.*;
import play.db.ebean.*;
import views.forms.SchoolForm;

import java.util.List;

/**
 * Created by Louise on 04/02/2015.
 */
@Entity
public class School extends Model {
    @Id
    private Long schoolId;
    private String name;

    @OneToMany(mappedBy="school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

    @OneToMany(mappedBy="school",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

    public School(String name) {
        this.name = name;
    }

    public static School makeInstance(SchoolForm data) {
        School s = new School(data.name);
        s.save();
        //adding the default questions added to the school - copy questions from default school
        SchoolDAO sdao = new SchoolDAO();
        School def = sdao.getSchool(0L);
        List<Question> qs = def.getQuestions();
        for(Question q : qs) {
            s.addQuestion(q.getText(), q.getDuration(),q.getOrder());
            s.update();
        }
        return s;
    }

    public static Finder<Long,School> find = new Finder<>(Long.class, School.class);

    public long getId() {
        return this.schoolId;
    }

    public void setId(long id) {
        this.schoolId = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public void addQuestion(String question, double duration, int order) {
        Question newQuestion = new Question(question, duration, this);
        newQuestion.setOrder(order);
        this.questions.add(newQuestion);
        newQuestion.save();
    }

    public List<Admin> getAdmins(){
        return Admin.find.where().eq("school",this).findList();

    }
}
