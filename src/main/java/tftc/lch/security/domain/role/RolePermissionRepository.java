package tftc.lch.security.domain.role;
import org.apache.ibatis.annotations.Mapper;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
@Mapper
public interface RolePermissionRepository {
    Optional<RolePermission> findById(int grantId);
    List<RolePermission> findByRoleId(int roleId, String functionNameKeyword);
    boolean existsByRoleIdAndPermissionId(int roleId, int permissionId);
    boolean existsActivePermission(int userId, String permCode, LocalDateTime now);
    int save(RolePermission rolePermission);
    int update(RolePermission rolePermission);
}
