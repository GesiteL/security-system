package tftc.lch.security.presentation.dto.response;

import tftc.lch.security.domain.audit.AuditLog;
import java.time.LocalDateTime;

/** 审计日志响应 DTO（改造新增）。 */
public class AuditLogResponse {
    private long id;
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

    public static AuditLogResponse fromDomain(AuditLog a) {
        AuditLogResponse r = new AuditLogResponse();
        r.id = a.getId();
        r.operatorId = a.getOperatorId();
        r.operatorName = a.getOperatorName();
        r.action = a.getAction();
        r.targetType = a.getTargetType();
        r.targetId = a.getTargetId();
        r.targetName = a.getTargetName();
        r.result = a.isResult();
        r.ipAddress = a.getIpAddress();
        r.remark = a.getRemark();
        r.operatedAt = a.getOperatedAt();
        return r;
    }

    public long getId() { return id; }
    public Integer getOperatorId() { return operatorId; }
    public String getOperatorName() { return operatorName; }
    public String getAction() { return action; }
    public String getTargetType() { return targetType; }
    public Integer getTargetId() { return targetId; }
    public String getTargetName() { return targetName; }
    public boolean isResult() { return result; }
    public String getIpAddress() { return ipAddress; }
    public String getRemark() { return remark; }
    public LocalDateTime getOperatedAt() { return operatedAt; }
}
