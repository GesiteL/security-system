package tftc.lch.security.application.role;

/** 查询角色列表查询对象（UC-09）。 */
public record GetRoleQuery(String nameKeyword, int page, int pageSize) {}
