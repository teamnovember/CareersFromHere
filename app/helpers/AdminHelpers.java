package helpers;

import models.School;
import models.SchoolDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by el on 21/02/15.
 */
public abstract class AdminHelpers {
    public static Map<String, Boolean> ConstructDiscriminatorMap(String currentDiscriminator) {
        Map<String, Boolean> discrMap = new HashMap<String, Boolean>();
        discrMap.put("student",    "student".equals(currentDiscriminator));
        discrMap.put("alumni",     "alumni".equals(currentDiscriminator));
        discrMap.put("admin",      "admin".equals(currentDiscriminator));
        discrMap.put("superadmin", "superadmin".equals(currentDiscriminator));
        return discrMap;
    }

    public static Map<String, Boolean> ConstructSchoolMap(String selectedSchoolName) {
        Map<String, Boolean> schoolMap = new HashMap<String, Boolean>();
        SchoolDAO dao = new SchoolDAO();
        for(School school : dao.getAllSchool()) {
            schoolMap.put(school.getName(), school.getName().equals(selectedSchoolName));
        }
        return schoolMap;
    }
}
