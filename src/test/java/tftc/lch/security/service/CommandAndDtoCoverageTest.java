package tftc.lch.security.service;

import org.junit.jupiter.api.Test;
import tftc.lch.security.application.accesscontrol.AccessControlCommand;
import tftc.lch.security.application.auth.*;
import tftc.lch.security.application.permission.*;
import tftc.lch.security.application.role.*;
import tftc.lch.security.application.user.*;
import tftc.lch.security.application.userrole.*;
import tftc.lch.security.presentation.dto.request.*;
import tftc.lch.security.presentation.dto.response.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandAndDtoCoverageTest {

    @Test void accessControlCommand() { var c = new AccessControlCommand("t", "p"); assertEquals("t", c.token()); assertEquals("p", c.requiredPermission()); }
    @Test void forgotPasswordCommand() { var c = new ForgotPasswordCommand("phone", "138"); assertEquals("phone", c.contactType()); }
    @Test void loginCommand() { var c = new LoginCommand("a", "p"); assertEquals("a", c.account()); }
    @Test void logoutCommand() { var c = new LogoutCommand(1); assertEquals(1, c.userId()); }
    @Test void registerCommand() { var c = new RegisterCommand("a", "p", "n", "138", "e"); assertEquals("n", c.realName()); }
    @Test void resetPasswordCommand() { var c = new ResetPasswordCommand("phone", "138", "123", "p1", "p1"); assertEquals("123", c.verifyCode()); }
    @Test void adjustRolePermissionsCommand() { var c = new AdjustRolePermissionsCommand(1, List.of(new AdjustRolePermissionsCommand.PermissionItem(1, null, null)), 2); assertEquals(1, c.roleId()); }
    @Test void deleteFunctionCommand() { var c = new DeleteFunctionCommand(1, 2); assertEquals(1, c.functionId()); }
    @Test void grantPermissionCommand() { var c = new GrantPermissionCommand(1, 2, null, null, 3); assertEquals(2, c.functionId()); }
    @Test void registerFunctionCommand() { var c = new RegisterFunctionCommand("f", "d", "m", null, null, 1); assertEquals("f", c.functionName()); }
    @Test void revokePermissionCommand() { var c = new RevokePermissionCommand(1, 2, 3); assertEquals(2, c.grantId()); }
    @Test void updateFunctionCommand() { var c = new UpdateFunctionCommand(1, "f", "d", "m", null, null, 2); assertEquals(1, c.functionId()); }
    @Test void createRoleCommand() { var c = new CreateRoleCommand("r", "d", 1); assertEquals("r", c.roleName()); }
    @Test void deleteRoleCommand() { var c = new DeleteRoleCommand(1, 2); assertEquals(1, c.roleId()); }
    @Test void updateRoleCommand() { var c = new UpdateRoleCommand(1, "r", "d", 2); assertEquals("r", c.roleName()); }
    @Test void createUserCommand() { var c = new CreateUserCommand("a", "p", "n", "138", "e", 1); assertEquals("n", c.realName()); }
    @Test void deleteUserCommand() { var c = new DeleteUserCommand(1, 2); assertEquals(1, c.targetUserId()); }
    @Test void updateUserCommand() { var c = new UpdateUserCommand(1, "n", "138", "e", true, 2); assertTrue(c.disabled()); }
    @Test void assignUserRoleCommand() { var c = new AssignUserRoleCommand(1, 2, null, null, 3); assertEquals(2, c.roleId()); }
    @Test void removeUserRoleCommand() { var c = new RemoveUserRoleCommand(1, 2, 3); assertEquals(2, c.userRoleId()); }
    @Test void updateUserRoleCommand() { var c = new UpdateUserRoleCommand(1, 2, null, null, 3); assertEquals(2, c.userRoleId()); }
    @Test void getFunctionQuery() { var q = new GetFunctionQuery("k", "m", 1, 10); assertEquals(1, q.page()); }
    @Test void getRoleQuery() { var q = new GetRoleQuery("k", 1, 10); assertEquals(10, q.pageSize()); }
    @Test void getUserQuery() { var q = new GetUserQuery("a", "n", "138", true, 1, 10); assertTrue(q.disabled()); }
    @Test void getUserRoleQuery() { var q = new GetUserRoleQuery(1, "n", "138", "e", "r", 1, 10); assertEquals(1, q.userId()); }

    @Test void adjustRolePermissionsRequest() {
        AdjustRolePermissionsRequest r = new AdjustRolePermissionsRequest();
        var item = new AdjustRolePermissionsRequest.PermissionItem();
        item.setFunctionId(1); item.setPlanEffectiveAt(LocalDateTime.now()); item.setPlanExpiredAt(LocalDateTime.now());
        r.setPermissions(List.of(item));
        var cmd = r.toCommand(1, 2);
        assertEquals(1, cmd.roleId()); assertEquals(1, cmd.permissions().size());
        assertNotNull(item.getFunctionId());
    }
    @Test void adjustRolePermissionsRequestNullPermissions() {
        AdjustRolePermissionsRequest r = new AdjustRolePermissionsRequest();
        var cmd = r.toCommand(1, 2);
        assertTrue(cmd.permissions().isEmpty());
    }
    @Test void assignRoleRequest() { AssignRoleRequest r = new AssignRoleRequest(); r.setRoleId(2); r.setPlanEffectiveAt(LocalDateTime.now()); var c = r.toCommand(1, 3); assertEquals(2, c.roleId()); }
    @Test void createRoleRequest() { CreateRoleRequest r = new CreateRoleRequest(); r.setRoleName("R"); r.setDescription("D"); var c = r.toCommand(1); assertEquals("R", c.roleName()); }
    @Test void createUserRequest() { CreateUserRequest r = new CreateUserRequest(); r.setAccount("a"); r.setPassword("p"); r.setRealName("n"); r.setPhone("138"); r.setEmail("e"); var c = r.toCommand(1); assertEquals("a", c.account()); }
    @Test void grantPermissionRequest() { GrantPermissionRequest r = new GrantPermissionRequest(); r.setFunctionId(2); var c = r.toCommand(1, 3); assertEquals(2, c.functionId()); }
    @Test void loginRequest() { LoginRequest r = new LoginRequest(); r.setAccount("a"); r.setPassword("p"); var c = r.toCommand(); assertEquals("a", c.account()); }
    @Test void registerFunctionRequest() { RegisterFunctionRequest r = new RegisterFunctionRequest(); r.setFunctionName("f"); r.setDescription("d"); r.setModule("m"); var c = r.toCommand(1); assertEquals("f", c.functionName()); }
    @Test void registerRequest() { RegisterRequest r = new RegisterRequest(); r.setAccount("a"); r.setPassword("p"); var c = r.toCommand(); assertEquals("a", c.account()); }
    @Test void resetPasswordRequest() { ResetPasswordRequest r = new ResetPasswordRequest(); r.setContactType("phone"); r.setContactValue("138"); var c = r.toCommand(); assertEquals("phone", c.contactType()); }
    @Test void sendResetCodeRequest() { SendResetCodeRequest r = new SendResetCodeRequest(); r.setContactType("phone"); r.setContactValue("138"); var c = r.toCommand(); assertEquals("phone", c.contactType()); }
    @Test void updateFunctionRequest() { UpdateFunctionRequest r = new UpdateFunctionRequest(); r.setFunctionName("f"); var c = r.toCommand(1, 2); assertEquals("f", c.functionName()); }
    @Test void updateRoleRequest() { UpdateRoleRequest r = new UpdateRoleRequest(); r.setRoleName("R"); var c = r.toCommand(1, 2); assertEquals("R", c.roleName()); }
    @Test void updateUserRequest() { UpdateUserRequest r = new UpdateUserRequest(); r.setRealName("N"); r.setDisabled(true); var c = r.toCommand(1, 2); assertTrue(c.disabled()); }
    @Test void updateUserRoleRequest() { UpdateUserRoleRequest r = new UpdateUserRoleRequest(); r.setPlanEffectiveAt(LocalDateTime.now()); var c = r.toCommand(1, 2, 3); assertEquals(2, c.userRoleId()); }

    @Test void functionResponse() {
        var f = tftc.lch.security.domain.permission.SysFunction.create("f", "d", "m", null, null);
        f.setId(1);
        FunctionResponse r = FunctionResponse.fromDomain(f);
        assertEquals(1, r.getId()); assertEquals("f", r.getFunctionName());
    }
    @Test void loginResponse() { LoginResponse r = LoginResponse.of("t", 1, "a"); assertEquals("t", r.getToken()); }
    @Test void pageResponse() { PageResponse<String> r = PageResponse.of(List.of("a"), 1, 1, 10); assertEquals(1, r.getTotal()); }
    @Test void permissionResponse() {
        var rp = new tftc.lch.security.domain.role.RolePermission(); rp.setId(1); rp.setRoleId(2); rp.setPermissionId(3);
        PermissionResponse r = PermissionResponse.fromDomain(rp);
        assertEquals(1, r.getId());
    }
    @Test void roleResponse() {
        var role = tftc.lch.security.domain.role.Role.create("R", "D"); role.setId(1);
        RoleResponse r = RoleResponse.fromDomain(role);
        assertEquals(1, r.getId()); assertEquals("R", r.getRoleName());
    }
    @Test void userResponse() {
        var u = tftc.lch.security.domain.user.User.create("a", "h", "N", "138", "e"); u.setId(1);
        UserResponse r = UserResponse.fromDomain(u);
        assertEquals(1, r.getId()); assertEquals("a", r.getUsername());
    }
    @Test void userRoleResponse() {
        var ur = new tftc.lch.security.domain.user.UserRole(); ur.setId(1); ur.setUserId(2); ur.setRoleId(3);
        UserRoleResponse r = UserRoleResponse.fromDomain(ur);
        assertEquals(1, r.getId());
    }
}
