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
import tftc.lch.security.domain.audit.AuditLog;
import tftc.lch.security.domain.audit.AuditLogRepository;
import tftc.lch.security.domain.user.UserRepository;
import tftc.lch.security.infrastructure.annotation.RequirePermission;
import tftc.lch.security.infrastructure.security.BaseContext;
import java.util.*;

@Aspect
@Component
public class AuditAspect {
    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);
    private final AccessControlApplicationService accessControlService;
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditAspect(AccessControlApplicationService accessControlService, AuditLogRepository auditLogRepository, UserRepository userRepository) {
        this.accessControlService = accessControlService;
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
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
        boolean ok = true;
        try {
            Object result = pjp.proceed();
            log.info("method={} userId={} params={} elapsed={}ms result={}", method, userId, Arrays.toString(pjp.getArgs()), System.currentTimeMillis() - start, result);
            return result;
        } catch (Throwable t) {
            ok = false;
            throw t;
        } finally {
            BaseContext.clear();
            // 改造新增：审计落库（写失败不影响业务响应）
            writeAudit(userId, perm.value(), request.getRemoteAddr(), ok);
        }
    }

    /** 写入 audit_log：动作取权限串，对象类型取路径首段；任何异常只记 WARN，不抛给业务层。 */
    private void writeAudit(Long userId, String permission, String ip, boolean ok) {
        try {
            AuditLog a = new AuditLog();
            a.setOperatorId(userId != null ? userId.intValue() : null);
            userRepository.findById(userId.intValue()).ifPresent(u -> a.setOperatorName(u.getAccount()));
            a.setAction(permission);
            a.setTargetType(targetTypeOf(permission));
            a.setResult(ok);
            a.setIpAddress(ip);
            auditLogRepository.save(a);
        } catch (Exception e) {
            log.warn("audit persist failed: {}", e.getMessage());
        }
    }

    /** 从 "METHOD:/api/v1/xxx/..." 里取出 xxx 作为对象类型。 */
    private String targetTypeOf(String permission) {
        if (permission == null) return null;
        int idx = permission.indexOf("/api/v1/");
        if (idx < 0) return null;
        String rest = permission.substring(idx + "/api/v1/".length());
        int slash = rest.indexOf('/');
        return slash > 0 ? rest.substring(0, slash) : rest;
    }

    private Object unauthorized() { return ResponseEntity.status(401).body(Map.of("code", 1002, "message", "未登录或Token已失效")); }
    private Object forbidden(String p) { return ResponseEntity.status(403).body(Map.of("code", 1003, "message", "无权限访问：" + p)); }
}
