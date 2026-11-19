package tftc.lch.security.infrastructure.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

/** 全局异常处理器，统一返回 ResponseEntity。 */
@RestControllerAdvice(basePackages = "tftc.lch.security.presentation.controller")
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    
    @ExceptionHandler(tftc.lch.security.domain.shared.DomainException.class)
    public ResponseEntity<Map<String, Object>> handleDomain(tftc.lch.security.domain.shared.DomainException e) {
        return ResponseEntity.badRequest().body(Map.of("code", e.getErrorCode(), "message", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b).orElse("参数校验失败");
        return ResponseEntity.badRequest().body(Map.of("code", 1001, "message", msg));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleNotReadable() {
        return ResponseEntity.badRequest().body(Map.of("code", 1001, "message", "请求体格式错误"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethod() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(Map.of("code", 1001, "message", "不支持的请求方法"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch() {
        return ResponseEntity.badRequest().body(Map.of("code", 1001, "message", "参数类型错误"));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam() {
        return ResponseEntity.badRequest().body(Map.of("code", 1001, "message", "缺少必要参数"));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMediaType() {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(Map.of("code", 1001, "message", "不支持的Content-Type"));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, Object>> handleBind(BindException e) {
        return ResponseEntity.badRequest().body(Map.of("code", 1001, "message", "参数绑定失败"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("系统异常", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("code", 9999, "message", "系统内部错误"));
    }
}
