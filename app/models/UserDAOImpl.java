package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by biko on 03/02/15.
 */
public class UserDAOImpl implements UserDAO {

    //list will be used as a database untill we get the database running
    List<User> users;

    public UserDAOImpl(){//placeholder constructor
        users = new ArrayList<User>();
        User user1 = new User(0,"Biko Agozino","blah","ba325@cam.ac.uk");
        User user2 = new User(1,"Edgaras Liberis","blahblah","el398@cam.ac.uk");
        users.add(user1);
        users.add(user2);
    }

    @Override
    public void deleteUser(User user){
        users.remove(user.getId());
        System.out.println("User: of ID " +user.getId() + "has been deleted");

    }


    //todo change this to a database impl, returning a list from the database
    @Override
    public List<User> getAllUsers(){
        return users;
    }


    //todo make this a database lookup
    @Override
    public User getUser(long id){
        return users.get((int)id);

    }

    //todo again this needs to use the database instead of the list
    @Override
    public void updateUser(User user){
        User old = users.get((int)user.getId());
        old.setName(user.getName());
        old.setEmail(user.getEmail());
        old.setPassword(user.getPassword());
    }

}
