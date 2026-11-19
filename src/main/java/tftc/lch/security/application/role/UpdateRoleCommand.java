package tftc.lch.security.application.role;

/** 编辑角色指令（UC-11）。 */
public record UpdateRoleCommand(int roleId, String roleName, String description, int operatorId) {}
