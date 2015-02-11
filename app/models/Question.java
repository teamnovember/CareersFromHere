package models;

import javax.persistence.*;
import play.db.ebean.*;
import views.forms.QuestionForm;

/**
 * Created by Louise on 04/02/2015.
 */
@Entity
public class Question extends Model {
    @Id
    private Long questionId;
    private String text;
    private double duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SCHOOL")
    public School school;

    public Question(String text, double duration, School school) {
        this.text = text;
        this.school = school;
        this.duration = duration;
    }

    public static Question makeInstance(QuestionForm data) {
        Question q = new Question(data.text,data.duration,data.school);
        return q;
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

    public double getDuration() { return duration; }

    public void setDuration(double duration) { this.duration = duration; }

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