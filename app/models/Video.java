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
    private Alumni user;
    private String title;
    @Lob
    private String description;
    private String thumbnailPath;
    private Boolean approved = true;
    @OneToMany(mappedBy="video", cascade=CascadeType.ALL)
    private List<VideoClip> videoClips;
    @ManyToMany(mappedBy = "videos")
    public List<Category> categories;

    public static Finder<Long,Video> find = new Finder<>(Long.class, Video.class);

    public Video(Alumni user, String title, String description, String thumbnailPath) {
        if (user == null) { throw new IllegalArgumentException("Video author must be provided"); }
        if (title == null) { throw new IllegalArgumentException("Video title must be provided"); }

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

    public Alumni getUser() { return user; }

    public void setUser(Alumni user) { this.user = user; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getThumbnailPath() { return thumbnailPath; }

    public void setThumbnailPath(String thumbnailPath) { this.thumbnailPath = thumbnailPath; }

    public double getDuration() {
        double totalDuration = 0.0;
        for (VideoClip clip : videoClips) {
            totalDuration += clip.getDuration();
        }
        return totalDuration;
    }

    public List<VideoClip> getVideoClips() { return videoClips; }

    public List<Category> getCategories() { return categories; }

    public void addCategory(Category c) {
        categories.add(c);
        //c.addVideo(this);
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean getApproved() {
        return this.approved;
    }

    public String getJSONPaths() {
        ArrayList<String> res = new ArrayList<String>();
        for (VideoClip clip : videoClips) {
            res.add(clip.getVideoPath());
        }
        return getJSONForList(res);
    }

    public String getJSONAudioPaths() {
        ArrayList<String> res = new ArrayList<String>();
        for (VideoClip clip : videoClips) {
            res.add(clip.getAudioPath());
        }
        return getJSONForList(res);
    }

    public String getJSONQuestionsText() {
        ArrayList<String> questionsText = new ArrayList<String>();
        for (VideoClip clip : videoClips) {
            questionsText.add(clip.getQuestion().getText());
        }
        return getJSONForList(questionsText);
    }

    public String getJSONDurations() {
        ArrayList<Double> res = new ArrayList<Double>();
        for (VideoClip clip : videoClips) {
            res.add(clip.getDuration());
        }
        return getJSONForList(res);
    }

    private String getJSONForList(ArrayList list) {
        String json = "";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            json = objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;

    }

    public int numberOfMatchesWithCategories(List<Category> categories) {
        List<Category> combinedCategories = getCategories();
        combinedCategories.addAll(categories);
        return getCategories().size() + categories.size() - combinedCategories.size();
    }
}
