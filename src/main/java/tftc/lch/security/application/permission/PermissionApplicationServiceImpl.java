package tftc.lch.security.application.permission;

import org.springframework.stereotype.Service;
import tftc.lch.security.domain.permission.*;
import tftc.lch.security.domain.role.*;
import tftc.lch.security.domain.shared.DomainException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PermissionApplicationServiceImpl implements PermissionApplicationService {
    private final SysFunctionRepository funcRepo;
    private final RolePermissionRepository rpRepo;
    public PermissionApplicationServiceImpl(SysFunctionRepository fr, RolePermissionRepository rp) { this.funcRepo = fr; this.rpRepo = rp; }

    @Override public List<SysFunction> listFunctions(GetFunctionQuery q) { return funcRepo.findAll(q.nameKeyword(), q.module(), q.page(), q.pageSize()); }
    @Override public int countFunctions(GetFunctionQuery q) { return funcRepo.countAll(q.nameKeyword(), q.module()); }
    @Override public SysFunction registerFunction(RegisterFunctionCommand c) {
        if (funcRepo.existsByName(c.functionName())) throw new DomainException(6001, "已存在");
        SysFunction f = SysFunction.create(c.functionName(), c.description(), c.module(), c.planEffectiveAt(), c.planExpiredAt());
        funcRepo.save(f); return f;
    }
    @Override public SysFunction updateFunction(UpdateFunctionCommand c) {
        SysFunction f = funcRepo.findById(c.functionId()).orElseThrow(() -> new DomainException(6002, "不存在"));
        f.updateInfo(c.functionName(), c.description(), c.module(), c.planEffectiveAt(), c.planExpiredAt()); funcRepo.update(f); return f;
    }
    @Override public void deleteFunction(DeleteFunctionCommand c) { SysFunction f = funcRepo.findById(c.functionId()).orElseThrow(() -> new DomainException(6002, "不存在")); f.softDelete(); funcRepo.update(f); }

    @Override public List<RolePermission> listRolePermissions(int roleId, String kw) { return rpRepo.findByRoleId(roleId, kw); }
    @Override public RolePermission grantPermission(GrantPermissionCommand c) {
        RolePermission rp = new RolePermission(); rp.setRoleId(c.roleId()); rp.setPermissionId(c.functionId()); rp.setPlanEffectiveAt(c.planEffectiveAt()!=null?c.planEffectiveAt():LocalDateTime.now()); rp.setPlanExpiredAt(c.planExpiredAt()); rp.setCreatedAt(LocalDateTime.now()); rp.setUpdatedAt(LocalDateTime.now()); rpRepo.save(rp); return rp;
    }
    @Override public List<RolePermission> adjustRolePermissions(AdjustRolePermissionsCommand c) { return rpRepo.findByRoleId(c.roleId(), null); }
    @Override public void revokePermission(RevokePermissionCommand c) { RolePermission rp = rpRepo.findById(c.grantId()).orElseThrow(() -> new DomainException(6004, "不存在")); rp.revoke(); rpRepo.update(rp); }
}
