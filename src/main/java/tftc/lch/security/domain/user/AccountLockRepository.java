package tftc.lch.security.domain.user;
import org.apache.ibatis.annotations.Mapper;
import java.util.Optional;
@Mapper
public interface AccountLockRepository {
    Optional<AccountLock> findByAccount(String account);
    int save(AccountLock lock);
    void resetByAccount(String account);
}
