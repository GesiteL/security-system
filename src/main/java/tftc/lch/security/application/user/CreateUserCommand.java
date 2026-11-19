package tftc.lch.security.application.user;

/** 管理员创建用户指令（UC-06）。 */
public record CreateUserCommand(
        String account,
        String password,
        String realName,
        String phone,
        String email,
        int operatorId
) {}
