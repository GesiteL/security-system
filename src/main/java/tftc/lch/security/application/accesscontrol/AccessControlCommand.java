package tftc.lch.security.application.accesscontrol;

public record AccessControlCommand(String token, String requiredPermission) {}
