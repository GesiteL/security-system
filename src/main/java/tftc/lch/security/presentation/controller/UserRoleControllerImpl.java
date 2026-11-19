package tftc.lch.security.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tftc.lch.security.application.userrole.*;
import tftc.lch.security.domain.user.UserRole;
import tftc.lch.security.infrastructure.annotation.RequirePermission;
import tftc.lch.security.infrastructure.security.BaseContext;
import tftc.lch.security.presentation.dto.request.*;
import tftc.lch.security.presentation.dto.response.*;
import java.util.stream.Collectors;

@RestController
public class UserRoleControllerImpl implements UserRoleController {
    private final UserRoleApplicationService s;
    public UserRoleControllerImpl(UserRoleApplicationService s) { this.s = s; }
    @Override @RequirePermission("GET:/api/v1/user-roles")
    public ResponseEntity<PageResponse<UserRoleResponse>> listUserRoles(@PathVariable int userId, @RequestParam(required=false) String displayName, @RequestParam(required=false) String phone, @RequestParam(required=false) String email, @RequestParam(required=false) String roleName, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int pageSize) {
        GetUserRoleQuery q = new GetUserRoleQuery(userId, displayName, phone, email, roleName, page*pageSize, pageSize);
        var list = s.listUserRoles(q); int total = s.countUserRoles(q);
        var recs = list.stream().map(UserRoleResponse::fromDomain).collect(Collectors.toList());
        return ResponseEntity.ok(PageResponse.of(recs, total, page, pageSize));
    }
    @Override @RequirePermission("POST:/api/v1/user-roles")
    public ResponseEntity<UserRoleResponse> assignRole(@PathVariable int userId, @RequestBody AssignRoleRequest r) {
        Long op = BaseContext.getCurrentUserId();
        UserRole ur = s.assignRole(r.toCommand(userId, op != null ? op.intValue() : 0));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserRoleResponse.fromDomain(ur));
    }
    @Override @RequirePermission("PATCH:/api/v1/user-roles")
    public ResponseEntity<UserRoleResponse> updateUserRole(@PathVariable int userId, @PathVariable int userRoleId, @RequestBody UpdateUserRoleRequest r) {
        Long op = BaseContext.getCurrentUserId();
        UserRole ur = s.updateUserRole(r.toCommand(userId, userRoleId, op != null ? op.intValue() : 0));
        return ResponseEntity.ok(UserRoleResponse.fromDomain(ur));
    }
    @Override @RequirePermission("DELETE:/api/v1/user-roles")
    public ResponseEntity<Void> removeRole(@PathVariable int userId, @PathVariable int userRoleId) { s.removeUserRole(new RemoveUserRoleCommand(userId, userRoleId, 0)); return ResponseEntity.noContent().build(); }
}
