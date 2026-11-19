package tftc.lch.security.application.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tftc.lch.security.domain.shared.DomainException;
import tftc.lch.security.domain.user.User;
import tftc.lch.security.domain.user.UserDomainService;
import tftc.lch.security.domain.user.UserRepository;
import java.util.List;

@Service
public class UserApplicationServiceImpl implements UserApplicationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDomainService userDomainService;

    public UserApplicationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDomainService userDomainService) {
        this.userRepository = userRepository; this.passwordEncoder = passwordEncoder; this.userDomainService = userDomainService;
    }
    @Override public List<User> listUsers(GetUserQuery q) {
        return userRepository.findAll(q.accountKeyword(), q.realNameKeyword(), q.phoneKeyword(), q.disabled(), q.page(), q.pageSize());
    }
    @Override public int countUsers(GetUserQuery q) {
        return userRepository.countAll(q.accountKeyword(), q.realNameKeyword(), q.phoneKeyword(), q.disabled());
    }
    @Override public User createUser(CreateUserCommand cmd) {
        userDomainService.checkUniqueAccount(cmd.account());
        if (cmd.phone() != null && !cmd.phone().isEmpty()) userDomainService.checkUniquePhone(cmd.phone(), 0);
        User user = User.create(cmd.account(), passwordEncoder.encode(cmd.password()), cmd.realName(), cmd.phone(), cmd.email());
        userRepository.save(user); return user;
    }
    @Override public User updateUser(UpdateUserCommand cmd) {
        User user = userRepository.findById(cmd.targetUserId()).orElseThrow(() -> new DomainException(2003, "用户不存在"));
        if (cmd.phone() != null && !cmd.phone().isEmpty()) userDomainService.checkUniquePhone(cmd.phone(), cmd.targetUserId());
        user.updateInfo(cmd.realName(), cmd.phone(), cmd.email(), cmd.disabled());
        userRepository.update(user); return user;
    }
    @Override public void deleteUser(DeleteUserCommand cmd) {
        if (cmd.targetUserId() == cmd.operatorId()) throw new DomainException(1010, "不能删除当前登录账号");
        User user = userRepository.findById(cmd.targetUserId()).orElseThrow(() -> new DomainException(2003, "用户不存在"));
        user.softDelete(); user.incrementCredentialsVersion(); userRepository.update(user);
    }
}