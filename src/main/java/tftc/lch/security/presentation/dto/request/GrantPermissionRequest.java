package tftc.lch.security.presentation.dto.request;

import tftc.lch.security.application.permission.GrantPermissionCommand;
import java.time.LocalDateTime;

/** 为角色授予权限入参 DTO（UC-22）。 */
public class GrantPermissionRequest {
    private int functionId;
    private LocalDateTime planEffectiveAt;
    private LocalDateTime planExpiredAt;

    public GrantPermissionCommand toCommand(int roleId, int operatorId) {
        return new GrantPermissionCommand(roleId, functionId, planEffectiveAt, planExpiredAt, operatorId);
    }

    public int getFunctionId() { return functionId; }
    public void setFunctionId(int functionId) { this.functionId = functionId; }
    public LocalDateTime getPlanEffectiveAt() { return planEffectiveAt; }
    public void setPlanEffectiveAt(LocalDateTime planEffectiveAt) { this.planEffectiveAt = planEffectiveAt; }
    public LocalDateTime getPlanExpiredAt() { return planExpiredAt; }
    public void setPlanExpiredAt(LocalDateTime planExpiredAt) { this.planExpiredAt = planExpiredAt; }
}
