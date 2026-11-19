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
import tftc.lch.security.application.auth.AuthApplicationService;
import tftc.lch.security.domain.user.User;
import tftc.lch.security.infrastructure.security.JwtUtils;
import tftc.lch.security.presentation.controller.AuthControllerImpl;
import tftc.lch.security.presentation.dto.request.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)class AuthControllerImplTest {
    @Mock AuthApplicationService authService;
    @Mock JwtUtils jwtUtils;
    @InjectMocks AuthControllerImpl controller;
    MockMvc mvc;
    ObjectMapper om = new ObjectMapper();

    @Test void register() throws Exception {
        when(authService.register(any())).thenReturn(User.create("u","h","U","138","e"));
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        mvc.perform(post("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(new RegisterRequest())))
            .andExpect(status().isCreated());
    }

    @Test void login() throws Exception {
        when(authService.login(any())).thenReturn("token123");
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        mvc.perform(post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"account\":\"admin\",\"password\":\"admin\"}"))
            .andExpect(status().isOk());
    }

    @Test void logout() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        mvc.perform(post("/api/v1/auth/logout"))
            .andExpect(status().isNoContent());
    }

    @Test void logoutWithBearerToken() throws Exception {
        when(jwtUtils.getUserId("tok")).thenReturn(7L);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        mvc.perform(post("/api/v1/auth/logout")
            .header("Authorization", "Bearer tok"))
            .andExpect(status().isNoContent());
        verify(authService).logout(any());
    }

    @Test void logoutWithBearerTokenJwtThrows() throws Exception {
        when(jwtUtils.getUserId(anyString())).thenThrow(new RuntimeException("bad"));
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        mvc.perform(post("/api/v1/auth/logout")
            .header("Authorization", "Bearer tok"))
            .andExpect(status().isNoContent());
        verify(authService, never()).logout(any());
    }

    @Test void logoutWithNonBearerHeader() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        mvc.perform(post("/api/v1/auth/logout")
            .header("Authorization", "Basic abc"))
            .andExpect(status().isNoContent());
        verify(authService, never()).logout(any());
    }

    @Test void sendResetCode() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        mvc.perform(post("/api/v1/auth/password-reset-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"contactType\":\"phone\",\"contactValue\":\"138\"}"))
            .andExpect(status().isCreated());
        verify(authService).sendPasswordResetCode(any());
    }

    @Test void resetPassword() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        mvc.perform(post("/api/v1/auth/password-resets")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"contactType\":\"phone\",\"contactValue\":\"138\",\"verifyCode\":\"123456\",\"newPassword\":\"p\",\"confirmPassword\":\"p\"}"))
            .andExpect(status().isNoContent());
        verify(authService).resetPassword(any());
    }
}
