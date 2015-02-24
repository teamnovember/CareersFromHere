package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;

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

    public List<Video> getAllApprovedVideos() {
        return Video.find.where().eq("approved", true).findList();
    }

    public List<Video> getAllPublicVideos() {
        return Video.find.where().eq("publicAccess", true).eq("approved", true).findList();
    }

    public List<Video> getAllPublicVideosByCategories(List<Category> categories) {
        List<Video> videos = getAllPublicVideos();
        return filterVideosByCategories(videos, categories);
    }

    public List<Video> getVideosBySchool(School s) {
        if (s == null) {
            return getAllPublicVideos();
        }
        List<Alumni> alumnis = Alumni.find.where().eq("school", s).eq("approved", true).findList();
        List<Video> videos = new ArrayList<>();
        for (Alumni a : alumnis) {
            List<Video> vids = Video.find.where().eq("user",a).findList();
            videos.addAll(vids);
        }
        return videos;
    }

    public List<Video> getVideosBySchoolAndCategories(School s, List<Category> categories) {
        List<Video> videos = getVideosBySchool(s);
        return filterVideosByCategories(videos, categories);
    }

    private List<Video> filterVideosByCategories(List<Video> videos, List<Category> categories) {
        if (categories.size() == 0) {
            return videos;
        }
        VideoCategoryComparator comparator = new VideoCategoryComparator(categories);
        Collections.sort(videos, comparator);
        while (videos.size() > 0) {
            int index = videos.size() - 1;
            if (videos.get(index).numberOfMatchesWithCategories(categories) == 0) {
                videos.remove(index);
            }
            else {
                break;
            }
        }
        return videos;
    }
}

class VideoCategoryComparator implements Comparator<Video> {
    private List<Category> categories;

    VideoCategoryComparator(List<Category> categories) { this.categories = categories; }

    public int compare(Video v1, Video v2) {
        return v2.numberOfMatchesWithCategories(categories) - v1.numberOfMatchesWithCategories(categories);
    }
}

