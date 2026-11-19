package tftc.lch.security.domain.user;

import org.springframework.stereotype.Service;
import tftc.lch.security.domain.shared.DomainException;

@Service
public class UserDomainServiceImpl implements UserDomainService {
    private final UserRepository userRepository;
    public UserDomainServiceImpl(UserRepository userRepository) { this.userRepository = userRepository; }
    @Override
    public void checkUniqueAccount(String account) {
        if (userRepository.existsByAccount(account)) throw new DomainException(1001, "账号已存在");
    }
    @Override
    public void checkUniquePhone(String phone, int excludeId) {
        if (userRepository.existsByPhone(phone, excludeId)) throw new DomainException(1004, "手机号已被使用");
    }
}