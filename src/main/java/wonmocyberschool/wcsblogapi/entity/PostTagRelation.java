package wonmocyberschool.wcsblogapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class PostTagRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_tag_relation_id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name="tag_id")
    private Tag tag;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        if(this.post != null){
            this.post.getPostTagRelations().remove(this);
        }
        this.post = post;
        post.getPostTagRelations().add(this);
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        if(this.tag != null){
            this.tag.getPostTagRelation().remove(this);
        }
        this.tag = tag;
        if(tag.getPostTagRelation() != null) {
            tag.getPostTagRelation().add(this);
        }
    }

}
