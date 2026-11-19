package tftc.lch.security.application.auth;

/** 用户登入指令（UC-02）。 */
public record LoginCommand(String account, String password) {}
