package tftc.lch.security.application.user;

/** 删除用户指令（UC-08）；operatorId 用于自我删除检查（BR-10）。 */
public record DeleteUserCommand(int targetUserId, int operatorId) {}
