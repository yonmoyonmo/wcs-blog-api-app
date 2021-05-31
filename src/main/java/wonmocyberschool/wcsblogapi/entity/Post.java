package wonmocyberschool.wcsblogapi.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    //foreign key
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    //foreign key
    @ManyToOne
    @JoinColumn(name = "blog_user_id")
    private BlogUser blogUser;

    @OneToMany(mappedBy = "post")
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Image> images = new ArrayList<>();

    private String title;

    @Lob
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    public Category getCategory() {
        return category;
    }

    //편의 메소드
    public void setCategory(Category category) {
        if(this.category != null){
            this.category.getPosts().remove(this);
        }
        this.category = category;
        category.getPosts().add(this);
    }

    public BlogUser getBlogUser() {
        return blogUser;
    }

    //편의 메소드
    public void setBlogUser(BlogUser blogUser) {
        if(this.blogUser != null){
            this.blogUser.getPosts().remove(this);
        }
        this.blogUser = blogUser;
        blogUser.getPosts().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
