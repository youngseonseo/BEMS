package energypa.bems.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import energypa.bems.login.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Manager {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "manager_id")
    public Long id;


    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "member_id")
    private Member member;


    public Manager(Member member) {
        this.member = member;
    }
}
