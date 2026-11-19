package tftc.lch.security.domain.user;
import org.apache.ibatis.annotations.Mapper;
import java.util.Optional;
@Mapper
public interface PasswordResetRepository {
    int save(PasswordReset passwordReset);
    Optional<PasswordReset> findLatestByUserId(int userId);
    int countRecentByUserId(int userId, int minutes);
    Optional<PasswordReset> findLatestByContact(String contactType, String contactValue);
    Optional<PasswordReset> findValidCode(String contactValue, String code);
}
