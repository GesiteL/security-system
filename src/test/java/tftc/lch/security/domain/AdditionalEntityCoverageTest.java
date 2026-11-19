package tftc.lch.security.domain;

import org.junit.jupiter.api.Test;
import tftc.lch.security.domain.permission.SysFunction;
import tftc.lch.security.domain.role.Role;
import tftc.lch.security.domain.role.RolePermission;
import tftc.lch.security.domain.user.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AdditionalEntityCoverageTest {

    @Test void sysFunctionCreate() {
        SysFunction f = SysFunction.create("n", "d", "m", null, null);
        assertEquals("n", f.getFunctionName()); assertFalse(f.isDeleted());
    }
    @Test void sysFunctionCreateWithEffectiveAt() {
        LocalDateTime t = LocalDateTime.now();
        SysFunction f = SysFunction.create("n", "d", "m", t, t.plusDays(1));
        assertEquals(t, f.getPlanEffectiveAt());
    }
    @Test void sysFunctionSoftDelete() {
        SysFunction f = SysFunction.create("n", "d", "m", null, null); f.setId(5); f.softDelete();
        assertTrue(f.isDeleted()); assertEquals(5L, f.getDeletedToken());
    }
    @Test void sysFunctionUpdateInfo() {
        SysFunction f = SysFunction.create("n", "d", "m", null, null);
        LocalDateTime t = LocalDateTime.now();
        f.updateInfo("n2", "d2", "m2", t, t.plusDays(1));
        assertEquals("n2", f.getFunctionName()); assertEquals("d2", f.getDescription());
    }
    @Test void sysFunctionUpdateInfoNulls() {
        SysFunction f = SysFunction.create("n", "d", "m", null, null);
        f.updateInfo(null, null, null, null, null);
        assertEquals("n", f.getFunctionName());
    }
    @Test void sysFunctionSetters() {
        SysFunction f = new SysFunction(); LocalDateTime t = LocalDateTime.now();
        f.setId(1); f.setFunctionName("n"); f.setDescription("d"); f.setModule("m");
        f.setPlanEffectiveAt(t); f.setPlanExpiredAt(t); f.setDeleted(true); f.setDeletedToken(1L);
        f.setCreatedAt(t); f.setUpdatedAt(t);
        assertEquals(1, f.getId()); assertEquals(t, f.getCreatedAt());
    }

    @Test void roleCreate() { Role r = Role.create("R", "D"); assertEquals("R", r.getRoleName()); assertFalse(r.isDeleted()); }
    @Test void roleSoftDelete() { Role r = Role.create("R", "D"); r.setId(3); r.softDelete(); assertTrue(r.isDeleted()); assertEquals(3L, r.getDeletedToken()); }
    @Test void roleUpdateInfo() { Role r = Role.create("R", "D"); r.updateInfo("R2", "D2"); assertEquals("R2", r.getRoleName()); }
    @Test void roleUpdateInfoNulls() { Role r = Role.create("R", "D"); r.updateInfo(null, null); assertEquals("R", r.getRoleName()); }
    @Test void roleSetters() { Role r = new Role(); LocalDateTime t = LocalDateTime.now(); r.setId(1); r.setRoleName("R"); r.setDescription("D"); r.setDeleted(true); r.setDeletedToken(1L); r.setCreatedAt(t); r.setUpdatedAt(t); assertEquals(1, r.getId()); }

    @Test void rolePermissionRevoke() { RolePermission rp = new RolePermission(); rp.setId(1); rp.revoke(); assertTrue(rp.isDeleted()); }
    @Test void rolePermissionUpdatePeriod() { RolePermission rp = new RolePermission(); LocalDateTime t = LocalDateTime.now(); rp.updatePeriod(t, t.plusDays(1)); assertEquals(t, rp.getPlanEffectiveAt()); }
    @Test void rolePermissionSetters() { RolePermission rp = new RolePermission(); LocalDateTime t = LocalDateTime.now(); rp.setId(1); rp.setRoleId(2); rp.setPermissionId(3); rp.setPlanEffectiveAt(t); rp.setPlanExpiredAt(t); rp.setDeleted(true); rp.setDeletedToken(1L); rp.setCreatedAt(t); rp.setUpdatedAt(t); assertEquals(1, rp.getId()); }
    @Test void rolePermissionAllGetters() {
        RolePermission rp = new RolePermission(); LocalDateTime t = LocalDateTime.now();
        rp.setId(1); rp.setRoleId(2); rp.setPermissionId(3); rp.setPlanEffectiveAt(t); rp.setPlanExpiredAt(t.plusDays(1));
        rp.setDeleted(true); rp.setDeletedToken(9L); rp.setCreatedAt(t); rp.setUpdatedAt(t);
        assertEquals(2, rp.getRoleId()); assertEquals(3, rp.getPermissionId());
        assertEquals(t.plusDays(1), rp.getPlanExpiredAt()); assertTrue(rp.isDeleted());
        assertEquals(9L, rp.getDeletedToken()); assertEquals(t, rp.getCreatedAt()); assertEquals(t, rp.getUpdatedAt());
    }

    @Test void accountLockCreate() { AccountLock a = AccountLock.create("acc"); assertEquals("acc", a.getAccount()); assertEquals(1, a.getFailCount()); }
    @Test void accountLockIncrementFail() { AccountLock a = AccountLock.create("acc"); a.incrementFail(); assertEquals(2, a.getFailCount()); }
    @Test void accountLockLockAfterFive() { AccountLock a = AccountLock.create("acc"); for (int i = 0; i < 4; i++) a.incrementFail(); assertNotNull(a.getLockedUntil()); }
    @Test void accountLockReset() { AccountLock a = AccountLock.create("acc"); a.incrementFail(); a.reset(); assertEquals(0, a.getFailCount()); assertNull(a.getLockedUntil()); }
    @Test void accountLockAllGetters() {
        AccountLock a = AccountLock.create("acc");
        assertEquals(0, a.getId());
        assertNotNull(a.getLastAttemptAt());
        assertNotNull(a.getCreatedAt());
        assertNotNull(a.getUpdatedAt());
    }

    @Test void passwordResetCreate() { PasswordReset p = PasswordReset.create("phone", "138", "123", LocalDateTime.now(), "127.0.0.1"); assertEquals("138", p.getContactValue()); assertFalse(p.isUsed()); }
    @Test void passwordResetMarkUsed() { PasswordReset p = PasswordReset.create("phone", "138", "123", null, null); p.markUsed(); assertTrue(p.isUsed()); }
    @Test void passwordResetSetters() { PasswordReset p = new PasswordReset(); p.setId(1); p.setCode("123"); p.setExpiresAt(LocalDateTime.now()); assertEquals("123", p.getCode()); }
    @Test void passwordResetAllGetters() {
        LocalDateTime t = LocalDateTime.now();
        PasswordReset p = PasswordReset.create("phone", "138", "123", t, "127.0.0.1");
        p.setId(7);
        assertEquals(7, p.getId());
        assertEquals("phone", p.getContactType());
        assertEquals("123", p.getCode());
        assertEquals(t, p.getExpiresAt());
        assertNotNull(p.getLastSentAt());
        assertEquals("127.0.0.1", p.getRequestIp());
        assertNotNull(p.getCreatedAt());
    }

    @Test void userRoleRemove() { UserRole ur = new UserRole(); ur.setId(1); ur.remove(); assertTrue(ur.isDeleted()); }
    @Test void userRoleUpdatePeriod() { UserRole ur = new UserRole(); LocalDateTime t = LocalDateTime.now(); ur.updatePeriod(t, t.plusDays(1)); assertEquals(t, ur.getPlanEffectiveAt()); }
    @Test void userRoleSetters() { UserRole ur = new UserRole(); LocalDateTime t = LocalDateTime.now(); ur.setId(1); ur.setUserId(2); ur.setRoleId(3); ur.setPlanEffectiveAt(t); ur.setPlanExpiredAt(t); ur.setDeleted(true); ur.setDeletedToken(1L); ur.setCreatedAt(t); ur.setUpdatedAt(t); assertEquals(1, ur.getId()); }
    @Test void userRoleAllGetters() {
        UserRole ur = new UserRole(); LocalDateTime t = LocalDateTime.now();
        ur.setId(1); ur.setUserId(2); ur.setRoleId(3); ur.setPlanEffectiveAt(t); ur.setPlanExpiredAt(t.plusDays(1));
        ur.setDeleted(true); ur.setDeletedToken(9L); ur.setCreatedAt(t); ur.setUpdatedAt(t);
        assertEquals(2, ur.getUserId()); assertEquals(3, ur.getRoleId());
        assertEquals(t.plusDays(1), ur.getPlanExpiredAt()); assertTrue(ur.isDeleted());
        assertEquals(9L, ur.getDeletedToken()); assertEquals(t, ur.getCreatedAt()); assertEquals(t, ur.getUpdatedAt());
    }

    @Test void userIdValid() { UserId id = new UserId(5); assertEquals(5, id.value()); }
    @Test void userIdInvalid() { assertThrows(IllegalArgumentException.class, () -> new UserId(0)); }

    @Test void userAllSetters() {
        User u = User.create("a", "h", "N", "138", "e");
        u.setId(1); u.setAccount("a2"); u.setPasswordHash("h2"); u.setRealName("N2"); u.setPhone("139"); u.setEmail("e2");
        u.setStatus(0); u.setCredentialsVersion(2); u.setDeleted(true); u.setDeletedToken(1L);
        LocalDateTime t = LocalDateTime.now(); u.setCreatedAt(t); u.setUpdatedAt(t);
        assertEquals(1, u.getId()); assertEquals("a2", u.getAccount()); assertTrue(u.isDisabled());
    }
    @Test void userUpdateInfoNulls() {
        User u = User.create("a", "h", "N", "138", "e");
        u.updateInfo(null, null, null, null);
        assertEquals("N", u.getRealName());
    }
}
