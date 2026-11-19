package tftc.lch.security.presentation.dto.request;

import tftc.lch.security.application.userrole.UpdateUserRoleCommand;
import java.time.LocalDateTime;

/** 修改用户角色有效期入参 DTO（UC-15）。 */
public class UpdateUserRoleRequest {
    private LocalDateTime planEffectiveAt;
    private LocalDateTime planExpiredAt;

    public UpdateUserRoleCommand toCommand(int userId, int userRoleId, int operatorId) {
        return new UpdateUserRoleCommand(userId, userRoleId, planEffectiveAt, planExpiredAt, operatorId);
    }

    public LocalDateTime getPlanEffectiveAt() { return planEffectiveAt; }
    public void setPlanEffectiveAt(LocalDateTime planEffectiveAt) { this.planEffectiveAt = planEffectiveAt; }
    public LocalDateTime getPlanExpiredAt() { return planExpiredAt; }
    public void setPlanExpiredAt(LocalDateTime planExpiredAt) { this.planExpiredAt = planExpiredAt; }
}
