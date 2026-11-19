package tftc.lch.security.application.userrole;

/** 查询用户角色列表查询对象（UC-13）。 */
public record GetUserRoleQuery(
        int userId,
        String displayName,
        String phone,
        String email,
        String roleName,
        int page,
        int pageSize
) {}
