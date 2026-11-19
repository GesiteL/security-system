package tftc.lch.security.presentation.dto.response;

import tftc.lch.security.domain.user.UserRole;
import java.time.LocalDateTime;

/** 用户角色绑定响应 DTO。 */
public class UserRoleResponse {
    private int id;
    private int userId;
    private int roleId;
    private LocalDateTime planEffectiveAt;
    private LocalDateTime planExpiredAt;
    private LocalDateTime createdAt;

    public static UserRoleResponse fromDomain(UserRole ur) {
        UserRoleResponse r = new UserRoleResponse();
        r.id = ur.getId();
        r.userId = ur.getUserId();
        r.roleId = ur.getRoleId();
        r.planEffectiveAt = ur.getPlanEffectiveAt();
        r.planExpiredAt = ur.getPlanExpiredAt();
        r.createdAt = ur.getCreatedAt();
        return r;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getRoleId() { return roleId; }
    public LocalDateTime getPlanEffectiveAt() { return planEffectiveAt; }
    public LocalDateTime getPlanExpiredAt() { return planExpiredAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
