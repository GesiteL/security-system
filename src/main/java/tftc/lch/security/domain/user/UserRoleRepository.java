package tftc.lch.security.domain.user;
import org.apache.ibatis.annotations.Mapper;
import java.util.Optional;
@Mapper
public interface UserRoleRepository {
    Optional<UserRole> findById(int userRoleId);
    java.util.List<UserRole> findByConditions(int userId, String displayName, String phone, String email, String roleName, int page, int pageSize);
    int countByConditions(int userId, String displayName, String phone, String email, String roleName);
    boolean existsByUserIdAndRoleId(int userId, int roleId);
    int save(UserRole userRole);
    int update(UserRole userRole);
}
