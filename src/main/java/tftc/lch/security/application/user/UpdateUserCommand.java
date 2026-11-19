package tftc.lch.security.application.user;

/** 编辑用户信息指令（UC-07）。 */
public record UpdateUserCommand(
        int targetUserId,
        String realName,
        String phone,
        String email,
        Boolean disabled,
        int operatorId
) {}
