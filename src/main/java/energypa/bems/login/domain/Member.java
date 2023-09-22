package energypa.bems.login.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import energypa.bems.awsS3.domain.entity.GalleryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Data
@ToString(exclude = {"userRoles"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "member_id")
    public Long id;

    @Column
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String password;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private Authority authority;


    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private GalleryEntity gallery;

    public void updateName(String name){
        this.username = username;
    }

    public void updateGallery(GalleryEntity gallery){
        this.gallery = gallery;
    }

}
