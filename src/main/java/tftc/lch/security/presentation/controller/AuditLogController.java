package tftc.lch.security.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tftc.lch.security.presentation.dto.response.AuditLogResponse;
import tftc.lch.security.presentation.dto.response.PageResponse;

/**
 * 审计日志读取 Controller 接口（改造新增）。
 * 现有仓库仅含审计写入侧（AuditAspect 切面），读取侧为本作品补全。
 *
 * @see <a href="../../../docs/architecture/db.html">audit_log / audit_log_item 表</a>
 */
@RequestMapping("/api/v1/audit-logs")
public interface AuditLogController {

    /**
     * 分页查询审计日志（按操作时间倒序）。
     *
     * @param page     页码，默认 1
     * @param pageSize 每页条数，默认 20
     * @return 分页审计日志
     */
    @GetMapping
    ResponseEntity<PageResponse<AuditLogResponse>> listAuditLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize
    );
}
