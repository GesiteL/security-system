package tftc.lch.security.application.permission;

import java.time.LocalDateTime;

/** 为角色授予权限指令（UC-22）；含有效期（BR-24）。 */
public record GrantPermissionCommand(
        int roleId,
        int functionId,
        LocalDateTime planEffectiveAt,
        LocalDateTime planExpiredAt,
        int operatorId
) {}
