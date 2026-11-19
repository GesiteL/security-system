package tftc.lch.security.application.permission;

/** 撤销角色权限指令（UC-24）。 */
public record RevokePermissionCommand(int roleId, int grantId, int operatorId) {}
