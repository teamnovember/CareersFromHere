package models;

import javax.persistence.*;
import play.db.ebean.*;

@Entity
public class VideoClip extends Model {
    @Id
    private Long id;
    private String videoClipPath;
    private Integer duration;
    @ManyToOne
    private Video video;
    @ManyToOne
    private Question question;

    public static Finder<Long,VideoClip> find = new Finder<>(Long.class, VideoClip.class);

    public VideoClip(String path, Question question, Integer duration) {
        this.videoClipPath = path;
        this.question = question;
        this.duration = duration;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Video getVideo() { return video; }

    public Long getId() { return id; }

    public String getVideoPath() { return videoClipPath; }

    public Integer getDuration() { return duration; }
}
