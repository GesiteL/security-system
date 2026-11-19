package tftc.lch.security.domain;

import org.junit.jupiter.api.Test;
import tftc.lch.security.domain.role.*;
import tftc.lch.security.domain.user.*;
import tftc.lch.security.presentation.dto.request.*;
import tftc.lch.security.presentation.dto.response.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EntityCoverageTest {
    @Test void userRoleEntity() { UserRole ur = new UserRole(); ur.setId(1); ur.setUserId(2); ur.setRoleId(3); assertEquals(1,ur.getId()); assertEquals(2,ur.getUserId()); assertEquals(3,ur.getRoleId()); }
    @Test void rolePermissionEntity() { RolePermission rp = new RolePermission(); rp.setId(1); rp.setRoleId(2); rp.setPermissionId(3); assertEquals(1,rp.getId()); assertEquals(2,rp.getRoleId()); assertEquals(3,rp.getPermissionId()); }
    @Test void roleEntity() { Role r = new Role(); r.setRoleName("ADMIN"); assertEquals("ADMIN",r.getRoleName()); r.setDeletedToken(5L); assertEquals(5L,r.getDeletedToken()); }
    @Test void accountLockEntity() { AccountLock a = AccountLock.create("test"); assertEquals("test",a.getAccount()); assertEquals(1,a.getFailCount()); }
    @Test void passwordResetEntity() { PasswordReset p = PasswordReset.create("phone","138","123456",null,""); assertEquals("138",p.getContactValue()); }
    @Test void pageResponse() { PageResponse<String> pr = PageResponse.of(List.of("a","b"),2,1,10); assertEquals(2,pr.getTotal()); }
    @Test void userResponse() { UserResponse ur = UserResponse.fromDomain(User.create("u","h","U","138","e")); assertNotNull(ur); }
    @Test void dtoCoverage() {
        LoginRequest lr = new LoginRequest(); lr.setAccount("admin"); assertEquals("admin",lr.getAccount());
        RegisterRequest rr = new RegisterRequest(); rr.setAccount("u"); rr.setRealName("n"); assertEquals("u",rr.getAccount()); assertEquals("n",rr.getRealName());
        CreateUserRequest cu = new CreateUserRequest(); cu.setAccount("cu"); assertEquals("cu",cu.getAccount());
    }
}
