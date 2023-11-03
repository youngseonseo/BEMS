package energypa.bems.login.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
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

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column
    private String imageUrl;

    @Column
    private Integer building;

    @Column
    private Integer floor;


    public void updateName(String name){
        this.username = username;
    }
    public void updateImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }


}
