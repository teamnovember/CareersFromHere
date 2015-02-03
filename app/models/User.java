package models;



/**
 * Created by biko on 03/02/15.
 */


public class User {

    private long id;
    private String name;
    private String password;
    private String email;

   User(long id, String name, String password, String email){
        this.id = id;
        this.name=name;
        this.email=email;
        this.password=password;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
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
}
