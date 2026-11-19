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
import tftc.lch.security.application.permission.*;
import tftc.lch.security.domain.permission.SysFunction;
import tftc.lch.security.presentation.controller.FunctionControllerImpl;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)class FunctionControllerImplTest {
    @Mock PermissionApplicationService permService;
    @InjectMocks FunctionControllerImpl controller;

    @Test void listFunctions() throws Exception { when(permService.listFunctions(any())).thenReturn(List.of()); when(permService.countFunctions(any())).thenReturn(0); MockMvcBuilders.standaloneSetup(controller).build().perform(get("/api/v1/functions?page=1&pageSize=10")).andExpect(status().isOk()); }

    @Test void registerFunction() throws Exception { when(permService.registerFunction(any())).thenReturn(SysFunction.create("f","d","M",LocalDateTime.now(),null)); MockMvcBuilders.standaloneSetup(controller).build().perform(post("/api/v1/functions").contentType(MediaType.APPLICATION_JSON).content("{\"functionName\":\"f\",\"module\":\"M\"}")).andExpect(status().isCreated()); }

    @Test void registerFunctionWithOperator() throws Exception { tftc.lch.security.infrastructure.security.BaseContext.setCurrentUserId(3L); try { when(permService.registerFunction(any())).thenReturn(SysFunction.create("f","d","M",LocalDateTime.now(),null)); MockMvcBuilders.standaloneSetup(controller).build().perform(post("/api/v1/functions").contentType(MediaType.APPLICATION_JSON).content("{\"functionName\":\"f\",\"module\":\"M\"}")).andExpect(status().isCreated()); } finally { tftc.lch.security.infrastructure.security.BaseContext.clear(); } }

    @Test void updateFunction() throws Exception { when(permService.updateFunction(any())).thenReturn(SysFunction.create("f","d","M",LocalDateTime.now(),null)); MockMvcBuilders.standaloneSetup(controller).build().perform(patch("/api/v1/functions/1").contentType(MediaType.APPLICATION_JSON).content("{\"functionName\":\"f2\",\"module\":\"M\"}")).andExpect(status().isOk()); }

    @Test void updateFunctionWithOperator() throws Exception { tftc.lch.security.infrastructure.security.BaseContext.setCurrentUserId(8L); try { when(permService.updateFunction(any())).thenReturn(SysFunction.create("f","d","M",LocalDateTime.now(),null)); MockMvcBuilders.standaloneSetup(controller).build().perform(patch("/api/v1/functions/1").contentType(MediaType.APPLICATION_JSON).content("{\"functionName\":\"f2\",\"module\":\"M\"}")).andExpect(status().isOk()); } finally { tftc.lch.security.infrastructure.security.BaseContext.clear(); } }

    @Test void deleteFunction() throws Exception { MockMvcBuilders.standaloneSetup(controller).build().perform(delete("/api/v1/functions/1")).andExpect(status().isNoContent()); }
}
