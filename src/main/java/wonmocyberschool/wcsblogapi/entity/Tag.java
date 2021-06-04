package wonmocyberschool.wcsblogapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tag_id")
    private Long id;

    @Column(nullable = false)
    private String tagName;

    @JsonIgnore
    @OneToMany(mappedBy = "tag")
    private List<PostTagRelation> postTagRelation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    public List<PostTagRelation> getPostTagRelation() {
        return postTagRelation;
    }

    public void setPostTagRelation(List<PostTagRelation> postTagRelation) {
        this.postTagRelation = postTagRelation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

}
