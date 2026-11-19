package tftc.lch.security.presentation.dto.request;

import tftc.lch.security.application.permission.AdjustRolePermissionsCommand;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/** 修改角色权限入参 DTO（UC-23）；提交完整新权限集合，系统差异计算后更新。 */
public class AdjustRolePermissionsRequest {

    private List<PermissionItem> permissions;

    public AdjustRolePermissionsCommand toCommand(int roleId, int operatorId) {
        List<AdjustRolePermissionsCommand.PermissionItem> items = permissions == null
                ? List.of()
                : permissions.stream()
                        .map(p -> new AdjustRolePermissionsCommand.PermissionItem(
                                p.getFunctionId(), p.getPlanEffectiveAt(), p.getPlanExpiredAt()))
                        .collect(Collectors.toList());
        return new AdjustRolePermissionsCommand(roleId, items, operatorId);
    }

    public List<PermissionItem> getPermissions() { return permissions; }
    public void setPermissions(List<PermissionItem> permissions) { this.permissions = permissions; }

    public static class PermissionItem {
        private int functionId;
        private LocalDateTime planEffectiveAt;
        private LocalDateTime planExpiredAt;

        public int getFunctionId() { return functionId; }
        public void setFunctionId(int functionId) { this.functionId = functionId; }
        public LocalDateTime getPlanEffectiveAt() { return planEffectiveAt; }
        public void setPlanEffectiveAt(LocalDateTime planEffectiveAt) { this.planEffectiveAt = planEffectiveAt; }
        public LocalDateTime getPlanExpiredAt() { return planExpiredAt; }
        public void setPlanExpiredAt(LocalDateTime planExpiredAt) { this.planExpiredAt = planExpiredAt; }
    }
}
