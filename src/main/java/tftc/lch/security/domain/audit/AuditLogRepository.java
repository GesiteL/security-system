package tftc.lch.security.domain.audit;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/** 审计日志读取仓储接口（改造新增：分页查询，表为只读 BR-25）。 */
@Mapper
public interface AuditLogRepository {
    List<AuditLog> findAll(@Param("offset") int offset, @Param("pageSize") int pageSize);
    int countAll();
}
