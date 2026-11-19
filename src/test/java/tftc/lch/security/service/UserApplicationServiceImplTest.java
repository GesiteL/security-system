package tftc.lch.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import tftc.lch.security.application.user.*;
import tftc.lch.security.domain.shared.DomainException;
import tftc.lch.security.domain.user.*;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)class UserApplicationServiceImplTest {
    @Mock UserRepository userRepo;
    @Mock PasswordEncoder passwordEncoder;
    @Mock UserDomainService domainService;
    @InjectMocks UserApplicationServiceImpl service;

    @Test void createUserSuccess() {
        when(passwordEncoder.encode("pass")).thenReturn("hash");
        when(userRepo.save(any())).thenReturn(1);
        User u = service.createUser(new CreateUserCommand("u1","pass","U1","13900000001","u@t.com",0));
        assertEquals("u1", u.getAccount());
    }

    @Test void createUserCallsDomainService() {
        doNothing().when(domainService).checkUniqueAccount("u1");
        when(passwordEncoder.encode(any())).thenReturn("h");
        when(userRepo.save(any())).thenReturn(1);
        service.createUser(new CreateUserCommand("u1","p","U1","13900000001","u@t.com",0));
        verify(domainService).checkUniqueAccount("u1");
    }

    @Test void listUsers() {
        when(userRepo.findAll(any(),any(),any(),any(),anyInt(),anyInt()))
            .thenReturn(List.of(User.create("a","h","A","138","e")));
        GetUserQuery q = new GetUserQuery(null,null,null,null,0,10);
        assertEquals(1, service.listUsers(q).size());
    }

    @Test void updateUserSuccess() {
        User user = User.create("old","hash","Old","13800000000","o@t.com");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(userRepo.update(any())).thenReturn(1);
        User u = service.updateUser(new UpdateUserCommand(1,"New","13900000001","n@t.com",false,0));
        assertEquals("New", u.getRealName());
    }

    @Test void deleteUserSelf() {
        assertThrows(DomainException.class, () -> service.deleteUser(new DeleteUserCommand(1, 1)));
    }

    @Test void deleteUserSuccess() {
        User user = User.create("u1","h","U1","138","e@t.com");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(userRepo.update(any())).thenReturn(1);
        service.deleteUser(new DeleteUserCommand(1, 0));
        assertTrue(user.isDeleted());
    }

    @Test void countUsers() {
        when(userRepo.countAll(any(),any(),any(),any())).thenReturn(8);
        assertEquals(8, service.countUsers(new GetUserQuery(null,null,null,null,0,10)));
    }

    @Test void createUserWithPhoneChecksUnique() {
        when(passwordEncoder.encode(any())).thenReturn("h");
        when(userRepo.save(any())).thenReturn(1);
        service.createUser(new CreateUserCommand("u1","p","U1","13900000001","u@t.com",0));
        verify(domainService).checkUniquePhone("13900000001", 0);
    }

    @Test void createUserWithoutPhoneSkipsPhoneCheck() {
        when(passwordEncoder.encode(any())).thenReturn("h");
        when(userRepo.save(any())).thenReturn(1);
        service.createUser(new CreateUserCommand("u1","p","U1","","u@t.com",0));
        verify(domainService, never()).checkUniquePhone(any(), anyInt());
    }

    @Test void createUserWithNullPhoneSkipsPhoneCheck() {
        when(passwordEncoder.encode(any())).thenReturn("h");
        when(userRepo.save(any())).thenReturn(1);
        service.createUser(new CreateUserCommand("u1","p","U1",null,"u@t.com",0));
        verify(domainService, never()).checkUniquePhone(any(), anyInt());
    }

    @Test void updateUserWithNullPhoneSkipsPhoneCheck() {
        User user = User.create("old","hash","Old","13800000000","o@t.com");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(userRepo.update(any())).thenReturn(1);
        service.updateUser(new UpdateUserCommand(1,"New",null,"n@t.com",false,0));
        verify(domainService, never()).checkUniquePhone(any(), anyInt());
    }

    @Test void updateUserWithEmptyPhoneSkipsPhoneCheck() {
        User user = User.create("old","hash","Old","13800000000","o@t.com");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(userRepo.update(any())).thenReturn(1);
        service.updateUser(new UpdateUserCommand(1,"New","","n@t.com",false,0));
        verify(domainService, never()).checkUniquePhone(any(), anyInt());
    }

    @Test void updateUserNotFound() {
        when(userRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(DomainException.class,
                () -> service.updateUser(new UpdateUserCommand(99,"N","138","e",false,0)));
    }

    @Test void updateUserWithPhoneChecksUnique() {
        User user = User.create("old","hash","Old","13800000000","o@t.com");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(userRepo.update(any())).thenReturn(1);
        service.updateUser(new UpdateUserCommand(1,"New","13900000001","n@t.com",false,0));
        verify(domainService).checkUniquePhone("13900000001", 1);
    }

    @Test void deleteUserNotFound() {
        when(userRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> service.deleteUser(new DeleteUserCommand(99, 0)));
    }
}
