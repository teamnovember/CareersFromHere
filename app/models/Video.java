package models;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Video {
    private String title;
    private String description;
    private String thumbnailPath;
    private Duration duration;
    private Boolean approved = true;

    public Video(String title, String description, String thumbnailPath, Duration duration) {
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

    public Duration getDuration() { return duration; }

    public void delete() {

    }

    public void approve(Boolean accept) {
        approved = accept;
        if (!accept) {
            delete();
        }
    }

    public List<Video> getVideos() {
        return new ArrayList<>();
    }

    public List<Video> getVideos(String category) {
        return new ArrayList<>();
    }
}
