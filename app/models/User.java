package models;



/**
 * Created by biko on 03/02/15.
 */
import javax.persistence.*;
import play.db.ebean.*;
import views.forms.UserForm;


@Entity
public class User extends Model {


    @Id
    private Long id;
    private String name;
    private String password;
    private String email;
    @ManyToOne
    private School school;
    private String discriminator;

    public User(String name, String password, String email){
        this.name=name;
        this.email=email;
        this.password=password;
    }

    public static Finder<Long,User> find = new Finder<>(Long.class,User.class);

    public Long getId(){
        return this.id;
    }

    public String getPassword(){ //todo use secure social here
        return password;
    }

    public void setPassword(String password){ //todo use secure social here
        this.password=password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public School getSchool(){
        return this.school;
    }
    public void setSchool(School school){
        this.school = school;
    }

    public String getDiscriminator() { return this.discriminator; }
    public void setDiscriminator(String d) { this.discriminator = d; }

}
