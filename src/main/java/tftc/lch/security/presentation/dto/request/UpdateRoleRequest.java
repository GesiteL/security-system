package tftc.lch.security.presentation.dto.request;

import tftc.lch.security.application.role.UpdateRoleCommand;

/** 编辑角色入参 DTO（UC-11）。 */
public class UpdateRoleRequest {
    private String roleName;
    private String description;

    public UpdateRoleCommand toCommand(int roleId, int operatorId) {
        return new UpdateRoleCommand(roleId, roleName, description, operatorId);
    }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
