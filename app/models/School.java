package models;

import javax.persistence.*;
import play.db.ebean.*;
import views.forms.SchoolForm;

import java.util.List;

/**
 * Represents a school.
 */
@Entity
public class School extends Model {
    /**
     * The ID used by the database
     */
    @Id
    private Long schoolId;
    /**
     * Unique name of the School.
     */
    private String name;

    /**
     * List of Questions associated with this School. The cascade type means that when this School is deleted all Questions associated with this School are also deleted.
     */
    @OneToMany(mappedBy="school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

    /**
     * List of Users associated with this School. The cascade type means that when this School is deleted all Users associated with this School are also deleted.
     */
    @OneToMany(mappedBy="school",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

    /**
     * Finder used to search the database for School objects
     */
    public static Finder<Long,School> find = new Finder<>(Long.class, School.class);

    /**
     * Creates a new School with the given name. (ID is auto-generated by the database).
     * @param name Name of the School.
     */
    public School(String name) {
        this.name = name;
    }

    /**
     * Helper for the forms. Makes an instance of a School and saves it in the database. Also adds the Questions from the 'Default' School located at database index ID '0'.
     * @param data SchoolForm that contains the data that we want to have associated with this School.
     * @return The School that was created from the form data.
     */
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

    /**
     * Getter for ID.
     * @return The ID of the School.
     */
    public long getId() {
        return this.schoolId;
    }

    /**
     * Setter for ID.
     * @param id The ID we want to be the new ID of the School.
     */
    public void setId(long id) {
        this.schoolId = id;
    }

    /**
     * Setter for name.
     * @return The name of the School.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for name.
     * @param name The name we want to be the new name of the School
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for questions
     * @return The List of questions associated with this School.
     */
    public List<Question> getQuestions() {
        return this.questions;
    }

    /**
     * Creates a new Question object, adds it to the question list of this School and saves the Question to the database.
     * @param question The text of the question we want to add.
     * @param duration The duration of the question we want to add.
     * @param order The ordering of the question we want to add.
     */
    public void addQuestion(String question, double duration, int order) {
        Question newQuestion = new Question(question, duration, this);
        newQuestion.setOrder(order);
        this.questions.add(newQuestion);
        newQuestion.save();
    }

    /**
     * Searches the database for all the Admin Users for this School.
     * @return A List of all Admins associated with this School.
     */
    public List<Admin> getAdmins(){
        return Admin.find.where().eq("school",this).findList();

    }
}
