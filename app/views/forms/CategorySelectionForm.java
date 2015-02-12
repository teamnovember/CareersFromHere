package views.forms;

import models.Category;

import java.util.List;

/**
 * Created by el on 12/02/15.
 */
public class CategorySelectionForm {
    public List<Category> categories;
    public CategorySelectionForm(List<Category> categories) {
        this.categories = categories;
    }
    public CategorySelectionForm() {}

    // TODO: add validation
}
