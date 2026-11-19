package tftc.lch.security.application.userrole;

import java.time.LocalDateTime;

/** 为用户分配角色指令（UC-14）；含有效期（BR-24）。 */
public record AssignUserRoleCommand(
        int userId,
        int roleId,
        LocalDateTime planEffectiveAt,
        LocalDateTime planExpiredAt,
        int operatorId
) {}
