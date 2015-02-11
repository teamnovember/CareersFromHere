package models;

import java.util.List;

/**
 * Created by Louise on 11/02/2015.
 */
public class CategoryDAO {

    public CategoryDAO() {}

    public Category getCategory(Long id) {
        return Category.find.byId(id);
    }

    public void deleteCategory(Long id) {
        Category.find.byId(id).delete();
    }

    public List<Category> getAllCategories() {
        return Category.find.all();
    }
}