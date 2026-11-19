package tftc.lch.security.application.userrole;

/** 移除用户角色绑定指令（UC-16）。 */
public record RemoveUserRoleCommand(int userId, int userRoleId, int operatorId) {}
