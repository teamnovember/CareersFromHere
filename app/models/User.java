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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name="discriminator")
public abstract class User extends Model {
    @Id
    private Long id;
    private String name;
    private String password;
    private String email;
    private boolean approved = false;
    @ManyToOne
    private School school;

    public User(String name, String password, String email, School school){
        this.name=name;
        this.email=email;
        this.password=password;
        this.school = school;
    }

    public static User authenticate(String email, String password){
        User user = find.where().eq("email",email).findUnique();
        if(user == null) return null;
        boolean auth = HashHelper.checkPassword(password,user.password);
        if (auth) {
            return user;
        } else {
            return null;
        }
    }

    //hacky fix so new db structure is compatible with old code
    @Transient
    public String getDiscriminator(){
        DiscriminatorValue val = this.getClass().getAnnotation( DiscriminatorValue.class );

        if (val == null) {
            return null; //TODO: because it "can" return null we should probably add some checks for it even though it should NEVER be null
        } else {
            return val.value();
        }
    }


    public static Finder<Long,User> find = new Finder<>(Long.class,User.class);

    public Long getId(){
        return this.id;
    }

    public void setId(Long id) { this.id = id; }

    public String getPassword(){
        return password;
    }

    public boolean setPassword(String password){
        try{
            this.password = HashHelper.createPassword(password);
            return true;
        }catch (AppException e){
            return false;
        }
    }

    public void setPasswordNoHash(String password) {
        this.password = password;
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

    public boolean getApproved() { return approved; }

    public void setApproved(boolean a) { this.approved = a; }

}
