package models;

import java.util.ArrayList;
import java.util.List;

import static com.avaje.ebean.Expr.eq;

/**
 * Created by Louise on 11/02/2015.
 */
public class VideoDAO {

    public VideoDAO() {}

    public Video getVideo(Long id) {
        return Video.find.byId(id);
    }

    public void deleteVideo(Long id) {
        Video.find.byId(id).delete();
    }

    public List<Video> getAllVideos() {
        return Video.find.all();
    }

    //TODO: change this so it only gets approved videos
    public List<Video> getVideosBySchool(School s) {
        List<Alumni> alumnis = Alumni.find.where().eq("school", s).findList();
        List<Video> videos = new ArrayList<>();
        for (Alumni a : alumnis) {
            List<Video> vids = Video.find.where().eq("user",a).findList();
            videos.addAll(vids);
        }
        return videos;
    }

    public List<Video> getAllApprovedVideos() {
        return Video.find.where().eq("approved", true).findList();
    }
}
