package tftc.lch.security.service;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tftc.lch.security.application.accesscontrol.AccessControlApplicationService;
import tftc.lch.security.domain.audit.AuditLogRepository;
import tftc.lch.security.domain.user.UserRepository;
import tftc.lch.security.infrastructure.annotation.RequirePermission;
import tftc.lch.security.infrastructure.aspect.AuditAspect;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditAspectTest {

    AccessControlApplicationService accessControl = mock(AccessControlApplicationService.class);
    AuditLogRepository auditLogRepository = mock(AuditLogRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    AuditAspect aspect = new AuditAspect(accessControl, auditLogRepository, userRepository);
    ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
    Signature signature = mock(Signature.class);
    HttpServletRequest request = mock(HttpServletRequest.class);

    @BeforeEach void setUp() throws Throwable {
        when(pjp.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn("Test.method()");
        when(pjp.getArgs()).thenReturn(new Object[]{});
        when(pjp.proceed()).thenReturn("ok");
    }

    @AfterEach void tearDown() { RequestContextHolder.resetRequestAttributes(); }

    @Test void noRequestAttributes() throws Throwable {
        RequestContextHolder.resetRequestAttributes();
        Object r = aspect.checkPermission(pjp, annotation("user:list"));
        assertUnauthorized(r);
    }

    @Test void missingAuthHeader() throws Throwable {
        setRequest(null);
        Object r = aspect.checkPermission(pjp, annotation("user:list"));
        assertUnauthorized(r);
    }

    @Test void nonBearerHeader() throws Throwable {
        setRequest("Basic abc");
        Object r = aspect.checkPermission(pjp, annotation("user:list"));
        assertUnauthorized(r);
    }

    @Test void verifySuccess() throws Throwable {
        setRequest("Bearer valid-token");
        when(accessControl.verify(any())).thenReturn(42L);
        Object r = aspect.checkPermission(pjp, annotation("user:list"));
        assertEquals("ok", r);
    }

    @Test void forbiddenException() throws Throwable {
        setRequest("Bearer token");
        RuntimeException ex = new RuntimeException("FORBIDDEN");
        when(accessControl.verify(any())).thenThrow(ex);
        Object r = aspect.checkPermission(pjp, annotation("user:list"));
        assertEquals(403, ((ResponseEntity<?>) r).getStatusCode().value());
    }

    @Test void otherRuntimeException() throws Throwable {
        setRequest("Bearer token");
        when(accessControl.verify(any())).thenThrow(new RuntimeException("bad"));
        Object r = aspect.checkPermission(pjp, annotation("user:list"));
        assertUnauthorized(r);
    }

    @Test void nullMessageRuntimeException() throws Throwable {
        setRequest("Bearer token");
        when(accessControl.verify(any())).thenThrow(new RuntimeException());
        Object r = aspect.checkPermission(pjp, annotation("user:list"));
        assertUnauthorized(r);
    }

    private void setRequest(String header) {
        ServletRequestAttributes attrs = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attrs);
        when(request.getHeader("Authorization")).thenReturn(header);
    }

    private RequirePermission annotation(String value) throws Exception {
        return getClass().getDeclaredMethod("annotated" + value.replace(":", "")).getAnnotation(RequirePermission.class);
    }

    @RequirePermission("user:list") void annotateduserlist() {}

    @SuppressWarnings("unchecked")
    private void assertUnauthorized(Object r) {
        ResponseEntity<?> re = (ResponseEntity<?>) r;
        assertEquals(401, re.getStatusCode().value());
    }
}
