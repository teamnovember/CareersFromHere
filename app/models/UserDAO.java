package models;

/**
 * Created by biko on 03/02/15.
 */

import java.util.List;


public interface UserDAO {

    public List<User> getAllUsers();
    public User getUser(long id);
    public void updateUser(User user);
    public void deleteUser(User user);


}
