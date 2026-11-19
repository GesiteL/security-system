package tftc.lch.security.service;

import org.junit.jupiter.api.Test;
import tftc.lch.security.presentation.dto.request.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class AdditionalDTOTest {
    private final LocalDateTime t = LocalDateTime.now();

    @Test void assignRoleRequest() {
        AssignRoleRequest r = new AssignRoleRequest();
        r.setRoleId(2); r.setPlanEffectiveAt(t); r.setPlanExpiredAt(t);
        assertEquals(2, r.getRoleId());
        assertEquals(t, r.getPlanEffectiveAt());
        assertEquals(t, r.getPlanExpiredAt());
        assertEquals(2, r.toCommand(1, 3).roleId());
    }

    @Test void updateRoleRequest() {
        UpdateRoleRequest r = new UpdateRoleRequest();
        r.setRoleName("R"); r.setDescription("d");
        assertEquals("R", r.getRoleName());
        assertEquals("d", r.getDescription());
        assertEquals("R", r.toCommand(1, 2).roleName());
    }

    @Test void createRoleRequest() {
        CreateRoleRequest r = new CreateRoleRequest();
        r.setRoleName("R"); r.setDescription("d");
        assertEquals("R", r.getRoleName());
        assertEquals("d", r.getDescription());
        assertEquals("R", r.toCommand(1).roleName());
    }

    @Test void registerFunctionRequest() {
        RegisterFunctionRequest r = new RegisterFunctionRequest();
        r.setFunctionName("f"); r.setDescription("d"); r.setModule("M");
        r.setPlanEffectiveAt(t); r.setPlanExpiredAt(t);
        assertEquals("f", r.getFunctionName());
        assertEquals("d", r.getDescription());
        assertEquals("M", r.getModule());
        assertEquals(t, r.getPlanEffectiveAt());
        assertEquals(t, r.getPlanExpiredAt());
        assertEquals("f", r.toCommand(1).functionName());
    }

    @Test void updateFunctionRequest() {
        UpdateFunctionRequest r = new UpdateFunctionRequest();
        r.setFunctionName("f"); r.setDescription("d"); r.setModule("M");
        r.setPlanEffectiveAt(t); r.setPlanExpiredAt(t);
        assertEquals("f", r.getFunctionName());
        assertEquals("d", r.getDescription());
        assertEquals("M", r.getModule());
        assertEquals(t, r.getPlanEffectiveAt());
        assertEquals(t, r.getPlanExpiredAt());
        assertEquals("f", r.toCommand(1, 2).functionName());
    }

    @Test void grantPermissionRequest() {
        GrantPermissionRequest r = new GrantPermissionRequest();
        r.setFunctionId(5); r.setPlanEffectiveAt(t); r.setPlanExpiredAt(t);
        assertEquals(5, r.getFunctionId());
        assertEquals(t, r.getPlanEffectiveAt());
        assertEquals(t, r.getPlanExpiredAt());
        assertEquals(5, r.toCommand(1, 2).functionId());
    }

    @Test void updateUserRoleRequest() {
        UpdateUserRoleRequest r = new UpdateUserRoleRequest();
        r.setPlanEffectiveAt(t); r.setPlanExpiredAt(t);
        assertEquals(t, r.getPlanEffectiveAt());
        assertEquals(t, r.getPlanExpiredAt());
        assertEquals(2, r.toCommand(1, 2, 3).userRoleId());
    }

    @Test void createUserRequest() {
        CreateUserRequest r = new CreateUserRequest();
        r.setAccount("a"); r.setPassword("p"); r.setRealName("N"); r.setPhone("138"); r.setEmail("e@x.com");
        assertEquals("a", r.getAccount());
        assertEquals("p", r.getPassword());
        assertEquals("N", r.getRealName());
        assertEquals("138", r.getPhone());
        assertEquals("e@x.com", r.getEmail());
        assertEquals("a", r.toCommand(1).account());
    }

    @Test void updateUserRequest() {
        UpdateUserRequest r = new UpdateUserRequest();
        r.setRealName("N"); r.setPhone("138"); r.setEmail("e@x.com"); r.setDisabled(true);
        assertEquals("N", r.getRealName());
        assertEquals("138", r.getPhone());
        assertEquals("e@x.com", r.getEmail());
        assertTrue(r.getDisabled());
        assertTrue(r.toCommand(1, 2).disabled());
    }

    @Test void resetPasswordRequest() {
        ResetPasswordRequest r = new ResetPasswordRequest();
        r.setContactType("phone"); r.setContactValue("138"); r.setVerifyCode("123456");
        r.setNewPassword("p1"); r.setConfirmPassword("p1");
        assertEquals("phone", r.getContactType());
        assertEquals("138", r.getContactValue());
        assertEquals("123456", r.getVerifyCode());
        assertEquals("p1", r.getNewPassword());
        assertEquals("p1", r.getConfirmPassword());
        assertEquals("phone", r.toCommand().contactType());
    }

    @Test void sendResetCodeRequest() {
        SendResetCodeRequest r = new SendResetCodeRequest();
        r.setContactType("email"); r.setContactValue("e@x.com");
        assertEquals("email", r.getContactType());
        assertEquals("e@x.com", r.getContactValue());
        assertEquals("email", r.toCommand().contactType());
    }

    @Test void loginRequest() {
        LoginRequest r = new LoginRequest();
        r.setAccount("a"); r.setPassword("p");
        assertEquals("a", r.getAccount());
        assertEquals("p", r.getPassword());
    }

    @Test void registerRequest() {
        RegisterRequest r = new RegisterRequest();
        r.setAccount("a"); r.setPassword("p"); r.setRealName("N"); r.setPhone("138"); r.setEmail("e@x.com");
        assertEquals("a", r.getAccount());
        assertEquals("p", r.getPassword());
        assertEquals("N", r.getRealName());
        assertEquals("138", r.getPhone());
        assertEquals("e@x.com", r.getEmail());
    }

    @Test void adjustRolePermissionsPermissionItem() {
        AdjustRolePermissionsRequest.PermissionItem item = new AdjustRolePermissionsRequest.PermissionItem();
        item.setFunctionId(9); item.setPlanEffectiveAt(t); item.setPlanExpiredAt(t);
        assertEquals(9, item.getFunctionId());
        assertEquals(t, item.getPlanEffectiveAt());
        assertEquals(t, item.getPlanExpiredAt());
    }

    @Test void adjustRolePermissionsRequestGetPermissions() {
        AdjustRolePermissionsRequest r = new AdjustRolePermissionsRequest();
        AdjustRolePermissionsRequest.PermissionItem item = new AdjustRolePermissionsRequest.PermissionItem();
        item.setFunctionId(1);
        r.setPermissions(java.util.List.of(item));
        assertEquals(1, r.getPermissions().size());
        assertEquals(1, r.getPermissions().get(0).getFunctionId());
    }
}
