package tftc.lch.security.application.permission;

import java.time.LocalDateTime;

/** 注册系统功能指令（UC-18）。 */
public record RegisterFunctionCommand(
        String functionName,
        String description,
        String module,
        LocalDateTime planEffectiveAt,
        LocalDateTime planExpiredAt,
        int operatorId
) {}
