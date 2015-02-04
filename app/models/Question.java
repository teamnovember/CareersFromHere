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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SCHOOL")
    public School school;

    Question(String text, School school) {
        this.text = text;
        this.school = school;
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