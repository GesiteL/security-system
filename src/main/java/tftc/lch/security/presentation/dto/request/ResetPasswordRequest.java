package tftc.lch.security.presentation.dto.request;

import tftc.lch.security.application.auth.ResetPasswordCommand;

/** 密码重置入参 DTO（UC-04 第二步）。 */
public class ResetPasswordRequest {
    private String contactType;
    private String contactValue;
    private String verifyCode;
    private String newPassword;
    private String confirmPassword;

    public ResetPasswordCommand toCommand() {
        return new ResetPasswordCommand(contactType, contactValue, verifyCode, newPassword, confirmPassword);
    }

    public String getContactType() { return contactType; }
    public void setContactType(String contactType) { this.contactType = contactType; }
    public String getContactValue() { return contactValue; }
    public void setContactValue(String contactValue) { this.contactValue = contactValue; }
    public String getVerifyCode() { return verifyCode; }
    public void setVerifyCode(String verifyCode) { this.verifyCode = verifyCode; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
