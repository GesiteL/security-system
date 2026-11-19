package tftc.lch.security.presentation.dto.request;
import tftc.lch.security.application.user.UpdateUserCommand;

public class UpdateUserRequest {
    private String realName;
    private String phone;
    private String email;
    private Boolean disabled;
    public UpdateUserCommand toCommand(int targetUserId, int operatorId) {
        return new UpdateUserCommand(targetUserId, realName, phone, email, disabled, operatorId);
    }
    public String getRealName() { return realName; } public void setRealName(String v) { realName = v; }
    public String getPhone() { return phone; } public void setPhone(String v) { phone = v; }
    public String getEmail() { return email; } public void setEmail(String v) { email = v; }
    public Boolean getDisabled() { return disabled; } public void setDisabled(Boolean v) { disabled = v; }
}