package tftc.lch.security.domain.role;

import java.time.LocalDateTime;

/** 角色权限授予实体（含有效期 BR-24，软删除 BR-25）。 */
public class RolePermission {

    private int id;
    private int roleId;
    private int permissionId;
    private LocalDateTime planEffectiveAt;
    private LocalDateTime planExpiredAt;
    private boolean isDeleted;
    private long deletedToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RolePermission() {}

    /** 撤销权限授予（软删除 BR-25）。见 {@code UC-24#M5}。 */
    public void revoke() {
        this.isDeleted = true;
        this.deletedToken = id;
        this.updatedAt = LocalDateTime.now();
    }

    /** 更新有效期（BR-24）。见 {@code UC-23#M5}。 */
    public void updatePeriod(LocalDateTime effectiveAt, LocalDateTime expiredAt) {
        this.planEffectiveAt = effectiveAt;
        this.planExpiredAt = expiredAt;
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
    public int getPermissionId() { return permissionId; }
    public void setPermissionId(int permissionId) { this.permissionId = permissionId; }
    public LocalDateTime getPlanEffectiveAt() { return planEffectiveAt; }
    public void setPlanEffectiveAt(LocalDateTime planEffectiveAt) { this.planEffectiveAt = planEffectiveAt; }
    public LocalDateTime getPlanExpiredAt() { return planExpiredAt; }
    public void setPlanExpiredAt(LocalDateTime planExpiredAt) { this.planExpiredAt = planExpiredAt; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
    public long getDeletedToken() { return deletedToken; }
    public void setDeletedToken(long v) { deletedToken = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
