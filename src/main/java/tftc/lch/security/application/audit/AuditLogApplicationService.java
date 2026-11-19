package tftc.lch.security.application.audit;

import tftc.lch.security.domain.audit.AuditLog;
import java.util.List;

/** 审计日志读取应用服务接口（改造新增：审计中心数据源）。 */
public interface AuditLogApplicationService {

    /**
     * 分页查询审计日志（按操作时间倒序）。
     *
     * @param offset   偏移量（(page-1)*pageSize）
     * @param pageSize 每页条数
     * @return 审计日志列表
     */
    List<AuditLog> listAuditLogs(int offset, int pageSize);

    /** 统计审计日志总数（分页支持）。 */
    int countAuditLogs();
}
