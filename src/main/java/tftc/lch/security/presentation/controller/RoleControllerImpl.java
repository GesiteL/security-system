package tftc.lch.security.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tftc.lch.security.application.role.*;
import tftc.lch.security.domain.role.Role;
import tftc.lch.security.infrastructure.annotation.RequirePermission;
import tftc.lch.security.infrastructure.security.BaseContext;
import tftc.lch.security.presentation.dto.request.*;
import tftc.lch.security.presentation.dto.response.*;
import java.util.stream.Collectors;

@RestController
public class RoleControllerImpl implements RoleController {
    private final RoleApplicationService rs;
    public RoleControllerImpl(RoleApplicationService rs) { this.rs = rs; }
    @Override @RequirePermission("GET:/api/v1/roles")
    public ResponseEntity<PageResponse<RoleResponse>> listRoles(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="20") int pageSize, @RequestParam(required=false) String nameKeyword) {
        GetRoleQuery q = new GetRoleQuery(nameKeyword, (page-1)*pageSize, pageSize);
        var roles = rs.listRoles(q); int total = rs.countRoles(q);
        var recs = roles.stream().map(RoleResponse::fromDomain).collect(Collectors.toList());
        return ResponseEntity.ok(PageResponse.of(recs, total, page, pageSize));
    }
    @Override @RequirePermission("POST:/api/v1/roles")
    public ResponseEntity<RoleResponse> createRole(@RequestBody CreateRoleRequest r) {
        Long op = BaseContext.getCurrentUserId();
        CreateRoleCommand cmd = r.toCommand(op != null ? op.intValue() : 0);
        Role role = rs.createRole(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(RoleResponse.fromDomain(role));
    }
    @Override @RequirePermission("PATCH:/api/v1/roles")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable int roleId, @RequestBody UpdateRoleRequest r) {
        Long op = BaseContext.getCurrentUserId();
        UpdateRoleCommand cmd = r.toCommand(roleId, op != null ? op.intValue() : 0);
        Role role = rs.updateRole(cmd);
        return ResponseEntity.ok(RoleResponse.fromDomain(role));
    }
    @Override @RequirePermission("DELETE:/api/v1/roles")
    public ResponseEntity<Void> deleteRole(@PathVariable int roleId) {
        rs.deleteRole(new DeleteRoleCommand(roleId, 0));
        return ResponseEntity.noContent().build();
    }
}
