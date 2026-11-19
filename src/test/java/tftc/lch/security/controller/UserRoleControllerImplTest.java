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
import tftc.lch.security.application.userrole.*;
import tftc.lch.security.presentation.controller.UserRoleControllerImpl;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)class UserRoleControllerImplTest {
    @Mock UserRoleApplicationService urService;
    @InjectMocks UserRoleControllerImpl controller;

    @Test void listUserRoles() throws Exception { when(urService.listUserRoles(any())).thenReturn(List.of()); when(urService.countUserRoles(any())).thenReturn(0); MockMvcBuilders.standaloneSetup(controller).build().perform(get("/api/v1/users/1/roles")).andExpect(status().isOk()); }

    @Test void assignRole() throws Exception { when(urService.assignRole(any())).thenReturn(new tftc.lch.security.domain.user.UserRole()); MockMvcBuilders.standaloneSetup(controller).build().perform(post("/api/v1/users/1/roles").contentType("application/json").content("{\"roleId\":2}")).andExpect(status().isCreated()); }

    @Test void assignRoleWithOperator() throws Exception { tftc.lch.security.infrastructure.security.BaseContext.setCurrentUserId(7L); try { when(urService.assignRole(any())).thenReturn(new tftc.lch.security.domain.user.UserRole()); MockMvcBuilders.standaloneSetup(controller).build().perform(post("/api/v1/users/1/roles").contentType("application/json").content("{\"roleId\":2}")).andExpect(status().isCreated()); } finally { tftc.lch.security.infrastructure.security.BaseContext.clear(); } }

    @Test void updateUserRole() throws Exception { when(urService.updateUserRole(any())).thenReturn(new tftc.lch.security.domain.user.UserRole()); MockMvcBuilders.standaloneSetup(controller).build().perform(patch("/api/v1/users/1/roles/1").contentType("application/json").content("{}")).andExpect(status().isOk()); }

    @Test void updateUserRoleWithOperator() throws Exception { tftc.lch.security.infrastructure.security.BaseContext.setCurrentUserId(3L); try { when(urService.updateUserRole(any())).thenReturn(new tftc.lch.security.domain.user.UserRole()); MockMvcBuilders.standaloneSetup(controller).build().perform(patch("/api/v1/users/1/roles/1").contentType("application/json").content("{}")).andExpect(status().isOk()); } finally { tftc.lch.security.infrastructure.security.BaseContext.clear(); } }

    @Test void removeRole() throws Exception { MockMvcBuilders.standaloneSetup(controller).build().perform(delete("/api/v1/users/1/roles/1")).andExpect(status().isNoContent()); }
}
