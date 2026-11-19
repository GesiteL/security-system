package tftc.lch.security.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tftc.lch.security.application.permission.*;
import tftc.lch.security.domain.role.RolePermission;
import tftc.lch.security.infrastructure.annotation.RequirePermission;
import tftc.lch.security.infrastructure.security.BaseContext;
import tftc.lch.security.presentation.dto.request.*;
import tftc.lch.security.presentation.dto.response.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RolePermissionControllerImpl implements RolePermissionController {
    private final PermissionApplicationService ps;
    public RolePermissionControllerImpl(PermissionApplicationService ps) { this.ps = ps; }
    @Override @RequirePermission("GET:/api/v1/role-permissions")
    public ResponseEntity<List<PermissionResponse>> listRolePermissions(@PathVariable int roleId, @RequestParam(required=false) String functionNameKeyword) {
        var list = ps.listRolePermissions(roleId, functionNameKeyword);
        return ResponseEntity.ok(list.stream().map(PermissionResponse::fromDomain).collect(Collectors.toList()));
    }
    @Override @RequirePermission("POST:/api/v1/role-permissions")
    public ResponseEntity<PermissionResponse> grantPermission(@PathVariable int roleId, @RequestBody GrantPermissionRequest r) {
        Long op = BaseContext.getCurrentUserId();
        RolePermission rp = ps.grantPermission(r.toCommand(roleId, op != null ? op.intValue() : 0));
        return ResponseEntity.status(HttpStatus.CREATED).body(PermissionResponse.fromDomain(rp));
    }
    @Override @RequirePermission("PUT:/api/v1/role-permissions")
    public ResponseEntity<List<PermissionResponse>> adjustRolePermissions(@PathVariable int roleId, @RequestBody AdjustRolePermissionsRequest r) {
        Long op = BaseContext.getCurrentUserId();
        var list = ps.adjustRolePermissions(r.toCommand(roleId, op != null ? op.intValue() : 0));
        return ResponseEntity.ok(list.stream().map(PermissionResponse::fromDomain).collect(Collectors.toList()));
    }
    @Override @RequirePermission("DELETE:/api/v1/role-permissions")
    public ResponseEntity<Void> revokePermission(@PathVariable int roleId, @PathVariable int grantId) { ps.revokePermission(new RevokePermissionCommand(roleId, grantId, 0)); return ResponseEntity.noContent().build(); }
}
