package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import play.db.ebean.*;

@Entity
public class Video extends Model {
    @Id
    private Long id;
    private String title;
    private String description;
    private String thumbnailPath;
    private Integer duration;
    private Boolean approved = true;

    public static Finder<Long,Video> find = new Finder<>(Long.class, Video.class);

    public Video(String title, String description, String thumbnailPath, Integer duration) {
        this.title = title;
        this.description = description;
        this.thumbnailPath = thumbnailPath;
        this.duration = duration;
        this.approved = false;
    }

    public void edit(String newTitle, String newDescription) {
        this.title = newTitle;
        this.description = newDescription;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailPath() { return thumbnailPath; }

    public Integer getDuration() { return duration; }

    public void approve(Boolean accept) {
        approved = accept;
        if (!accept) {
            delete();
        }
    }

    public static List<Video> getVideos(String category) {
        return new ArrayList<>();
    }
}
