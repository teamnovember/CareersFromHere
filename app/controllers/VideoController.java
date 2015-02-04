package controllers;

import models.Video;
import play.mvc.*;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

public class VideoController extends Controller {
    public static Result index() {

        // Temporary data, TEDx videos sorted by popularity
        List<Video> videoList = new ArrayList<Video>();
        videoList.add(new Video("Beatbox brilliance", "Blah", "http://img.youtube.com/vi/GNZBSZD16cY/0.jpg", 163));
        videoList.add(new Video("Hackschooling makes me happy", "Blah", "http://img.youtube.com/vi/h11u3vtcpaY/0.jpg", 155));
        videoList.add(new Video("My philosophy for a happy life", "Blah", "http://img.youtube.com/vi/36m1o-tM05g/0.jpg", 258));
        videoList.add(new Video("Why I stopped watching porn", "Blah", "http://img.youtube.com/vi/gRJ_QfP2mhU/0.jpg", 863));
        videoList.add(new Video("Coming out of your closet", "Blah", "http://img.youtube.com/vi/kSR4xuU07sc/0.jpg", 863));

        return ok(index.render(videoList));
    }

    public static Result view(long id) {
        Video v = new Video("Beatbox brilliance", "Blah", "http://img.youtube.com/vi/GNZBSZD16cY/0.jpg", 163);
        return ok(view.render(v));
    }
}
