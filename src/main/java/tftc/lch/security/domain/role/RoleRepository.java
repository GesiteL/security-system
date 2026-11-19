package tftc.lch.security.domain.role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Optional;
import java.util.List;
@Mapper
public interface RoleRepository {
    Optional<Role> findById(int id);
    Optional<Role> findByName(String name);
    List<Role> findAll(@Param("nameKeyword") String nameKeyword, @Param("page") int page, @Param("pageSize") int pageSize);
    int countAll(@Param("nameKeyword") String nameKeyword);
    int save(Role role);
    int update(Role role);
    boolean existsByName(String name);
}
