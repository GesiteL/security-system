package tftc.lch.security.application.userrole;

import org.springframework.stereotype.Service;
import tftc.lch.security.domain.shared.DomainException;
import tftc.lch.security.domain.user.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserRoleApplicationServiceImpl implements UserRoleApplicationService {
    private final UserRoleRepository urRepo;
    private final UserRepository userRepo;
    public UserRoleApplicationServiceImpl(UserRoleRepository r, UserRepository u) { urRepo=r; userRepo=u; }
    @Override public List<UserRole> listUserRoles(GetUserRoleQuery q) { return urRepo.findByConditions(q.userId(), q.displayName(), q.phone(), q.email(), q.roleName(), q.page(), q.pageSize()); }
    @Override public int countUserRoles(GetUserRoleQuery q) { return urRepo.countByConditions(q.userId(), q.displayName(), q.phone(), q.email(), q.roleName()); }
    @Override public UserRole assignRole(AssignUserRoleCommand c) { if (urRepo.existsByUserIdAndRoleId(c.userId(), c.roleId())) throw new tftc.lch.security.domain.shared.DomainException(5001, "已分配");
        UserRole ur = new UserRole(); ur.setUserId(c.userId()); ur.setRoleId(c.roleId()); ur.setPlanEffectiveAt(c.planEffectiveAt()!=null?c.planEffectiveAt():LocalDateTime.now()); ur.setPlanExpiredAt(c.planExpiredAt()); ur.setCreatedAt(LocalDateTime.now()); ur.setUpdatedAt(LocalDateTime.now()); urRepo.save(ur);
        userRepo.findById(c.userId()).ifPresent(u -> { u.incrementCredentialsVersion(); userRepo.update(u); });
        return ur;
    }
    @Override public UserRole updateUserRole(UpdateUserRoleCommand c) {
        UserRole ur = urRepo.findById(c.userRoleId()).orElseThrow(() -> new DomainException(5002, "不存在"));
        ur.updatePeriod(c.planEffectiveAt(), c.planExpiredAt()); urRepo.update(ur);
        userRepo.findById(c.userId()).ifPresent(u -> { u.incrementCredentialsVersion(); userRepo.update(u); });
        return ur;
    }
    @Override public void removeUserRole(RemoveUserRoleCommand c) {
        UserRole ur = urRepo.findById(c.userRoleId()).orElseThrow(() -> new DomainException(5002, "不存在"));
        ur.remove(); urRepo.update(ur);
        userRepo.findById(c.userId()).ifPresent(u -> { u.incrementCredentialsVersion(); userRepo.update(u); });
    }
}
