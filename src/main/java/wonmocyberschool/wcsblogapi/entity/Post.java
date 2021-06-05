package wonmocyberschool.wcsblogapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    //foreign key
    @ManyToOne
    @JoinColumn(name = "blog_user_id")
    private BlogUser blogUser;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<PostTagRelation> postTagRelations = new ArrayList<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    //생성, 수정 API request 로 받는 부분--------------------------
    private String title;
    @Lob
    private String text;

    //@JsonIgnore
    @Transient
    private Long categoryId;
    //@JsonIgnore
    @Transient
    private List<String> tagList = new ArrayList<>();
    //@JsonIgnore
    @Transient
    private List<String> imageUrlList = new ArrayList<>();
    //-------------------------------------------------------------

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }
    public List<PostTagRelation> getPostTagRelations() {
        return postTagRelations;
    }

    public void setPostTagRelations(List<PostTagRelation> postTagRelations) {
        this.postTagRelations = postTagRelations;
    }
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
