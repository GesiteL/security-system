package tftc.lch.security.domain.user;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRepository {
    Optional<User> findById(@Param("id") int id);
    Optional<User> findByAccount(@Param("username") String username);
    List<User> findAll(@Param("usernameKeyword") String usernameKeyword, @Param("displayNameKeyword") String displayNameKeyword, @Param("phoneKeyword") String phoneKeyword, @Param("status") Boolean status, @Param("page") int page, @Param("pageSize") int pageSize);
    int countAll(@Param("usernameKeyword") String usernameKeyword, @Param("displayNameKeyword") String displayNameKeyword, @Param("phoneKeyword") String phoneKeyword, @Param("status") Boolean status);
    int save(User user);
    int update(User user);
    boolean existsByAccount(@Param("username") String username);
    boolean existsByPhone(@Param("phone") String phone, @Param("excludeId") int excludeId);
    Optional<User> findByPhone(@Param("phone") String phone);
    Optional<User> findByEmail(@Param("email") String email);
}