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
        if (user.getType() == 1) {
            Student s = new Student(user.getName(),user.getEmail(),user.getPassword(),user.getSchool());
            return s;
        } else if (user.getType() == 2) {
            Alumni a = new Alumni(user.getName(),user.getEmail(),user.getPassword(),user.getSchool());
            return a;
        } else if (user.getType() == 3) {
            Admin ad = new Admin(user.getName(),user.getEmail(),user.getPassword(),user.getSchool());
            return ad;
        } else {
            SuperAdmin sa = new SuperAdmin(user.getName(),user.getEmail(),user.getPassword(),user.getSchool());
            return sa;
        }
    }

    public void deleteUser(Long ID){
        User.find.byId(ID).delete();
    }

}
