package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by biko on 03/02/15.
 */
public class UserDAOImpl<T extends User> implements UserDAO {

    public UserDAOImpl() {}

    public List<User> getAllUsers(){
        List<User> users = User.find.all();
        return users;
    }
    public User getUser(Long ID){
        User user = User.find.byId(ID);
        return user;
    }

    public void deleteUser(Long ID){
        User.find.byId(ID).delete();
    }

}
