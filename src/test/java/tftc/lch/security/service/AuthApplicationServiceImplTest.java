package tftc.lch.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import tftc.lch.security.application.auth.*;
import tftc.lch.security.domain.shared.DomainException;
import tftc.lch.security.domain.user.*;
import tftc.lch.security.infrastructure.security.JwtUtils;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthApplicationServiceImplTest {
    @Mock UserRepository userRepo;
    @Mock PasswordResetRepository prRepo;
    @Mock AccountLockRepository alRepo;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtUtils jwtUtils;
    @InjectMocks AuthApplicationServiceImpl service;

    @Test void registerSuccess() {
        when(userRepo.existsByAccount("newuser")).thenReturn(false);
        when(passwordEncoder.encode("pass")).thenReturn("hash");
        when(userRepo.save(any())).thenReturn(1);
        User u = service.register(new RegisterCommand("newuser","pass","Name","13900000001","e@t.com"));
        assertEquals("newuser", u.getAccount());
    }

    @Test void registerDuplicate() {
        when(userRepo.existsByAccount("dup")).thenReturn(true);
        assertThrows(DomainException.class, () -> service.register(new RegisterCommand("dup","p","n","139","e")));
    }

    @Test void loginSuccess() {
        User user = User.create("admin", "hash", "Admin", "13800000000", "a@t.com");
        user.setId(1);
        lenient().when(alRepo.findByAccount("admin")).thenReturn(Optional.empty());
        when(userRepo.findByAccount("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("admin", "hash")).thenReturn(true);
        when(jwtUtils.generateToken(1L, 1L)).thenReturn("token123");
        assertEquals("token123", service.login(new LoginCommand("admin", "admin")));
    }

    @Test void loginFailWrongPassword() {
        User user = User.create("admin", "hash", "Admin", "13800000000", "a@t.com");
        lenient().when(alRepo.findByAccount("admin")).thenReturn(Optional.empty());
        when(userRepo.findByAccount("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hash")).thenReturn(false);
        assertThrows(DomainException.class, () -> service.login(new LoginCommand("admin", "wrong")));
    }

    @Test void logoutIncrementsCredentialsVersion() {
        User user = User.create("admin", "hash", "Admin", "13800000000", "a@t.com");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        service.logout(new LogoutCommand(1));
        assertEquals(2, user.getCredentialsVersion());
    }

    @Test void logoutUserNotFoundDoesNothing() {
        when(userRepo.findById(99)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> service.logout(new LogoutCommand(99)));
    }

    @Test void loginUserNotFound() {
        when(userRepo.findByAccount("ghost")).thenReturn(Optional.empty());
        when(alRepo.findByAccount("ghost")).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> service.login(new LoginCommand("ghost", "p")));
        verify(alRepo).save(any());
    }

    @Test void loginUserDeleted() {
        User user = User.create("del", "hash", "Del", "13800000000", "a@t.com");
        user.softDelete();
        when(userRepo.findByAccount("del")).thenReturn(Optional.of(user));
        when(alRepo.findByAccount("del")).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> service.login(new LoginCommand("del", "p")));
    }

    @Test void loginUserDisabled() {
        User user = User.create("dis", "hash", "Dis", "13800000000", "a@t.com");
        user.setStatus(0);
        when(userRepo.findByAccount("dis")).thenReturn(Optional.of(user));
        when(alRepo.findByAccount("dis")).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> service.login(new LoginCommand("dis", "p")));
    }

    @Test void loginFailWithExistingLock() {
        User user = User.create("admin", "hash", "Admin", "13800000000", "a@t.com");
        when(userRepo.findByAccount("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hash")).thenReturn(false);
        when(alRepo.findByAccount("admin")).thenReturn(Optional.of(AccountLock.create("admin")));
        assertThrows(DomainException.class, () -> service.login(new LoginCommand("admin", "wrong")));
        verify(alRepo).save(any());
    }

    @Test void sendResetCodeSuccess() {
        when(prRepo.findLatestByContact("phone", "138")).thenReturn(Optional.empty());
        service.sendPasswordResetCode(new ForgotPasswordCommand("phone", "138"));
        verify(prRepo).save(any());
    }

    @Test void sendResetCodeTooFrequent() {
        PasswordReset recent = PasswordReset.create("phone", "138", "123456",
                java.time.LocalDateTime.now().plusMinutes(5), "");
        when(prRepo.findLatestByContact("phone", "138")).thenReturn(Optional.of(recent));
        assertThrows(DomainException.class,
                () -> service.sendPasswordResetCode(new ForgotPasswordCommand("phone", "138")));
        verify(prRepo, never()).save(any());
    }

    @Test void sendResetCodeAllowedAfterInterval() {
        PasswordReset old = PasswordReset.create("phone", "138", "123456",
                java.time.LocalDateTime.now(), "");
        old.setExpiresAt(java.time.LocalDateTime.now());
        when(prRepo.findLatestByContact("phone", "138")).thenReturn(Optional.of(old));
        java.time.LocalDateTime twoMinAgo = java.time.LocalDateTime.now().minusMinutes(2);
        try {
            var f = PasswordReset.class.getDeclaredField("lastSentAt");
            f.setAccessible(true);
            f.set(old, twoMinAgo);
        } catch (Exception ignored) { }
        service.sendPasswordResetCode(new ForgotPasswordCommand("phone", "138"));
        verify(prRepo).save(any());
    }

    @Test void resetPasswordSuccessPhone() {
        PasswordReset pr = PasswordReset.create("phone", "138", "123456",
                java.time.LocalDateTime.now().plusMinutes(5), "");
        User user = User.create("u", "hash", "U", "138", "e@t.com");
        when(prRepo.findValidCode("138", "123456")).thenReturn(Optional.of(pr));
        when(userRepo.findByPhone("138")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("newhash");
        service.resetPassword(new ResetPasswordCommand("phone", "138", "123456", "newpass", "newpass"));
        assertEquals("newhash", user.getPasswordHash());
        assertTrue(pr.isUsed());
        verify(userRepo).update(user);
    }

    @Test void resetPasswordSuccessEmail() {
        PasswordReset pr = PasswordReset.create("email", "e@t.com", "123456",
                java.time.LocalDateTime.now().plusMinutes(5), "");
        User user = User.create("u", "hash", "U", "138", "e@t.com");
        when(prRepo.findValidCode("e@t.com", "123456")).thenReturn(Optional.of(pr));
        when(userRepo.findByEmail("e@t.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("newhash");
        service.resetPassword(new ResetPasswordCommand("email", "e@t.com", "123456", "newpass", "newpass"));
        assertEquals("newhash", user.getPasswordHash());
    }

    @Test void resetPasswordInvalidCode() {
        when(prRepo.findValidCode("138", "000000")).thenReturn(Optional.empty());
        assertThrows(DomainException.class,
                () -> service.resetPassword(new ResetPasswordCommand("phone", "138", "000000", "p", "p")));
    }

    @Test void resetPasswordUserNotFound() {
        PasswordReset pr = PasswordReset.create("phone", "138", "123456",
                java.time.LocalDateTime.now().plusMinutes(5), "");
        when(prRepo.findValidCode("138", "123456")).thenReturn(Optional.of(pr));
        when(userRepo.findByPhone("138")).thenReturn(Optional.empty());
        assertThrows(DomainException.class,
                () -> service.resetPassword(new ResetPasswordCommand("phone", "138", "123456", "p", "p")));
    }
}
