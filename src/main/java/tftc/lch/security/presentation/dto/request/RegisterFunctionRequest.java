package tftc.lch.security.presentation.dto.request;

import tftc.lch.security.application.permission.RegisterFunctionCommand;
import java.time.LocalDateTime;

/** 注册系统功能入参 DTO（UC-18）。 */
public class RegisterFunctionRequest {
    private String functionName;
    private String description;
    private String module;
    private LocalDateTime planEffectiveAt;
    private LocalDateTime planExpiredAt;

    public RegisterFunctionCommand toCommand(int operatorId) {
        return new RegisterFunctionCommand(functionName, description, module,
                planEffectiveAt, planExpiredAt, operatorId);
    }

    public String getFunctionName() { return functionName; }
    public void setFunctionName(String functionName) { this.functionName = functionName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public LocalDateTime getPlanEffectiveAt() { return planEffectiveAt; }
    public void setPlanEffectiveAt(LocalDateTime planEffectiveAt) { this.planEffectiveAt = planEffectiveAt; }
    public LocalDateTime getPlanExpiredAt() { return planExpiredAt; }
    public void setPlanExpiredAt(LocalDateTime planExpiredAt) { this.planExpiredAt = planExpiredAt; }
}
