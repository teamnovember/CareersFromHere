package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.AdminHelpers;
import models.*;
import play.api.Routes;
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
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();

        if (Video.find.findRowCount() == 0) {
            // Seed data, TEDx videos sorted by popularity
            School s = new School("Super High School");
            s.save();
            Alumni u = new Alumni("Test user", "Stuff", "test@asdf.com",s);
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
        List<Video> accessibleVideos = new ArrayList<Video>();
        if (user == null) {
            accessibleVideos = dao.getAllPublicVideos();
        }
        else {
            accessibleVideos = dao.getVideosBySchool(user.getSchool());
        }

        CategoryDAO cdao = new CategoryDAO();
        List<Category> allCats = cdao.getAllCategories();

        Map<String, Boolean> catIdNameMap = new HashMap<>();
        for(Category c : allCats)  {
            catIdNameMap.put(c.getName(), false);
        }

        Form<CategorySelectionForm> catForm = form(CategorySelectionForm.class)
                .fill(new CategorySelectionForm(allCats));

        return ok(index.render(accessibleVideos, catForm, catIdNameMap, user));
    }

    public static Result categorySelect()
    {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();

        CategoryDAO cdao = new CategoryDAO();
        List<Category> allCats = cdao.getAllCategories();

        CategorySelectionForm form = Form.form(CategorySelectionForm.class).bindFromRequest().get();

        Map<String, Boolean> catIdNameMap = new HashMap<>();
        List<Category> selectedCategories = new ArrayList<Category>();
        for (Category c : allCats) {
            // TODO: add to a separate list for filtering
            // form.categories contains malformed Category objects -- only name is present, no ids, etc.
            boolean cont = (form.categories == null) ? false : AdminHelpers.CategoryContains(form.categories, c);
            if(cont) selectedCategories.add(c);
            catIdNameMap.put(c.getName(), cont);
        }

        Form<CategorySelectionForm> catForm = form(CategorySelectionForm.class)
                .fill(new CategorySelectionForm(allCats));

        VideoDAO dao = new VideoDAO();
        List<Video> accessibleVideos = new ArrayList<>();

        if (user == null) {
            accessibleVideos = dao.getAllPublicVideosByCategories(selectedCategories);
        }
        else {
            accessibleVideos = dao.getVideosBySchoolAndCategories(user.getSchool(), selectedCategories);
        }


        return ok(index.render(accessibleVideos, catForm, catIdNameMap, user));
    }

    //TODO: change this to view the correct video from database
    public static Result view(Long id) {
        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();
        VideoDAO vdao = new VideoDAO();
        Video v = vdao.getVideo(id);

        if (v.getUser().getSchool().getId() != user.getSchool().getId()) {
            flash("error", "You don't have sufficient permissions to view requested video.");
            return redirect("/");
        }

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

        return ok(view.render(v, user));
    }

    @Security.Authenticated(AlumniSecured.class)
    public static Result record() {
        ArrayList<Question> questions = new ArrayList<>();

        questions.add(0, new Question("People say nothing is impossible, but I do nothing every day.", 19.0, null));
        questions.add(1, new Question("Etc. – End of Thinking Capacity.", 37.0, null));
        questions.add(2, new Question("We live in the era of smart phones and stupid people.", 75.0, null));
        questions.add(3, new Question("Alarm Clocks: because every morning should begin with a heart attack.", 153.0, null));

        // TODO: this might look redundant now , but it can be useful later
        ArrayList<String> questionsText = new ArrayList<>();
        for (int i = 0; i < questions.size(); ++i)
            questionsText.add(i, questions.get(i).getText());

        ArrayList<Double> questionsDurations = new ArrayList<>();
        for (int i = 0; i < questions.size(); ++i)
            questionsDurations.add(i, questions.get(i).getDuration());

        ObjectMapper obj = new ObjectMapper();
        String JSONQuestionsText = null;
        String JSONQuestionsDurations = null;
        try {
            JSONQuestionsText = obj.writeValueAsString(questionsText);
            JSONQuestionsDurations = obj.writeValueAsString(questionsDurations);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        UserDAOImpl udao = new UserDAOImpl();
        User user = udao.getUserFromContext();

        return ok(record.render(questions, JSONQuestionsText, JSONQuestionsDurations, user));
    }

}
