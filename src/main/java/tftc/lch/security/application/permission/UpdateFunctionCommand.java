package tftc.lch.security.application.permission;

import java.time.LocalDateTime;

/** 编辑系统功能指令（UC-19）。 */
public record UpdateFunctionCommand(
        int functionId,
        String functionName,
        String description,
        String module,
        LocalDateTime planEffectiveAt,
        LocalDateTime planExpiredAt,
        int operatorId
) {}
