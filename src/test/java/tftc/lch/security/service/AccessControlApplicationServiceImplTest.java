package tftc.lch.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import tftc.lch.security.application.accesscontrol.*;
import tftc.lch.security.domain.permission.*;
import tftc.lch.security.domain.role.*;
import tftc.lch.security.domain.user.*;
import tftc.lch.security.infrastructure.security.JwtUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)class AccessControlApplicationServiceImplTest {
    @Mock JwtUtils jwtUtils;
    @Mock UserRepository userRepo;
    @Mock UserRoleRepository urRepo;
    @Mock RolePermissionRepository rpRepo;
    @Mock SysFunctionRepository funcRepo;
    @InjectMocks AccessControlApplicationServiceImpl service;

    @BeforeEach void stubCredentialsVersion() {
        lenient().when(jwtUtils.getCredentialsVersion(anyString())).thenReturn(1L);
    }

    @Test void verifySuccess() {
        when(jwtUtils.getUserId("token")).thenReturn(1L);
        User user = User.create("admin","h","A","138","e");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        assertEquals(1L, service.verify(new AccessControlCommand("token",null)));
    }
    @Test void verifyWithPermission() {
        when(jwtUtils.getUserId("token")).thenReturn(1L);
        User user = User.create("admin","h","A","138","e");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        UserRole ur = new UserRole(); ur.setRoleId(1);
        when(urRepo.findByConditions(anyInt(),any(),any(),any(),any(),anyInt(),anyInt())).thenReturn(List.of(ur));
        RolePermission rp = new RolePermission(); rp.setPermissionId(1);
        when(rpRepo.findByRoleId(1,null)).thenReturn(List.of(rp));
        SysFunction f = SysFunction.create("用户列表","desc","user:list",LocalDateTime.now(),null);
        when(funcRepo.findById(1)).thenReturn(Optional.of(f));
        assertEquals(1L, service.verify(new AccessControlCommand("token","user:list")));
    }
    @Test void verifyUserDeleted() {
        when(jwtUtils.getUserId("token")).thenReturn(1L);
        User user = User.create("del","h","D","138","e"); user.softDelete();
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class,()->service.verify(new AccessControlCommand("token",null)));
    }

    @Test void verifyUserEmpty() {
        when(jwtUtils.getUserId("token")).thenReturn(1L);
        when(userRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,()->service.verify(new AccessControlCommand("token",null)));
    }

    @Test void verifyUserDisabled() {
        when(jwtUtils.getUserId("token")).thenReturn(1L);
        User user = User.create("dis","h","D","138","e"); user.setStatus(0);
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class,()->service.verify(new AccessControlCommand("token",null)));
    }

    @Test void verifyEmptyPermissionString() {
        when(jwtUtils.getUserId("token")).thenReturn(1L);
        User user = User.create("admin","h","A","138","e");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        assertEquals(1L, service.verify(new AccessControlCommand("token","")));
    }

    @Test void verifyCredentialsVersionMismatch() {
        when(jwtUtils.getUserId("token")).thenReturn(1L);
        User user = User.create("admin","h","A","138","e");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(jwtUtils.getCredentialsVersion("token")).thenReturn(2L);
        assertThrows(RuntimeException.class,()->service.verify(new AccessControlCommand("token",null)));
    }

    @Test void verifyCredentialsVersionNull() {
        when(jwtUtils.getUserId("token")).thenReturn(1L);
        User user = User.create("admin","h","A","138","e");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(jwtUtils.getCredentialsVersion("token")).thenReturn(null);
        assertThrows(RuntimeException.class,()->service.verify(new AccessControlCommand("token",null)));
    }

    @Test void verifyForbiddenNoMatchingPermission() {
        when(jwtUtils.getUserId("token")).thenReturn(1L);
        User user = User.create("admin","h","A","138","e");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        UserRole ur = new UserRole(); ur.setRoleId(1);
        when(urRepo.findByConditions(anyInt(),any(),any(),any(),any(),anyInt(),anyInt())).thenReturn(List.of(ur));
        RolePermission rp = new RolePermission(); rp.setPermissionId(1);
        when(rpRepo.findByRoleId(1,null)).thenReturn(List.of(rp));
        SysFunction f = SysFunction.create("其他权限","desc","other:perm",LocalDateTime.now(),null);
        when(funcRepo.findById(1)).thenReturn(Optional.of(f));
        assertThrows(RuntimeException.class,()->service.verify(new AccessControlCommand("token","user:list")));
    }

    @Test void verifySkipsDeletedRole() {
        when(jwtUtils.getUserId("token")).thenReturn(1L);
        User user = User.create("admin","h","A","138","e");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        UserRole ur = new UserRole(); ur.setRoleId(1); ur.remove();
        when(urRepo.findByConditions(anyInt(),any(),any(),any(),any(),anyInt(),anyInt())).thenReturn(List.of(ur));
        assertThrows(RuntimeException.class,()->service.verify(new AccessControlCommand("token","user:list")));
        verify(rpRepo, never()).findByRoleId(anyInt(), any());
    }

    @Test void verifySkipsDeletedPermission() {
        when(jwtUtils.getUserId("token")).thenReturn(1L);
        User user = User.create("admin","h","A","138","e");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        UserRole ur = new UserRole(); ur.setRoleId(1);
        when(urRepo.findByConditions(anyInt(),any(),any(),any(),any(),anyInt(),anyInt())).thenReturn(List.of(ur));
        RolePermission rp = new RolePermission(); rp.setPermissionId(1); rp.revoke();
        when(rpRepo.findByRoleId(1,null)).thenReturn(List.of(rp));
        assertThrows(RuntimeException.class,()->service.verify(new AccessControlCommand("token","user:list")));
        verify(funcRepo, never()).findById(anyInt());
    }

    @Test void verifySkipsDeletedFunction() {
        when(jwtUtils.getUserId("token")).thenReturn(1L);
        User user = User.create("admin","h","A","138","e");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        UserRole ur = new UserRole(); ur.setRoleId(1);
        when(urRepo.findByConditions(anyInt(),any(),any(),any(),any(),anyInt(),anyInt())).thenReturn(List.of(ur));
        RolePermission rp = new RolePermission(); rp.setPermissionId(1);
        when(rpRepo.findByRoleId(1,null)).thenReturn(List.of(rp));
        SysFunction f = SysFunction.create("用户列表","desc","user:list",LocalDateTime.now(),null); f.softDelete();
        when(funcRepo.findById(1)).thenReturn(Optional.of(f));
        assertThrows(RuntimeException.class,()->service.verify(new AccessControlCommand("token","user:list")));
    }

    @Test void verifyFunctionNotFound() {
        when(jwtUtils.getUserId("token")).thenReturn(1L);
        User user = User.create("admin","h","A","138","e");
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        UserRole ur = new UserRole(); ur.setRoleId(1);
        when(urRepo.findByConditions(anyInt(),any(),any(),any(),any(),anyInt(),anyInt())).thenReturn(List.of(ur));
        RolePermission rp = new RolePermission(); rp.setPermissionId(1);
        when(rpRepo.findByRoleId(1,null)).thenReturn(List.of(rp));
        when(funcRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,()->service.verify(new AccessControlCommand("token","user:list")));
    }
}
