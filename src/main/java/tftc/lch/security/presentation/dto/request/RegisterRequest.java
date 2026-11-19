package tftc.lch.security.presentation.dto.request;

import tftc.lch.security.application.auth.RegisterCommand;

/** 用户注册入参 DTO（UC-01）。 */
public class RegisterRequest {
    private String account;
    private String password;
    private String realName;
    private String phone;
    private String email;

    public RegisterCommand toCommand() {
        return new RegisterCommand(account, password, realName, phone, email);
    }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
