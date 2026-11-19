package tftc.lch.security.presentation.dto.request;

import tftc.lch.security.application.auth.ForgotPasswordCommand;

/** 发送密码重置验证码入参 DTO（UC-04 第一步）。 */
public class SendResetCodeRequest {
    private String contactType;   // "phone" 或 "email"
    private String contactValue;

    public ForgotPasswordCommand toCommand() {
        return new ForgotPasswordCommand(contactType, contactValue);
    }

    public String getContactType() { return contactType; }
    public void setContactType(String contactType) { this.contactType = contactType; }
    public String getContactValue() { return contactValue; }
    public void setContactValue(String contactValue) { this.contactValue = contactValue; }
}
