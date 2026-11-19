package tftc.lch.security.application.user;

/** 查询用户列表查询对象（UC-05）。 */
public record GetUserQuery(
        String accountKeyword,
        String realNameKeyword,
        String phoneKeyword,
        Boolean disabled,
        int page,
        int pageSize
) {}
