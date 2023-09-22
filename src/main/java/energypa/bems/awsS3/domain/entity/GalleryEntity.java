package energypa.bems.awsS3.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import energypa.bems.login.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "gallery")
@Table(name = "gallery")
public class GalleryEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "gallery_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String filePath;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "member_id")
    private Member member;


    @Builder
    public GalleryEntity(Long id, String filePath,Member member) {
        this.id = id;
        this.filePath = filePath;
        this.member=member;
    }
}
