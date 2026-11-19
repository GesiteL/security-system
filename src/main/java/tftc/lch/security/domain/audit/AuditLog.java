package tftc.lch.security.domain.audit;

import java.time.LocalDateTime;

/** 审计日志聚合根（改造新增：读取侧实体，对应 audit_log 表）。 */
public class AuditLog {
    private Long id;
    private Integer operatorId;
    private String operatorName;
    private String action;
    private String targetType;
    private Integer targetId;
    private String targetName;
    private boolean result;
    private String ipAddress;
    private String remark;
    private LocalDateTime operatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getOperatorId() { return operatorId; }
    public void setOperatorId(Integer operatorId) { this.operatorId = operatorId; }
    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }
    public Integer getTargetId() { return targetId; }
    public void setTargetId(Integer targetId) { this.targetId = targetId; }
    public String getTargetName() { return targetName; }
    public void setTargetName(String targetName) { this.targetName = targetName; }
    public boolean isResult() { return result; }
    public void setResult(boolean result) { this.result = result; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getOperatedAt() { return operatedAt; }
    public void setOperatedAt(LocalDateTime operatedAt) { this.operatedAt = operatedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
