package tftc.lch.security.application.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tftc.lch.security.domain.shared.DomainException;
import tftc.lch.security.domain.user.*;
import tftc.lch.security.infrastructure.security.JwtUtils;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthApplicationServiceImpl implements AuthApplicationService {
    private static final Logger log = LoggerFactory.getLogger(AuthApplicationServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final AccountLockRepository accountLockRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthApplicationServiceImpl(UserRepository ur, PasswordResetRepository pr, AccountLockRepository al, PasswordEncoder pe, JwtUtils jw) {
        this.userRepository = ur; this.passwordResetRepository = pr; this.accountLockRepository = al; this.passwordEncoder = pe; this.jwtUtils = jw;
    }

    @Override
    public User register(RegisterCommand cmd) {
        if (userRepository.existsByAccount(cmd.account())) throw new DomainException(2001, "账号已存在");
        User user = User.create(cmd.account(), passwordEncoder.encode(cmd.password()), cmd.realName(), cmd.phone(), cmd.email());
        userRepository.save(user);
        return user;
    }

    @Override
    public String login(LoginCommand cmd) {
        Optional<AccountLock> lockOpt = Optional.empty(); // accountLockRepository.findByAccount(cmd.account());
        
        Optional<User> userOpt = userRepository.findByAccount(cmd.account());
        if (userOpt.isEmpty() || userOpt.get().isDeleted() || userOpt.get().isDisabled()) { incrementFail(cmd.account()); throw new DomainException(2002, "账号或密码错误"); }
        User user = userOpt.get();
        if (!passwordEncoder.matches(cmd.password(), user.getPasswordHash())) { incrementFail(cmd.account()); throw new DomainException(2002, "账号或密码错误"); }
        // accountLockRepository.resetByAccount(cmd.account());
        return jwtUtils.generateToken((long) user.getId(), (long) user.getCredentialsVersion());
    }

    @Override
    public void logout(LogoutCommand cmd) {
        userRepository.findById(cmd.userId()).ifPresent(u -> { u.incrementCredentialsVersion(); userRepository.update(u); });
    }

    @Override
    public void sendPasswordResetCode(ForgotPasswordCommand cmd) {
        Optional<PasswordReset> latest = passwordResetRepository.findLatestByContact(cmd.contactType(), cmd.contactValue());
        if (latest.isPresent() && latest.get().getLastSentAt().plusMinutes(1).isAfter(LocalDateTime.now())) throw new DomainException(3002, "发送频率过快");
        String code = String.format("%06d", new Random().nextInt(1000000));
        PasswordReset pr = PasswordReset.create(cmd.contactType(), cmd.contactValue(), code, LocalDateTime.now().plusMinutes(5), "");
        passwordResetRepository.save(pr);
    }

    @Override
    public void resetPassword(ResetPasswordCommand cmd) {
        Optional<PasswordReset> valid = passwordResetRepository.findValidCode(cmd.contactValue(), cmd.verifyCode());
        if (valid.isEmpty()) throw new DomainException(4001, "验证码无效");
        Optional<User> userOpt = "phone".equals(cmd.contactType()) ? userRepository.findByPhone(cmd.contactValue()) : userRepository.findByEmail(cmd.contactValue());
        if (userOpt.isEmpty()) throw new DomainException(4002, "用户不存在");
        User user = userOpt.get();
        user.setPasswordHash(passwordEncoder.encode(cmd.newPassword()));
        user.incrementCredentialsVersion();
        userRepository.update(user);
        valid.get().markUsed();
        passwordResetRepository.save(valid.get());
    }

    private void incrementFail(String account) {
        Optional<AccountLock> opt = accountLockRepository.findByAccount(account);
        AccountLock lock = opt.isPresent() ? opt.get() : AccountLock.create(account);
        lock.incrementFail();
        accountLockRepository.save(lock);
    }
}
