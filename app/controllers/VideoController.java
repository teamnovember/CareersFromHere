package controllers;

import models.*;
import play.data.Form;
import play.mvc.*;
import views.forms.CategorySelectionForm;
import views.html.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static play.data.Form.form;

public class VideoController extends Controller {
    public static Result index() {
        if (Video.find.findRowCount() == 0) {
            // Seed data, TEDx videos sorted by popularity
            School s = new School("Super High School");
            s.save();
            User u = new User("Test user", "Stuff", "test@asdf.com", "alumni", s);
            u.save();
            Video v = new Video(u, "Beatbox brilliance", "Blah", "http://img.youtube.com/vi/GNZBSZD16cY/0.jpg");
            v.save();
            v = new Video(u, "Hackschooling makes me happy", "Blah", "http://img.youtube.com/vi/h11u3vtcpaY/0.jpg");
            v.save();
            v = new Video(u, "My philosophy for a happy life", "Blah", "http://img.youtube.com/vi/36m1o-tM05g/0.jpg");
            v.save();
            v = new Video(u, "Why I stopped watching porn", "Blah", "http://img.youtube.com/vi/gRJ_QfP2mhU/0.jpg");
            v.save();
            v = new Video(u, "Coming out of your closet", "Blah", "http://img.youtube.com/vi/kSR4xuU07sc/0.jpg");
            v.save();
        }
        VideoDAO dao = new VideoDAO();
        List<Video> videoList = dao.getAllVideos();

        Student u = new Student("Test user", "Stuff", "test@asdf.com", null);
        CategoryDAO cdao = new CategoryDAO();
        List<Category> allCats = cdao.getAllCategories();

        Map<String, Boolean> catIdNameMap = new HashMap<>();
        for(Category c : allCats)  {
            catIdNameMap.put(c.getName(), false);
        }

        Form<CategorySelectionForm> catForm = form(CategorySelectionForm.class)
                .fill(new CategorySelectionForm(allCats));

        return ok(index.render(videoList, catForm, catIdNameMap, u));
    }

    public static Result categorySelect()
    {
        VideoDAO dao = new VideoDAO();
        List<Video> videoList = dao.getAllVideos();

        Student u = new Student("Test user", "Stuff", "test@asdf.com", null);
        CategoryDAO cdao = new CategoryDAO();
        List<Category> allCats = cdao.getAllCategories();

        CategorySelectionForm form = Form.form(CategorySelectionForm.class).bindFromRequest().get();
        List<String> selectedNames = new ArrayList<>();
        if(form.categories != null) {
            for (Category c : form.categories) {
                selectedNames.add(c.getName());
            }
        }

        Map<String, Boolean> catIdNameMap = new HashMap<>();
        for(Category c : allCats)  {
            // TODO: for categories in selectedNames (second argument is true), add to a separate list for filtering
            catIdNameMap.put(c.getName(), selectedNames.contains(c.getName()));
        }

        Form<CategorySelectionForm> catForm = form(CategorySelectionForm.class)
                .fill(new CategorySelectionForm(allCats));

        return ok(index.render(videoList, catForm, catIdNameMap, u));
    }

    public static Result view(Long id) {
        Alumni u = new Alumni("Test user", "Stuff", "test@asdf.com", null);
        Video v = new Video(u, "Beatbox brilliance", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce sollicitudin augue et molestie tincidunt. Suspendisse eu semper diam. Maecenas pulvinar arcu rhoncus augue suscipit, vitae sodales nulla condimentum. Etiam dignissim varius massa non tristique. Integer vel dolor et purus ultrices tincidunt ut eu turpis. Pellentesque laoreet varius diam et finibus. Etiam scelerisque erat velit, quis fringilla tellus interdum varius. Praesent eu rutrum tortor. Praesent volutpat tellus a mi viverra malesuada. Donec egestas ut nisi sed interdum. In vel ex at nisl pulvinar interdum. Aenean nisl velit, lobortis sed suscipit sed, mollis id nisl. Sed iaculis lectus eu lorem dignissim accumsan.", "http://img.youtube.com/vi/GNZBSZD16cY/0.jpg");

        Question q = new Question("RANDOM text RANDOM very", 0, null);
        VideoClip clip = new VideoClip("/assets/test1.mp4", null, q, 20.34068);
        v.addClip(clip);
        q = new Question("I. S. R. A. O. Y. O. T. E.", 0, null);
        clip = new VideoClip("/assets/test2.mp4", null, q, 14.481133);
        v.addClip(clip);
        q = new Question("I shall remember all of you on the exams.", 0, null);
        clip = new VideoClip("/assets/test3.mp4", null, q, 19.686333);
        v.addClip(clip);
        q = new Question("The great ending ...", 0, null);
        clip = new VideoClip("/assets/test4.mp4", null, q, 16.964983);
        v.addClip(clip);
        q = new Question("TOC TOC", 0, null);
        clip = new VideoClip("/assets/test5.mp4", null, q, 7.941267);
        v.addClip(clip);
        return ok(view.render(v, null));
    }
}
