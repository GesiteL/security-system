package tftc.lch.security.domain.role;

import java.time.LocalDateTime;

/** 角色聚合根（软删除 BR-25）。 */
public class Role {

    private int id;
    private String roleName;
    private String description;
    private boolean isDeleted;
    private long deletedToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Role() {}

    /** 工厂方法：创建新角色。见 {@code UC-10#M5}。 */
    public static Role create(String roleName, String description) {
        Role r = new Role();
        r.roleName = roleName;
        r.description = description;
        r.isDeleted = false;
        r.createdAt = LocalDateTime.now();
        r.updatedAt = LocalDateTime.now();
        return r;
    }

    /** 软删除（BR-25）。见 {@code UC-12#M5}。 */
    public void softDelete() {
        this.isDeleted = true; deletedToken = this.id;
        this.updatedAt = LocalDateTime.now();
    }

    /** 更新角色信息。见 {@code UC-11#M5}。 */
    public void updateInfo(String roleName, String description) {
        if (roleName != null) this.roleName = roleName;
        if (description != null) this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isDeleted() { return isDeleted; }
    public long getDeletedToken() { return deletedToken; }
    public void setDeletedToken(long v) { deletedToken = v; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
