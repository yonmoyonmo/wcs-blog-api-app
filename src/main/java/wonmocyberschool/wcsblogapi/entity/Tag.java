package wonmocyberschool.wcsblogapi.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tag_id")
    private Long id;

    @Column(nullable = false)
    private String tagName;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    public Post getPost() {
        return post;
    }
    //편의 메소드
    public void setPost(Post post) {
        if(this.post != null){
            this.post.getTags().remove(this);
        }
        this.post = post;
        post.getTags().add(this);
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
