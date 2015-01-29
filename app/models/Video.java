package models;

import java.util.ArrayList;
import java.util.List;

public class Video {
    private String title;
    private String description;
    private Boolean approved = true;

    public Video(String title, String description) {
        this.title = title;
        this.description = description;
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
