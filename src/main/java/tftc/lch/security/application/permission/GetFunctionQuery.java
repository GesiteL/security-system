package tftc.lch.security.application.permission;

/** 查询系统功能列表查询对象（UC-17）。 */
public record GetFunctionQuery(String nameKeyword, String module, int page, int pageSize) {}
