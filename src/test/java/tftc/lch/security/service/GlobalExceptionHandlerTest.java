package tftc.lch.security.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import tftc.lch.security.domain.shared.DomainException;
import tftc.lch.security.infrastructure.exception.GlobalExceptionHandler;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {
    GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test void handleDomainException() {
        ResponseEntity<Map<String, Object>> r = handler.handleDomain(new DomainException(1001, "业务异常"));
        assertEquals(400, r.getStatusCode().value());
        assertEquals(1001, r.getBody().get("code"));
    }

    @Test void handleGenericException() {
        ResponseEntity<Map<String, Object>> r = handler.handleException(new RuntimeException("系统错误"));
        assertEquals(500, r.getStatusCode().value());
        assertEquals(9999, r.getBody().get("code"));
    }

    @Test void handleValidationWithMultipleFieldErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult br = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(br);
        when(br.getFieldErrors()).thenReturn(List.of(
                new FieldError("obj", "account", "不能为空"),
                new FieldError("obj", "password", "长度不足")));
        ResponseEntity<Map<String, Object>> r = handler.handleValidation(ex);
        assertEquals(400, r.getStatusCode().value());
        assertEquals(1001, r.getBody().get("code"));
        assertTrue(r.getBody().get("message").toString().contains(";"));
    }

    @Test void handleValidationWithNoFieldErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult br = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(br);
        when(br.getFieldErrors()).thenReturn(List.of());
        ResponseEntity<Map<String, Object>> r = handler.handleValidation(ex);
        assertEquals("参数校验失败", r.getBody().get("message"));
    }

    @Test void handleNotReadable() {
        ResponseEntity<Map<String, Object>> r = handler.handleNotReadable();
        assertEquals(400, r.getStatusCode().value());
        assertEquals(1001, r.getBody().get("code"));
    }

    @Test void handleMethod() {
        ResponseEntity<Map<String, Object>> r = handler.handleMethod();
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), r.getStatusCode().value());
    }

    @Test void handleTypeMismatch() {
        ResponseEntity<Map<String, Object>> r = handler.handleTypeMismatch();
        assertEquals(400, r.getStatusCode().value());
    }

    @Test void handleMissingParam() {
        ResponseEntity<Map<String, Object>> r = handler.handleMissingParam();
        assertEquals(400, r.getStatusCode().value());
    }

    @Test void handleMediaType() {
        ResponseEntity<Map<String, Object>> r = handler.handleMediaType();
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), r.getStatusCode().value());
    }

    @Test void handleBind() {
        ResponseEntity<Map<String, Object>> r = handler.handleBind(new BindException(new Object(), "obj"));
        assertEquals(400, r.getStatusCode().value());
        assertEquals(1001, r.getBody().get("code"));
    }
}
