package tftc.lch.security.application.permission;

/** 删除系统功能指令（UC-20）。 */
public record DeleteFunctionCommand(int functionId, int operatorId) {}
