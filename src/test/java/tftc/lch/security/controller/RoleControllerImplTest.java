package tftc.lch.security.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tftc.lch.security.application.role.*;
import tftc.lch.security.domain.role.Role;
import tftc.lch.security.presentation.controller.RoleControllerImpl;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)class RoleControllerImplTest {
    @Mock RoleApplicationService roleService;
    @InjectMocks RoleControllerImpl controller;

    @Test void listRoles() throws Exception { when(roleService.listRoles(any())).thenReturn(List.of()); when(roleService.countRoles(any())).thenReturn(0); MockMvcBuilders.standaloneSetup(controller).build().perform(get("/api/v1/roles?page=1&pageSize=10")).andExpect(status().isOk()); }

    @Test void createRole() throws Exception { when(roleService.createRole(any())).thenReturn(new Role()); MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build(); mvc.perform(post("/api/v1/roles").contentType(MediaType.APPLICATION_JSON).content("{\"roleName\":\"R\"}")).andExpect(status().isCreated()); }

    @Test void createRoleWithOperator() throws Exception { tftc.lch.security.infrastructure.security.BaseContext.setCurrentUserId(5L); try { when(roleService.createRole(any())).thenReturn(new Role()); MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build(); mvc.perform(post("/api/v1/roles").contentType(MediaType.APPLICATION_JSON).content("{\"roleName\":\"R\"}")).andExpect(status().isCreated()); } finally { tftc.lch.security.infrastructure.security.BaseContext.clear(); } }

    @Test void updateRole() throws Exception { when(roleService.updateRole(any())).thenReturn(new Role()); MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build(); mvc.perform(patch("/api/v1/roles/1").contentType(MediaType.APPLICATION_JSON).content("{\"roleName\":\"R2\"}")).andExpect(status().isOk()); }

    @Test void updateRoleWithOperator() throws Exception { tftc.lch.security.infrastructure.security.BaseContext.setCurrentUserId(9L); try { when(roleService.updateRole(any())).thenReturn(new Role()); MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build(); mvc.perform(patch("/api/v1/roles/1").contentType(MediaType.APPLICATION_JSON).content("{\"roleName\":\"R2\"}")).andExpect(status().isOk()); } finally { tftc.lch.security.infrastructure.security.BaseContext.clear(); } }

    @Test void deleteRole() throws Exception { MockMvcBuilders.standaloneSetup(controller).build().perform(delete("/api/v1/roles/1")).andExpect(status().isNoContent()); }
}
