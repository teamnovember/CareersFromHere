package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.db.ebean.*;

@Entity
public class Video extends Model {
    @Id
    private Long id;
    @ManyToOne
    private User user;
    private String title;
    @Lob
    private String description;
    private String thumbnailPath;
    private Boolean approved = true;
    @OneToMany(mappedBy="video", cascade=CascadeType.ALL)
    private List<VideoClip> videoClips;

    public static Finder<Long,Video> find = new Finder<>(Long.class, Video.class);

    public Video(User user, String title, String description, String thumbnailPath) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.thumbnailPath = thumbnailPath;
        this.approved = false;
    }

    public void addClip(VideoClip clip) {
        videoClips.add(clip);
        clip.setVideo(this);
    }

    public void edit(String newTitle, String newDescription) {
        this.title = newTitle;
        this.description = newDescription;
    }

    public Long getId() { return id; }

    public User getUser() { return user; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public String getThumbnailPath() { return thumbnailPath; }

    public double getDuration() {
        double totalDuration = 0.0;
        for (VideoClip clip : videoClips) {
            totalDuration += clip.getDuration();
        }
        return totalDuration;
    }

    public List<VideoClip> getVideoClips() { return videoClips; }

    public void approve(Boolean accept) {
        approved = accept;
        if (!accept) {
            delete();
        }
    }

    public String getJSONPaths() {
        ArrayList<String> res = new ArrayList<String>();
        for (VideoClip clip : videoClips) {
            res.add(clip.getVideoPath());
        }

        String json = "";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            json = objectMapper.writeValueAsString(res);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public String getJSONQuestionsText() {
        ArrayList<String> questionsText = new ArrayList<String>();
        for (VideoClip clip : videoClips) {
            questionsText.add(clip.getQuestion().getText());
        }

        String json = "";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            json = objectMapper.writeValueAsString(questionsText);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }

    public String getJSONDurations() {
        ArrayList<Double> res = new ArrayList<Double>();
        for (VideoClip clip : videoClips) {
            res.add(clip.getDuration());
        }

        String json = "";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            json = objectMapper.writeValueAsString(res);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }
}
