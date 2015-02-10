package models;

import javax.persistence.*;
import play.db.ebean.*;

/**
 * Created by Louise on 04/02/2015.
 */
@Entity
public class Question extends Model {
    @Id
    private Long questionId;
    private String text;
    private int duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SCHOOL")
    public School school;

    Question(String text, School school,int duration) {
        this.text = text;
        this.school = school;
        this.duration = duration;
    }

    // TODO: delete this; used only for testing; seems that previous Question constr is not public
    public Question(String text) {
        this.text = text;
        this.school = null;
        this.duration = 0;
    }

    public static Finder<Long,Question> find = new Finder<>(Long.class, Question.class);

    public long getId() {
        return questionId;
    }

    public void setId(long id) {
        this.questionId = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDuration() { return duration; }

    public void setDuration(int duration) { this.duration = duration; }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
        if (!school.getQuestions().contains(this)) {
            school.getQuestions().add(this);
        }
    }
}