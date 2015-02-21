package models;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;

/**
 * Created by Louise on 11/02/2015.
 */
@Entity
public class Category extends Model {
    @Id
    private Long id;
    private String name;

    @ManyToMany
    private List<Video> videos;

    public static Model.Finder<Long,Category> find = new Model.Finder<>(Long.class, Category.class);

    public Category(String name) {
        this.name = name;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public List<Video> getVideos() { return videos; }

    public void addVideo(Video v) {
        videos.add(v);
        //v.addCategory(this);
    }
}
