package tftc.lch.security.application.role;

import org.springframework.stereotype.Service;
import tftc.lch.security.domain.role.*;
import tftc.lch.security.domain.shared.DomainException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleApplicationServiceImpl implements RoleApplicationService {
    private final RoleRepository repo;
    public RoleApplicationServiceImpl(RoleRepository rp) { this.repo = rp; }
    @Override public List<Role> listRoles(GetRoleQuery q) { return repo.findAll(q.nameKeyword(), q.page(), q.pageSize()); }
    @Override public int countRoles(GetRoleQuery q) { return repo.countAll(q.nameKeyword()); }
    @Override public Role createRole(CreateRoleCommand c) {
        if (repo.existsByName(c.roleName())) throw new DomainException(4001, "角色名已存在");
        Role r = Role.create(c.roleName(), c.description()); repo.save(r); return r;
    }
    @Override public Role updateRole(UpdateRoleCommand c) {
        Role r = repo.findById(c.roleId()).orElseThrow(() -> new DomainException(4004, "不存在"));
        r.updateInfo(c.roleName(), c.description()); repo.update(r); return r;
    }
    @Override public void deleteRole(DeleteRoleCommand c) { Role r = repo.findById(c.roleId()).orElseThrow(() -> new DomainException(4004, "不存在")); r.softDelete(); repo.update(r); }
}
