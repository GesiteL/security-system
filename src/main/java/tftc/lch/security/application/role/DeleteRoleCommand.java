package tftc.lch.security.application.role;

/** 删除角色指令（UC-12）。 */
public record DeleteRoleCommand(int roleId, int operatorId) {}
