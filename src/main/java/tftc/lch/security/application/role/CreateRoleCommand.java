package tftc.lch.security.application.role;

/** 创建角色指令（UC-10）。 */
public record CreateRoleCommand(String roleName, String description, int operatorId) {}
