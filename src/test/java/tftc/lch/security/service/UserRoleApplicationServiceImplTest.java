package tftc.lch.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import tftc.lch.security.application.userrole.*;
import tftc.lch.security.domain.user.*;
import tftc.lch.security.domain.shared.DomainException;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)class UserRoleApplicationServiceImplTest {
    @Mock UserRoleRepository urRepo;
    @Mock UserRepository userRepo;
    @InjectMocks UserRoleApplicationServiceImpl service;

    @Test void listUserRoles() { when(urRepo.findByConditions(anyInt(),any(),any(),any(),any(),anyInt(),anyInt())).thenReturn(List.of()); when(urRepo.countByConditions(anyInt(),any(),any(),any(),any())).thenReturn(0); assertEquals(0,service.listUserRoles(new GetUserRoleQuery(1,null,null,null,null,0,10)).size()); }
    @Test void assignRoleSuccess() { when(urRepo.existsByUserIdAndRoleId(1,2)).thenReturn(false); when(urRepo.save(any())).thenReturn(1); assertNotNull(service.assignRole(new AssignUserRoleCommand(1,2,null,null,0))); }
    @Test void assignRoleDuplicate() { when(urRepo.existsByUserIdAndRoleId(1,2)).thenReturn(true); assertThrows(DomainException.class,()->service.assignRole(new AssignUserRoleCommand(1,2,null,null,0))); }
    @Test void removeUserRole() { UserRole ur = new UserRole(); ur.setUserId(1); when(urRepo.findById(1)).thenReturn(Optional.of(ur)); when(urRepo.save(any())).thenReturn(1); service.removeUserRole(new RemoveUserRoleCommand(1,1,0)); assertTrue(ur.isDeleted()); }

    @Test void removeUserRoleIncrementsUserCredentials() {
        UserRole ur = new UserRole(); ur.setUserId(1);
        User user = User.create("u","h","U","138","e@t.com");
        when(urRepo.findById(1)).thenReturn(Optional.of(ur));
        when(urRepo.update(any())).thenReturn(1);
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        service.removeUserRole(new RemoveUserRoleCommand(1,1,0));
        assertTrue(ur.isDeleted());
        assertEquals(2, user.getCredentialsVersion());
        verify(userRepo).update(user);
    }

    @Test void countUserRoles() { when(urRepo.countByConditions(anyInt(),any(),any(),any(),any())).thenReturn(5); assertEquals(5, service.countUserRoles(new GetUserRoleQuery(1,null,null,null,null,0,10))); }

    @Test void assignRoleWithEffectiveAtAndUserFound() {
        java.time.LocalDateTime t = java.time.LocalDateTime.now();
        User user = User.create("u","h","U","138","e@t.com");
        when(urRepo.existsByUserIdAndRoleId(1,2)).thenReturn(false);
        when(urRepo.save(any())).thenReturn(1);
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        UserRole ur = service.assignRole(new AssignUserRoleCommand(1,2,t,t.plusDays(1),0));
        assertEquals(t, ur.getPlanEffectiveAt());
        assertEquals(2, user.getCredentialsVersion());
        verify(userRepo).update(user);
    }

    @Test void updateUserRoleSuccess() {
        UserRole ur = new UserRole(); ur.setId(1);
        User user = User.create("u","h","U","138","e@t.com");
        java.time.LocalDateTime t = java.time.LocalDateTime.now();
        when(urRepo.findById(1)).thenReturn(Optional.of(ur));
        when(urRepo.update(any())).thenReturn(1);
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        UserRole result = service.updateUserRole(new UpdateUserRoleCommand(1,1,t,t.plusDays(1),0));
        assertEquals(t, result.getPlanEffectiveAt());
        assertEquals(2, user.getCredentialsVersion());
    }

    @Test void updateUserRoleNotFound() {
        when(urRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> service.updateUserRole(new UpdateUserRoleCommand(1,99,null,null,0)));
    }

    @Test void removeUserRoleNotFound() {
        when(urRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> service.removeUserRole(new RemoveUserRoleCommand(1,99,0)));
    }
}
