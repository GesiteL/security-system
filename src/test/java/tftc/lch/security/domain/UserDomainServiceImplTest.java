package tftc.lch.security.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tftc.lch.security.domain.shared.DomainException;
import tftc.lch.security.domain.user.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDomainServiceImplTest {
    @Mock UserRepository userRepo;
    @InjectMocks UserDomainServiceImpl domainService;

    @Test void checkUniqueAccountPass() {
        when(userRepo.existsByAccount("new")).thenReturn(false);
        assertDoesNotThrow(() -> domainService.checkUniqueAccount("new"));
    }

    @Test void checkUniqueAccountFail() {
        when(userRepo.existsByAccount("dup")).thenReturn(true);
        assertThrows(DomainException.class, () -> domainService.checkUniqueAccount("dup"));
    }

    @Test void checkUniquePhonePass() {
        when(userRepo.existsByPhone("13800000000", 1)).thenReturn(false);
        assertDoesNotThrow(() -> domainService.checkUniquePhone("13800000000", 1));
    }

    @Test void checkUniquePhoneFail() {
        when(userRepo.existsByPhone("13800000000", 0)).thenReturn(true);
        assertThrows(DomainException.class, () -> domainService.checkUniquePhone("13800000000", 0));
    }
}
