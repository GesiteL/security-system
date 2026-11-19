package tftc.lch.security.domain.user;
import java.time.LocalDateTime;

public class User {
    private int id;
    private String account;
    private String passwordHash;
    private String realName;
    private String phone;
    private String email;
    private int status;
    private int credentialsVersion;
    private boolean isDeleted;
    private long deletedToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {}

    public static User create(String account, String passwordHash, String realName, String phone, String email) {
        User u = new User();
        u.account = account; u.passwordHash = passwordHash; u.realName = realName;
        u.phone = phone; u.email = email; u.status = 1; u.credentialsVersion = 1;
        u.isDeleted = false; u.deletedToken = 0; u.createdAt = LocalDateTime.now(); u.updatedAt = LocalDateTime.now();
        return u;
    }
    public void softDelete() { isDeleted = true; deletedToken = id; updatedAt = LocalDateTime.now(); }
    public void incrementCredentialsVersion() { credentialsVersion++; updatedAt = LocalDateTime.now(); }
    public void updateInfo(String realName, String phone, String email, Boolean disabled) {
        if (realName != null) this.realName = realName;
        if (phone != null) this.phone = phone;
        if (email != null) this.email = email;
        if (disabled != null) this.status = disabled ? 0 : 1;
        this.updatedAt = LocalDateTime.now();
    }
    public int getId() { return id; } public void setId(int v) { id = v; }
    public String getAccount() { return account; } public void setAccount(String v) { account = v; }
    public String getPasswordHash() { return passwordHash; } public void setPasswordHash(String v) { passwordHash = v; }
    public String getRealName() { return realName; } public void setRealName(String v) { realName = v; }
    public String getPhone() { return phone; } public void setPhone(String v) { phone = v; }
    public String getEmail() { return email; } public void setEmail(String v) { email = v; }
    public int getStatus() { return status; } public void setStatus(int v) { status = v; }
    public boolean isDisabled() { return status == 0; }
    public int getCredentialsVersion() { return credentialsVersion; } public void setCredentialsVersion(int v) { credentialsVersion = v; }
    public boolean isDeleted() { return isDeleted; } public void setDeleted(boolean v) { isDeleted = v; }
    public long getDeletedToken() { return deletedToken; } public void setDeletedToken(long v) { deletedToken = v; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime v) { createdAt = v; }
    public LocalDateTime getUpdatedAt() { return updatedAt; } public void setUpdatedAt(LocalDateTime v) { updatedAt = v; }
}