package energypa.bems.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ManagerApplyListDto {

    private Long managerId;
    private Long memberId;
    private String memberName;
    private String memberEmail;

}
