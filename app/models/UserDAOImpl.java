package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.SqlUpdate;
import play.mvc.Http;

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

    public List<User> getSchoolUsers(School school) {
        ExpressionList<User> expList = User.find.where().eq("school",school);
        List<User> users = expList.findList();
        return users;
    }

    public List<User> getSchoolUsersNoSA(School school) {
        ExpressionList<User> expList = User.find.where().eq("school",school).ne("discriminator","superadmin");
        List<User> users = expList.findList();
        return users;
    }

    public User getUser(Long ID){
        User user = User.find.byId(ID);
        if (user.getDiscriminator().equals("student")) {
            Student s = new Student(user.getName(),user.getPassword(),user.getEmail(),user.getSchool());
            s.setId(user.getId());
            s.setApproved(user.getApproved());
            return s;
        } else if (user.getDiscriminator().equals("alumni")) {
            Alumni a = new Alumni(user.getName(),user.getPassword(),user.getEmail(),user.getSchool());
            a.setId(user.getId());
            a.setApproved(user.getApproved());
            return a;
        } else if (user.getDiscriminator().equals("admin")) {
            Admin ad = new Admin(user.getName(),user.getPassword(),user.getEmail(),user.getSchool());
            ad.setId(user.getId());
            ad.setApproved(user.getApproved());
            return ad;
        } else if (user.getDiscriminator().equals("superadmin")) {
            SuperAdmin sa = new SuperAdmin(user.getName(),user.getPassword(),user.getEmail(),user.getSchool());
            sa.setId(user.getId());
            sa.setApproved(user.getApproved());
            return sa;
        }
        else return user; //TODO: throw an exception or something
    }

    public User getUserByEmail(String email) {
        User u = User.find.where().eq("email",email).findUnique();
        return u;
    }

    public User getUserFromContext() {
        String email = Http.Context.current().session().get("email");
        User user = null;
        if (email != null) {
            user = getUserByEmail(email);
        }
        return user;
    }

    public void approveUser(Long ID) {
        User user = getUser(ID);
        user.setApproved(true);
        user.update();
    }

    public void deleteUser(Long ID){
        SqlUpdate delete = Ebean.createSqlUpdate("DELETE FROM user WHERE id = :id");
        delete.setParameter("id",ID);
        delete.execute();
    }

}
