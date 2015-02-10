package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by biko on 03/02/15.
 */
public class UserDAOImpl implements UserDAO {

    public UserDAOImpl() {}

    public List<User> getAllUsers(){
        List<User> users = User.find.all();
        return users;
    }
    public User getUser(Long ID){
        User user = User.find.byId(ID);
        if (user.getDiscriminator().equals("student")) {
            Student s = new Student(user.getName(),user.getEmail(),user.getPassword(),user.getSchool());
            return s;
        } else if (user.getDiscriminator().equals("alumni")) {
            Alumni a = new Alumni(user.getName(),user.getEmail(),user.getPassword(),user.getSchool());
            return a;
        } else if (user.getDiscriminator().equals("admin")) {
            Admin ad = new Admin(user.getName(),user.getEmail(),user.getPassword(),user.getSchool());
            return ad;
        } else if (user.getDiscriminator().equals("superadmin")) {
            SuperAdmin sa = new SuperAdmin(user.getName(),user.getEmail(),user.getPassword(),user.getSchool());
            return sa;
        }
        else return user; //TODO: throw an exception or something
    }

    public void deleteUser(Long ID){
        User.find.byId(ID).delete();
    }

}
