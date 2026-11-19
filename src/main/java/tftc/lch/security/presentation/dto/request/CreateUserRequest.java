package tftc.lch.security.presentation.dto.request;
import tftc.lch.security.application.user.CreateUserCommand;

public class CreateUserRequest {
    private String account;
    private String password;
    private String realName;
    private String phone;
    private String email;

    public CreateUserCommand toCommand(int operatorId) {
        return new CreateUserCommand(account, password, realName, phone, email, operatorId);
    }
    public String getAccount() { return account; } public void setAccount(String v) { account = v; }
    public String getPassword() { return password; } public void setPassword(String v) { password = v; }
    public String getRealName() { return realName; } public void setRealName(String v) { realName = v; }
    public String getPhone() { return phone; } public void setPhone(String v) { phone = v; }
    public String getEmail() { return email; } public void setEmail(String v) { email = v; }
}
