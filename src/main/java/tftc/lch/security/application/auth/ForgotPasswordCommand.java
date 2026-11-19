package tftc.lch.security.application.auth;

/** 发送密码重置验证码指令（UC-04 第一步）。contactType = phone 或 email。 */
public record ForgotPasswordCommand(String contactType, String contactValue) {}
