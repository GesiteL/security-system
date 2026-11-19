package tftc.lch.security.domain.audit;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/** 审计日志仓储接口（改造新增：读取侧分页查询 + 写入侧落库）。 */
@Mapper
public interface AuditLogRepository {
    List<AuditLog> findAll(@Param("offset") int offset, @Param("pageSize") int pageSize);
    int countAll();
    int save(AuditLog auditLog);
}
