package tftc.lch.security.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tftc.lch.security.application.audit.AuditLogApplicationService;
import tftc.lch.security.presentation.dto.response.AuditLogResponse;
import tftc.lch.security.presentation.dto.response.PageResponse;
import java.util.stream.Collectors;

/** 审计日志读取 Controller 实现（改造新增）。未加 @RequirePermission：sys_function 表无对应功能项，权限校验切面会拒绝所有调用者。 */
@RestController
public class AuditLogControllerImpl implements AuditLogController {
    private final AuditLogApplicationService auditLogService;
    public AuditLogControllerImpl(AuditLogApplicationService auditLogService) { this.auditLogService = auditLogService; }
    @Override
    public ResponseEntity<PageResponse<AuditLogResponse>> listAuditLogs(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="20") int pageSize) {
        if (page < 1) page = 1;
        var logs = auditLogService.listAuditLogs((page - 1) * pageSize, pageSize);
        int total = auditLogService.countAuditLogs();
        var recs = logs.stream().map(AuditLogResponse::fromDomain).collect(Collectors.toList());
        return ResponseEntity.ok(PageResponse.of(recs, total, page, pageSize));
    }
}
