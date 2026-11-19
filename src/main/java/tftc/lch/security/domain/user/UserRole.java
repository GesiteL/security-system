package tftc.lch.security.domain.user;

import java.time.LocalDateTime;

/** 用户角色绑定实体（含有效期 BR-24，软删除 BR-25）。 */
public class UserRole {

    private int id;
    private int userId;
    private int roleId;
    private LocalDateTime planEffectiveAt;
    private LocalDateTime planExpiredAt;
    private boolean isDeleted;
    private long deletedToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserRole() {}

    /** 移除角色绑定（软删除 BR-25）。见 {@code UC-16#M5}。 */
    public void remove() {
        this.isDeleted = true;
        this.updatedAt = LocalDateTime.now();
    }

    /** 更新有效期（BR-24）。见 {@code UC-15#M5}。 */
    public void updatePeriod(LocalDateTime effectiveAt, LocalDateTime expiredAt) {
        this.planEffectiveAt = effectiveAt;
        this.planExpiredAt = expiredAt;
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
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
