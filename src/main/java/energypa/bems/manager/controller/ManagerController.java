package energypa.bems.manager.controller;

import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.manager.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;


@Slf4j
@RestController
@Tag(name="manager", description = "건물 관리자 신청 및 등록")
@RequestMapping("/api")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @Operation(method = "post", summary = "건물 관리자 신청 API")
    @PostMapping("/manager/apply")
    public ResponseEntity<?> managerApply(@CurrentUser UserPrincipal user) {
        return managerService.apply(user);
    }

    @Operation(method = "post", summary = "건물 관리자 등록 API")
    @PostMapping("/manager/enroll")
    public ResponseEntity<?> managerEnroll(@CurrentUser UserPrincipal user, @RequestParam Map<String, String> manager) {
        Long managerId = Long.valueOf(manager.get("managerId"));

        return managerService.enroll(user, managerId);
    }

    @Operation(method = "get", summary = "건물 관리자 신청자들 목록 API")
    @GetMapping("/manager/applyList")
    public ResponseEntity<?> managerApplyList(@CurrentUser UserPrincipal user) {
        return managerService.applyList(user);
    }

}



