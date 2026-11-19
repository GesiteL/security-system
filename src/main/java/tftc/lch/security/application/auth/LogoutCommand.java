package tftc.lch.security.application.auth;

/** 用户登出指令（UC-03）；使 credentials_version 自增（BR-07）。 */
public record LogoutCommand(int userId) {}
