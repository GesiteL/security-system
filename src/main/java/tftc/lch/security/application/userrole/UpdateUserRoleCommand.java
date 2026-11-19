package tftc.lch.security.application.userrole;

import java.time.LocalDateTime;

/** 修改用户角色有效期指令（UC-15）。 */
public record UpdateUserRoleCommand(
        int userId,
        int userRoleId,
        LocalDateTime planEffectiveAt,
        LocalDateTime planExpiredAt,
        int operatorId
) {}
