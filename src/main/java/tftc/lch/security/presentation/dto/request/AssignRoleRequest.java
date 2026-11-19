package tftc.lch.security.presentation.dto.request;

import tftc.lch.security.application.userrole.AssignUserRoleCommand;
import java.time.LocalDateTime;

/** 为用户分配角色入参 DTO（UC-14）。 */
public class AssignRoleRequest {
    private int roleId;
    private LocalDateTime planEffectiveAt;
    private LocalDateTime planExpiredAt;

    public AssignUserRoleCommand toCommand(int userId, int operatorId) {
        return new AssignUserRoleCommand(userId, roleId, planEffectiveAt, planExpiredAt, operatorId);
    }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
    public LocalDateTime getPlanEffectiveAt() { return planEffectiveAt; }
    public void setPlanEffectiveAt(LocalDateTime planEffectiveAt) { this.planEffectiveAt = planEffectiveAt; }
    public LocalDateTime getPlanExpiredAt() { return planExpiredAt; }
    public void setPlanExpiredAt(LocalDateTime planExpiredAt) { this.planExpiredAt = planExpiredAt; }
}
