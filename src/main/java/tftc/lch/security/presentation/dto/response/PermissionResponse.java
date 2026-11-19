package tftc.lch.security.presentation.dto.response;

import tftc.lch.security.domain.role.RolePermission;
import java.time.LocalDateTime;

/** 角色权限授予响应 DTO。 */
public class PermissionResponse {
    private int id;
    private int roleId;
    private int permissionId;
    private LocalDateTime planEffectiveAt;
    private LocalDateTime planExpiredAt;
    private LocalDateTime createdAt;

    public static PermissionResponse fromDomain(RolePermission rp) {
        PermissionResponse r = new PermissionResponse();
        r.id = rp.getId();
        r.roleId = rp.getRoleId();
        r.permissionId = rp.getPermissionId();
        r.planEffectiveAt = rp.getPlanEffectiveAt();
        r.planExpiredAt = rp.getPlanExpiredAt();
        r.createdAt = rp.getCreatedAt();
        return r;
    }

    public int getId() { return id; }
    public int getRoleId() { return roleId; }
    public int getPermissionId() { return permissionId; }
    public LocalDateTime getPlanEffectiveAt() { return planEffectiveAt; }
    public LocalDateTime getPlanExpiredAt() { return planExpiredAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
