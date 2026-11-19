package tftc.lch.security.application.permission;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 修改角色权限指令（UC-23）。
 * 采用全量替换策略：系统根据新旧权限差异新增缺少项、将多余项标记为失效（BR-25）。
 */
public record AdjustRolePermissionsCommand(
        int roleId,
        List<PermissionItem> permissions,
        int operatorId
) {
    /** 单条权限项：功能 ID + 可选有效期（BR-24）。 */
    public record PermissionItem(
            int functionId,
            LocalDateTime planEffectiveAt,
            LocalDateTime planExpiredAt
    ) {}
}
