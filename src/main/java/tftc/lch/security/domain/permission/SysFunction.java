package tftc.lch.security.domain.permission;

import java.time.LocalDateTime;

/** 系统功能（权限点）聚合根（软删除 BR-25，有效期 BR-24）。 */
public class SysFunction {

    private int id;
    private String functionName;
    private String description;
    private String module;
    private LocalDateTime planEffectiveAt;
    private LocalDateTime planExpiredAt;
    private boolean isDeleted;
    private long deletedToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SysFunction() {}

    /** 工厂方法：注册新系统功能。见 {@code UC-18#M5}。 */
    public static SysFunction create(String functionName, String description, String module,
                                     LocalDateTime planEffectiveAt, LocalDateTime planExpiredAt) {
        SysFunction f = new SysFunction();
        f.functionName = functionName;
        f.description = description;
        f.module = module;
        f.planEffectiveAt = planEffectiveAt != null ? planEffectiveAt : LocalDateTime.now();
        f.planExpiredAt = planExpiredAt;
        f.isDeleted = false;
        f.createdAt = LocalDateTime.now();
        f.updatedAt = LocalDateTime.now();
        return f;
    }

    /** 软删除（BR-25）。见 {@code UC-20#M5}。 */
    public void softDelete() {
        this.isDeleted = true; deletedToken = this.id;
        this.updatedAt = LocalDateTime.now();
    }

    /** 更新系统功能信息。见 {@code UC-19#M5}。 */
    public void updateInfo(String functionName, String description, String module,
                           LocalDateTime planEffectiveAt, LocalDateTime planExpiredAt) {
        if (functionName != null) this.functionName = functionName;
        if (description != null) this.description = description;
        if (module != null) this.module = module;
        if (planEffectiveAt != null) this.planEffectiveAt = planEffectiveAt;
        this.planExpiredAt = planExpiredAt;
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFunctionName() { return functionName; }
    public void setFunctionName(String functionName) { this.functionName = functionName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public LocalDateTime getPlanEffectiveAt() { return planEffectiveAt; }
    public void setPlanEffectiveAt(LocalDateTime planEffectiveAt) { this.planEffectiveAt = planEffectiveAt; }
    public LocalDateTime getPlanExpiredAt() { return planExpiredAt; }
    public void setPlanExpiredAt(LocalDateTime planExpiredAt) { this.planExpiredAt = planExpiredAt; }
    public boolean isDeleted() { return isDeleted; }
    public long getDeletedToken() { return deletedToken; }
    public void setDeletedToken(long v) { deletedToken = v; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
