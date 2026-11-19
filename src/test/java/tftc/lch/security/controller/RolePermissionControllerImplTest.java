package tftc.lch.security.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tftc.lch.security.application.permission.*;
import tftc.lch.security.presentation.controller.RolePermissionControllerImpl;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RolePermissionControllerImplTest {
    @Mock PermissionApplicationService permService;
    @InjectMocks RolePermissionControllerImpl controller;

    @Test void listRolePermissions() throws Exception { when(permService.listRolePermissions(anyInt(),any())).thenReturn(List.of()); MockMvcBuilders.standaloneSetup(controller).build().perform(get("/api/v1/roles/1/permissions")).andExpect(status().isOk()); }
    @Test void grantPermission() throws Exception { when(permService.grantPermission(any())).thenReturn(new tftc.lch.security.domain.role.RolePermission()); MockMvcBuilders.standaloneSetup(controller).build().perform(post("/api/v1/roles/1/permissions").contentType("application/json").content("{\"functionId\":1}")).andExpect(status().isCreated()); }
    @Test void grantPermissionWithOperator() throws Exception { tftc.lch.security.infrastructure.security.BaseContext.setCurrentUserId(4L); try { when(permService.grantPermission(any())).thenReturn(new tftc.lch.security.domain.role.RolePermission()); MockMvcBuilders.standaloneSetup(controller).build().perform(post("/api/v1/roles/1/permissions").contentType("application/json").content("{\"functionId\":1}")).andExpect(status().isCreated()); } finally { tftc.lch.security.infrastructure.security.BaseContext.clear(); } }
    @Test void adjustRolePermissions() throws Exception { when(permService.adjustRolePermissions(any())).thenReturn(List.of()); MockMvcBuilders.standaloneSetup(controller).build().perform(put("/api/v1/roles/1/permissions").contentType("application/json").content("{\"permissions\":[{\"functionId\":1}]}")).andExpect(status().isOk()); }
    @Test void adjustRolePermissionsWithOperator() throws Exception { tftc.lch.security.infrastructure.security.BaseContext.setCurrentUserId(6L); try { when(permService.adjustRolePermissions(any())).thenReturn(List.of()); MockMvcBuilders.standaloneSetup(controller).build().perform(put("/api/v1/roles/1/permissions").contentType("application/json").content("{\"permissions\":[{\"functionId\":1}]}")).andExpect(status().isOk()); } finally { tftc.lch.security.infrastructure.security.BaseContext.clear(); } }
    @Test void revokePermission() throws Exception { MockMvcBuilders.standaloneSetup(controller).build().perform(delete("/api/v1/roles/1/permissions/1")).andExpect(status().isNoContent()); }
}
