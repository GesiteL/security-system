package tftc.lch.security.application.accesscontrol;

import org.springframework.stereotype.Service;
import tftc.lch.security.infrastructure.security.JwtUtils;
import tftc.lch.security.domain.user.User;
import tftc.lch.security.domain.user.UserRepository;
import tftc.lch.security.domain.user.UserRoleRepository;
import tftc.lch.security.domain.permission.SysFunctionRepository;
import tftc.lch.security.domain.role.RolePermissionRepository;
import java.util.Optional;

@Service
public class AccessControlApplicationServiceImpl implements AccessControlApplicationService {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final SysFunctionRepository funcRepository;

    public AccessControlApplicationServiceImpl(JwtUtils jwtUtils, UserRepository userRepository, UserRoleRepository userRoleRepository, RolePermissionRepository rolePermissionRepository, SysFunctionRepository funcRepository) {
        this.jwtUtils = jwtUtils; this.userRepository = userRepository; this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository; this.funcRepository = funcRepository;
    }

    @Override
    public Long verify(AccessControlCommand cmd) {
        Long userId = jwtUtils.getUserId(cmd.token());
        Optional<User> userOpt = userRepository.findById(userId.intValue());
        if (userOpt.isEmpty() || userOpt.get().isDeleted() || userOpt.get().isDisabled())
            throw new RuntimeException("UNAUTHORIZED");
        Long tokenCv = jwtUtils.getCredentialsVersion(cmd.token());
        if (tokenCv == null || tokenCv.intValue() != userOpt.get().getCredentialsVersion())
            throw new RuntimeException("UNAUTHORIZED");
        if (cmd.requiredPermission() == null || cmd.requiredPermission().isEmpty())
            return userId;
        var roles = userRoleRepository.findByConditions(userId.intValue(), null, null, null, null, 0, Integer.MAX_VALUE);
        for (var ur : roles) {
            if (ur.isDeleted()) continue;
            var rps = rolePermissionRepository.findByRoleId(ur.getRoleId(), null);
            for (var rp : rps) {
                if (rp.isDeleted()) continue;
                var fOpt = funcRepository.findById(rp.getPermissionId());
                if (fOpt.isPresent() && !fOpt.get().isDeleted() && fOpt.get().getModule().equals(cmd.requiredPermission()))
                    return userId;
            }
        }
        throw new RuntimeException("FORBIDDEN");
    }
}
