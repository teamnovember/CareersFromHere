package models;

import java.util.List;

/**
 * Created by Louise on 05/02/2015.
 */
public class SchoolDAO {

    public SchoolDAO() {}

    public School getSchool(Long id) {
        return School.find.byId(id);
    }

    public List<School> getAllSchool() {
        List<School> result = School.find.all();
        return result;
    }

    public void deleteSchool(Long id) {
        School.find.byId(id).delete();
    }

}
