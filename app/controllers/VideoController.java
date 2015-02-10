package controllers;

import models.Question;
import models.User;
import models.Video;
import models.VideoClip;
import play.mvc.*;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class VideoController extends Controller {
    public static Result index() {
        if (Video.find.findRowCount() == 0) {
            // Seed data, TEDx videos sorted by popularity
            User u = new User("Test user", "Stuff", "test@asdf.com");
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
        List<Video> videoList = Video.find.all();

        User u = new User("Edgaras Liberis","blahblah","el398@cam.ac.uk");

        return ok(index.render(videoList, u));
    }

    public static Result view(long id) {
        User u = new User("Test user", "Stuff", "test@asdf.com");
        Video v = new Video(u, "Beatbox brilliance", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce sollicitudin augue et molestie tincidunt. Suspendisse eu semper diam. Maecenas pulvinar arcu rhoncus augue suscipit, vitae sodales nulla condimentum. Etiam dignissim varius massa non tristique. Integer vel dolor et purus ultrices tincidunt ut eu turpis. Pellentesque laoreet varius diam et finibus. Etiam scelerisque erat velit, quis fringilla tellus interdum varius. Praesent eu rutrum tortor. Praesent volutpat tellus a mi viverra malesuada. Donec egestas ut nisi sed interdum. In vel ex at nisl pulvinar interdum. Aenean nisl velit, lobortis sed suscipit sed, mollis id nisl. Sed iaculis lectus eu lorem dignissim accumsan.", "http://img.youtube.com/vi/GNZBSZD16cY/0.jpg");

        Question q = new Question("RANDOM text RANDOM very", null, 0);
        VideoClip clip = new VideoClip("/assets/test1.mp4", q, 20.34068);
        v.addClip(clip);
        q = new Question("I. S. R. A. O. Y. O. T. E.", null, 0);
        clip = new VideoClip("/assets/test2.mp4", q, 14.481133);
        v.addClip(clip);
        q = new Question("I shall remember all of you on the exams.", null, 0);
        clip = new VideoClip("/assets/test3.mp4", q, 19.686333);
        v.addClip(clip);
        q = new Question("The great ending ...", null, 0);
        clip = new VideoClip("/assets/test4.mp4", q, 16.964983);
        v.addClip(clip);
        q = new Question("TOC TOC", null, 0);
        clip = new VideoClip("/assets/test5.mp4", q, 7.941267);
        v.addClip(clip);
        return ok(view.render(v, null));
    }
}
