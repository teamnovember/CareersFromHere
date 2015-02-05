package controllers;

import models.User;
import models.Video;
import play.mvc.*;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class VideoController extends Controller {
    public static Result index() {
        if (Video.find.findRowCount() == 0) {
            // Seed data, TEDx videos sorted by popularity
            Video v = new Video("Beatbox brilliance", "Blah", "http://img.youtube.com/vi/GNZBSZD16cY/0.jpg", 163);
            v.save();
            v = new Video("Hackschooling makes me happy", "Blah", "http://img.youtube.com/vi/h11u3vtcpaY/0.jpg", 155);
            v.save();
            v = new Video("My philosophy for a happy life", "Blah", "http://img.youtube.com/vi/36m1o-tM05g/0.jpg", 258);
            v.save();
            v = new Video("Why I stopped watching porn", "Blah", "http://img.youtube.com/vi/gRJ_QfP2mhU/0.jpg", 863);
            v.save();
            v = new Video("Coming out of your closet", "Blah", "http://img.youtube.com/vi/kSR4xuU07sc/0.jpg", 863);
            v.save();
        }
        List<Video> videoList = Video.find.all();

        User u = new User(1,"Edgaras Liberis","blahblah","el398@cam.ac.uk");

        return ok(index.render(videoList, u));
    }

    public static Result view(long id) {
        Video v = new Video("Beatbox brilliance", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce sollicitudin augue et molestie tincidunt. Suspendisse eu semper diam. Maecenas pulvinar arcu rhoncus augue suscipit, vitae sodales nulla condimentum. Etiam dignissim varius massa non tristique. Integer vel dolor et purus ultrices tincidunt ut eu turpis. Pellentesque laoreet varius diam et finibus. Etiam scelerisque erat velit, quis fringilla tellus interdum varius. Praesent eu rutrum tortor. Praesent volutpat tellus a mi viverra malesuada. Donec egestas ut nisi sed interdum. In vel ex at nisl pulvinar interdum. Aenean nisl velit, lobortis sed suscipit sed, mollis id nisl. Sed iaculis lectus eu lorem dignissim accumsan.", "http://img.youtube.com/vi/GNZBSZD16cY/0.jpg", 163);
        return ok(view.render(v, null));
    }
}
