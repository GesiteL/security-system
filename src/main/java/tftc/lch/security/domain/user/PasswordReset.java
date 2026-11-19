package tftc.lch.security.domain.user;

import java.time.LocalDateTime;

/** 密码重置验证码实体（UC-04，映射 verify_code 表）。 */
public class PasswordReset {
    private int id;
    private String contactType;
    private String contactValue;
    private String code;
    private LocalDateTime expiresAt;
    private LocalDateTime lastSentAt;
    private boolean used;
    private String requestIp;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PasswordReset() {}

    public static PasswordReset create(String contactType, String contactValue, String code,
                                       LocalDateTime expiresAt, String requestIp) {
        PasswordReset p = new PasswordReset();
        p.contactType = contactType;
        p.contactValue = contactValue;
        p.code = code;
        p.expiresAt = expiresAt;
        p.lastSentAt = LocalDateTime.now();
        p.used = false;
        p.requestIp = requestIp;
        p.createdAt = LocalDateTime.now();
        p.updatedAt = LocalDateTime.now();
        return p;
    }

    public void markUsed() { this.used = true; this.updatedAt = LocalDateTime.now(); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getContactType() { return contactType; }
    public String getContactValue() { return contactValue; }
    public String getCode() { return code; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public LocalDateTime getLastSentAt() { return lastSentAt; }
    public boolean isUsed() { return used; }
    public String getRequestIp() { return requestIp; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCode(String code) { this.code = code; }
    public void setExpiresAt(LocalDateTime t) { this.expiresAt = t; }
}
