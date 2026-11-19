package tftc.lch.security.infrastructure.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tftc.lch.security.application.accesscontrol.AccessControlApplicationService;
import tftc.lch.security.application.accesscontrol.AccessControlCommand;
import tftc.lch.security.infrastructure.annotation.RequirePermission;
import tftc.lch.security.infrastructure.security.BaseContext;
import java.util.*;

@Aspect
@Component
public class AuditAspect {
    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);
    private final AccessControlApplicationService accessControlService;

    public AuditAspect(AccessControlApplicationService accessControlService) {
        this.accessControlService = accessControlService;
    }

    @Around("@annotation(perm)")
    public Object checkPermission(ProceedingJoinPoint pjp, RequirePermission perm) throws Throwable {
        long start = System.currentTimeMillis();
        String method = pjp.getSignature().toShortString();

        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return unauthorized();
        HttpServletRequest request = attrs.getRequest();
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return unauthorized();
        String token = authHeader.substring(7);

        Long userId;
        try {
            userId = accessControlService.verify(new AccessControlCommand(token, perm.value()));
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("FORBIDDEN"))
                return forbidden(perm.value());
            return unauthorized();
        }

        BaseContext.setCurrentUserId(userId);
        try {
            Object result = pjp.proceed();
            log.info("method={} userId={} params={} elapsed={}ms result={}", method, userId, Arrays.toString(pjp.getArgs()), System.currentTimeMillis() - start, result);
            return result;
        } finally {
            BaseContext.clear();
        }
    }

    private Object unauthorized() { return ResponseEntity.status(401).body(Map.of("code", 1002, "message", "未登录或Token已失效")); }
    private Object forbidden(String p) { return ResponseEntity.status(403).body(Map.of("code", 1003, "message", "无权限访问：" + p)); }
}