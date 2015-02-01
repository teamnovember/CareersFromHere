package controllers;

import models.Video;
import play.mvc.*;
import views.html.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class VideoController extends Controller {
    public static Result index() {

        // Temporary data, use
        List<Video> videoList = new ArrayList<Video>();
        videoList.add(new Video("My Career in ML", "Blah", "http://placehold.it/275x177/E8117F/FFFFFF", Duration.ofSeconds(163)));
        videoList.add(new Video("My Career in Prolog", "Blah", "http://placehold.it/275x177/E8117F/FFFFFF", Duration.ofSeconds(155)));
        videoList.add(new Video("My Failed Career in Java", "Blah", "http://placehold.it/275x177/E8117F/FFFFFF", Duration.ofSeconds(258)));
        videoList.add(new Video("How I lecture both CC and DBs", "Blah", "http://placehold.it/275x177/E8117F/FFFFFF", Duration.ofSeconds(863)));

        return ok(index.render(videoList));
    }
}
