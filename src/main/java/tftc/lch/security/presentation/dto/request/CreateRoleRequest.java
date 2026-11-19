package tftc.lch.security.presentation.dto.request;

import tftc.lch.security.application.role.CreateRoleCommand;

/** 创建角色入参 DTO（UC-10）。 */
public class CreateRoleRequest {
    private String roleName;
    private String description;

    public CreateRoleCommand toCommand(int operatorId) {
        return new CreateRoleCommand(roleName, description, operatorId);
    }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
