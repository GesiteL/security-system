package tftc.lch.security.domain.user;

public interface UserDomainService {
    void checkUniqueAccount(String account);
    void checkUniquePhone(String phone, int excludeId);
}
