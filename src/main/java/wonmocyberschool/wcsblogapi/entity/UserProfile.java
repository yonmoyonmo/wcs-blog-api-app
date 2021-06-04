package wonmocyberschool.wcsblogapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_profile_id")
    private Long id;

    //1:1 관계이지만 혹시 더 갖고 싶을까봐 다대일로 함
    //foreign key
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "blog_user_id")
    private BlogUser owner;

    //스토리지 서버에 프로필 이미지 저장하는 API 만들어야 한다.
    private String profileImageURL;

    @Lob
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    //편의메소드
    public void setOwner(BlogUser owner) {
        if(this.owner != null){
            this.owner.getProfiles().remove(this);
        }
        this.owner = owner;
        owner.getProfiles().add(this);
    }

    public BlogUser getOwner() {
        return owner;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public Date getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
