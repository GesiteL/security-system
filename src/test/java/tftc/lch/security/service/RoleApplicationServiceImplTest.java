package tftc.lch.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import tftc.lch.security.application.role.*;
import tftc.lch.security.domain.role.*;
import tftc.lch.security.domain.shared.DomainException;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RoleApplicationServiceImplTest {
    @Mock RoleRepository roleRepo;
    @InjectMocks RoleApplicationServiceImpl service;

    @Test void listRoles() {
        when(roleRepo.findAll(any(), anyInt(), anyInt())).thenReturn(List.of(new Role()));
        when(roleRepo.countAll(any())).thenReturn(1);
        assertEquals(1, service.listRoles(new GetRoleQuery(null, 0, 10)).size());
        assertEquals(1, service.countRoles(new GetRoleQuery(null, 0, 10)));
    }

    @Test void createRoleSuccess() {
        when(roleRepo.existsByName("ADMIN")).thenReturn(false);
        when(roleRepo.save(any())).thenReturn(1);
        Role r = service.createRole(new CreateRoleCommand("ADMIN", "Admin", 0));
        assertEquals("ADMIN", r.getRoleName());
    }

    @Test void createRoleDuplicate() {
        when(roleRepo.existsByName("ADMIN")).thenReturn(true);
        assertThrows(DomainException.class, () -> service.createRole(new CreateRoleCommand("ADMIN", "", 0)));
    }

    @Test void deleteRoleSuccess() {
        Role r = new Role();
        r.setRoleName("R");
        when(roleRepo.findById(1)).thenReturn(Optional.of(r));
        when(roleRepo.save(any())).thenReturn(1);
        service.deleteRole(new DeleteRoleCommand(1, 0));
        assertTrue(r.isDeleted());
    }

    @Test void updateRoleSuccess() {
        Role r = Role.create("OLD", "D");
        when(roleRepo.findById(1)).thenReturn(Optional.of(r));
        when(roleRepo.update(any())).thenReturn(1);
        Role result = service.updateRole(new UpdateRoleCommand(1, "NEW", "D2", 0));
        assertEquals("NEW", result.getRoleName());
    }

    @Test void updateRoleNotFound() {
        when(roleRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> service.updateRole(new UpdateRoleCommand(99, "N", "D", 0)));
    }

    @Test void deleteRoleNotFound() {
        when(roleRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> service.deleteRole(new DeleteRoleCommand(99, 0)));
    }
}
