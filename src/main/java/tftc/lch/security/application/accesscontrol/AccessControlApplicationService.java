package tftc.lch.security.application.accesscontrol;

public interface AccessControlApplicationService {
    Long verify(AccessControlCommand cmd);
}
