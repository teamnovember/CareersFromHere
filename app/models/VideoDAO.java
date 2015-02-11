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

    public List<Video> getAllVideo() {
        return Video.find.all();
    }
}
