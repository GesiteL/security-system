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
import tftc.lch.security.application.user.*;
import tftc.lch.security.domain.user.User;
import tftc.lch.security.presentation.controller.UserControllerImpl;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserControllerImplTest {
    @Mock UserApplicationService userService;
    @InjectMocks UserControllerImpl controller;

    @Test void listUsers() throws Exception { when(userService.listUsers(any())).thenReturn(List.of()); when(userService.countUsers(any())).thenReturn(0); MockMvcBuilders.standaloneSetup(controller).build().perform(get("/api/v1/users?page=1&pageSize=10")).andExpect(status().isOk()); }
    @Test void createUser() throws Exception { when(userService.createUser(any())).thenReturn(User.create("u","h","U","138","e")); MockMvcBuilders.standaloneSetup(controller).build().perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content("{\"account\":\"u\",\"password\":\"p\"}")).andExpect(status().isCreated()); }
    @Test void createUserWithOperator() throws Exception { tftc.lch.security.infrastructure.security.BaseContext.setCurrentUserId(2L); try { when(userService.createUser(any())).thenReturn(User.create("u","h","U","138","e")); MockMvcBuilders.standaloneSetup(controller).build().perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content("{\"account\":\"u\",\"password\":\"p\"}")).andExpect(status().isCreated()); } finally { tftc.lch.security.infrastructure.security.BaseContext.clear(); } }
    @Test void updateUser() throws Exception { when(userService.updateUser(any())).thenReturn(User.create("u","h","U","138","e")); MockMvcBuilders.standaloneSetup(controller).build().perform(patch("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON).content("{\"realName\":\"N\"}")).andExpect(status().isOk()); }
    @Test void updateUserWithOperator() throws Exception { tftc.lch.security.infrastructure.security.BaseContext.setCurrentUserId(2L); try { when(userService.updateUser(any())).thenReturn(User.create("u","h","U","138","e")); MockMvcBuilders.standaloneSetup(controller).build().perform(patch("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON).content("{\"realName\":\"N\"}")).andExpect(status().isOk()); } finally { tftc.lch.security.infrastructure.security.BaseContext.clear(); } }
    @Test void deleteUser() throws Exception { MockMvcBuilders.standaloneSetup(controller).build().perform(delete("/api/v1/users/1")).andExpect(status().isNoContent()); }
    @Test void deleteUserWithOperator() throws Exception { tftc.lch.security.infrastructure.security.BaseContext.setCurrentUserId(2L); try { MockMvcBuilders.standaloneSetup(controller).build().perform(delete("/api/v1/users/1")).andExpect(status().isNoContent()); } finally { tftc.lch.security.infrastructure.security.BaseContext.clear(); } }
}