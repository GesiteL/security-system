package tftc.lch.security.application.audit;

import org.springframework.stereotype.Service;
import tftc.lch.security.domain.audit.AuditLog;
import tftc.lch.security.domain.audit.AuditLogRepository;
import java.util.List;

@Service
public class AuditLogApplicationServiceImpl implements AuditLogApplicationService {
    private final AuditLogRepository repo;
    public AuditLogApplicationServiceImpl(AuditLogRepository repo) { this.repo = repo; }
    @Override public List<AuditLog> listAuditLogs(int offset, int pageSize) { return repo.findAll(offset, pageSize); }
    @Override public int countAuditLogs() { return repo.countAll(); }
}
