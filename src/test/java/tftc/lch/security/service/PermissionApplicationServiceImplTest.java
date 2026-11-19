package tftc.lch.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import tftc.lch.security.application.permission.*;
import tftc.lch.security.domain.permission.*;
import tftc.lch.security.domain.role.*;
import tftc.lch.security.domain.shared.DomainException;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PermissionApplicationServiceImplTest {
    @Mock SysFunctionRepository funcRepo;
    @Mock RolePermissionRepository rpRepo;
    @InjectMocks PermissionApplicationServiceImpl service;

    @Test void listFunctions() { when(funcRepo.findAll(any(),any(),anyInt(),anyInt())).thenReturn(List.of()); assertEquals(0,service.listFunctions(new GetFunctionQuery(null,null,0,10)).size()); }
    @Test void registerFunctionSuccess() { when(funcRepo.existsByName(any())).thenReturn(false); when(funcRepo.save(any())).thenReturn(1); assertNotNull(service.registerFunction(new RegisterFunctionCommand("n","d","m",LocalDateTime.now(),null,0))); }
    @Test void registerDuplicate() { when(funcRepo.existsByName(any())).thenReturn(true); assertThrows(DomainException.class,()->service.registerFunction(new RegisterFunctionCommand("n","d","m",null,null,0))); }
    @Test void updateFunctionSuccess() { SysFunction f = SysFunction.create("old","d","m",LocalDateTime.now(),null); when(funcRepo.findById(1)).thenReturn(Optional.of(f)); service.updateFunction(new UpdateFunctionCommand(1,"new","d","m",null,null,0)); assertEquals("new",f.getFunctionName()); }
    @Test void deleteFunctionSuccess() { SysFunction f = SysFunction.create("f","d","m",LocalDateTime.now(),null); when(funcRepo.findById(1)).thenReturn(Optional.of(f)); service.deleteFunction(new DeleteFunctionCommand(1,0)); assertTrue(f.isDeleted()); }
    @Test void listRolePermissions() { when(rpRepo.findByRoleId(1,null)).thenReturn(List.of()); assertEquals(0,service.listRolePermissions(1,null).size()); }

    @Test void countFunctions() { when(funcRepo.countAll(any(),any())).thenReturn(7); assertEquals(7, service.countFunctions(new GetFunctionQuery(null,null,0,10))); }

    @Test void updateFunctionNotFound() {
        when(funcRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> service.updateFunction(new UpdateFunctionCommand(99,"n","d","m",null,null,0)));
    }

    @Test void deleteFunctionNotFound() {
        when(funcRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> service.deleteFunction(new DeleteFunctionCommand(99,0)));
    }

    @Test void grantPermissionWithEffectiveAt() {
        LocalDateTime t = LocalDateTime.now();
        when(rpRepo.save(any())).thenReturn(1);
        RolePermission rp = service.grantPermission(new GrantPermissionCommand(1,2,t,t.plusDays(1),0));
        assertEquals(t, rp.getPlanEffectiveAt());
        assertEquals(2, rp.getPermissionId());
    }

    @Test void grantPermissionDefaultEffectiveAt() {
        when(rpRepo.save(any())).thenReturn(1);
        RolePermission rp = service.grantPermission(new GrantPermissionCommand(1,2,null,null,0));
        assertNotNull(rp.getPlanEffectiveAt());
    }

    @Test void adjustRolePermissions() {
        when(rpRepo.findByRoleId(1,null)).thenReturn(List.of(new RolePermission()));
        var list = service.adjustRolePermissions(new AdjustRolePermissionsCommand(1, List.of(), 0));
        assertEquals(1, list.size());
    }

    @Test void revokePermissionSuccess() {
        RolePermission rp = new RolePermission(); rp.setId(1);
        when(rpRepo.findById(1)).thenReturn(Optional.of(rp));
        when(rpRepo.save(any())).thenReturn(1);
        service.revokePermission(new RevokePermissionCommand(1,1,0));
        assertTrue(rp.isDeleted());
    }

    @Test void revokePermissionNotFound() {
        when(rpRepo.findById(99)).thenReturn(Optional.empty());
        assertThrows(DomainException.class, () -> service.revokePermission(new RevokePermissionCommand(1,99,0)));
    }
}
