package tftc.lch.security.presentation.dto.response;

import tftc.lch.security.domain.user.User;
import java.time.LocalDateTime;

/** 用户信息响应 DTO（敏感字段：passwordHash 不返回）。 */
public class UserResponse {
    private int id;
    private String account;
    private String realName;
    private String phone;
    private String email;
    private boolean disabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse fromDomain(User user) {
        UserResponse r = new UserResponse();
        r.id = user.getId();
        r.account = user.getAccount();
        r.realName = user.getRealName();
        r.phone = user.getPhone();
        r.email = user.getEmail();
        r.disabled = user.isDisabled();
        r.createdAt = user.getCreatedAt();
        r.updatedAt = user.getUpdatedAt();
        return r;
    }

    public int getId() { return id; }
    public String getUsername() { return account; }
    public String getDisplayName() { return realName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public boolean isDisabled() { return disabled; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
