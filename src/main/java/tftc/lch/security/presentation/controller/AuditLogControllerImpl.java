package tftc.lch.security.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tftc.lch.security.application.audit.AuditLogApplicationService;
import tftc.lch.security.infrastructure.annotation.RequirePermission;
import tftc.lch.security.presentation.dto.response.AuditLogResponse;
import tftc.lch.security.presentation.dto.response.PageResponse;
import java.util.stream.Collectors;

/** 审计日志读取 Controller 实现（改造新增）：走与其余 Controller 一致的 @RequirePermission 权限校验。 */
@RestController
public class AuditLogControllerImpl implements AuditLogController {
    private final AuditLogApplicationService auditLogService;
    public AuditLogControllerImpl(AuditLogApplicationService auditLogService) { this.auditLogService = auditLogService; }
    @Override @RequirePermission("GET:/api/v1/audit-logs")
    public ResponseEntity<PageResponse<AuditLogResponse>> listAuditLogs(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="20") int pageSize) {
        if (page < 1) page = 1;
        var logs = auditLogService.listAuditLogs((page - 1) * pageSize, pageSize);
        int total = auditLogService.countAuditLogs();
        var recs = logs.stream().map(AuditLogResponse::fromDomain).collect(Collectors.toList());
        return ResponseEntity.ok(PageResponse.of(recs, total, page, pageSize));
    }
}
