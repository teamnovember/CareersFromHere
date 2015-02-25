package helpers;

import models.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by el on 21/02/15.
 */
public abstract class AdminHelpers {
    public static Map<String, Boolean> ConstructDiscriminatorMap(String currentDiscriminator,String authorisation) {
        Map<String, Boolean> discrMap = new HashMap<String, Boolean>();
        discrMap.put("student",    "student".equals(currentDiscriminator));
        discrMap.put("alumni",     "alumni".equals(currentDiscriminator));
        discrMap.put("admin",      "admin".equals(currentDiscriminator));
        if(authorisation.equals("superadmin")) {
            discrMap.put("superadmin", "superadmin".equals(currentDiscriminator));
        }
        return discrMap;
    }

    public static Map<String, Boolean> ConstructSchoolMap(String selectedSchoolName) {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        Map<String, Boolean> schoolMap = new HashMap<String, Boolean>();
        SchoolDAO dao = new SchoolDAO();
        for(School school : dao.getAllSchool()) {
            if(school.getId() != 0 || user.getDiscriminator().equals("superadmin")) { //these checks are here so that school admins can't fiddle with the default school (ID = 0) but superadmins can
                schoolMap.put(school.getName(), school.getName().equals(selectedSchoolName));
            }
        }
        return schoolMap;
    }

    public static Map<String, Boolean> ConstructCategoryMap(List<Category> existingCats) {
        Map<String, Boolean> catMap = new HashMap<String, Boolean>();
        CategoryDAO cdao = new CategoryDAO();
        for(Category c : cdao.getAllCategories()) {
            catMap.put(c.getName(), existingCats.contains(c));
        }
        return catMap;
    }

    public static boolean CategoryContains(List<Category> cats, Category cat) {
        if(cats == null || cat == null) return false;
        for(Category c : cats) {
            if(cat.getName().equals(c.getName()))
                return true;
        }
        return false;
    }
}
