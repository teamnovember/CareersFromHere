package models;

import java.util.List;

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

    public List<Video> getAllApprovedVideos() {
        return Video.find.where().eq("approved", true).findList();
    }
}
