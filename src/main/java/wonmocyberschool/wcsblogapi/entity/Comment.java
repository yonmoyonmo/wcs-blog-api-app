package wonmocyberschool.wcsblogapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long id;

    @Column(length = 1000)
    private String text;

    @ManyToOne
    @JoinColumn(name = "blog_user_id")
    private BlogUser blogUser;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @JsonIgnore
    @Transient
    private Long postId;


    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        if(this.post != null){
            this.post.getComments().remove(this);
        }
        this.post = post;
        post.getComments().add(this);
    }

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTime;

    public BlogUser getBlogUser() {
        return blogUser;
    }

    //편의 메소드
    public void setBlogUser(BlogUser blogUser) {
        if(this.blogUser != null){
            this.blogUser.getComments().remove(this);
        }
        this.blogUser = blogUser;
        blogUser.getComments().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
