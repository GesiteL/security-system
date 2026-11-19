package tftc.lch.security.application.auth;

/** 执行密码重置指令（UC-04 第二步）。见 {@code UC-04#M5}。 */
public record ResetPasswordCommand(
        String contactType,
        String contactValue,
        String verifyCode,
        String newPassword,
        String confirmPassword
) {}
