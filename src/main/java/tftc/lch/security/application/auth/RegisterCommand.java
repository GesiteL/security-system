package tftc.lch.security.application.auth;

/** 用户注册指令（UC-01）。 */
public record RegisterCommand(
        String account,
        String password,
        String realName,
        String phone,
        String email
) {}
