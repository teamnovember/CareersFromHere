package models;



/**
 * Created by biko on 03/02/15.
 */
import javax.persistence.*;

import helpers.*;
import play.db.ebean.*;
import views.forms.UserForm;
import play.libs.Crypto;


@Entity
public class User extends Model {


    @Id
    private Long id;
    private String name;
    private String password;
    private String email;
    private boolean approved = false;
    @ManyToOne
    private School school;
    private String discriminator;


    public User(String name, String password, String email, String discriminator){
        this.name=name;
        this.email=email;
        this.password=password;
        this.discriminator=discriminator;
    }

    public static Finder<Long,User> find = new Finder<>(Long.class,User.class);

    public Long getId(){
        return this.id;
    }

    public String getPassword(){
        return password;
    }

    public boolean setPassword(String password){
        try{
            this.password= HashHelper.createPassword(password);
            return true;
        }catch (AppException e){
            return false; //todo confirm this is how we want this
        }
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

    public void approved(){
        this.approved = false;
    }
    public String getDiscriminator() { return this.discriminator; }
    public void setDiscriminator(String d) { this.discriminator = d; }

}
