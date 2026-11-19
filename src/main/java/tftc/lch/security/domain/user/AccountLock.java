package tftc.lch.security.domain.user;

import java.time.LocalDateTime;

public class AccountLock {
    private int id;
    private String account;
    private int failCount;
    private LocalDateTime lockedUntil;
    private LocalDateTime lastAttemptAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccountLock() {}

    public static AccountLock create(String account) {
        AccountLock a = new AccountLock();
        a.account = account; a.failCount = 1; a.lastAttemptAt = LocalDateTime.now();
        a.createdAt = LocalDateTime.now(); a.updatedAt = LocalDateTime.now();
        return a;
    }
    public void incrementFail() { this.failCount++; this.lastAttemptAt = LocalDateTime.now(); if (this.failCount >= 5) this.lockedUntil = LocalDateTime.now().plusMinutes(30); }
    public void reset() { this.failCount = 0; this.lockedUntil = null; }
    public int getId() { return id; }
    public String getAccount() { return account; }
    public int getFailCount() { return failCount; }
    public LocalDateTime getLockedUntil() { return lockedUntil; }
    public LocalDateTime getLastAttemptAt() { return lastAttemptAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
