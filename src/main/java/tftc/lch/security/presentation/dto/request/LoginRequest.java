package tftc.lch.security.presentation.dto.request;

import tftc.lch.security.application.auth.LoginCommand;

/** 用户登入入参 DTO（UC-02）。 */
public class LoginRequest {
    private String account;
    private String password;

    public LoginCommand toCommand() {
        return new LoginCommand(account, password);
    }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
