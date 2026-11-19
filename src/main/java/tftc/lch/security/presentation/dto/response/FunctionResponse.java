package tftc.lch.security.presentation.dto.response;

import tftc.lch.security.domain.permission.SysFunction;
import java.time.LocalDateTime;

/** 系统功能响应 DTO。 */
public class FunctionResponse {
    private int id;
    private String functionName;
    private String description;
    private String module;
    private LocalDateTime planEffectiveAt;
    private LocalDateTime planExpiredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FunctionResponse fromDomain(SysFunction f) {
        FunctionResponse r = new FunctionResponse();
        r.id = f.getId();
        r.functionName = f.getFunctionName();
        r.description = f.getDescription();
        r.module = f.getModule();
        r.planEffectiveAt = f.getPlanEffectiveAt();
        r.planExpiredAt = f.getPlanExpiredAt();
        r.createdAt = f.getCreatedAt();
        r.updatedAt = f.getUpdatedAt();
        return r;
    }

    public int getId() { return id; }
    public String getFunctionName() { return functionName; }
    public String getDescription() { return description; }
    public String getModule() { return module; }
    public LocalDateTime getPlanEffectiveAt() { return planEffectiveAt; }
    public LocalDateTime getPlanExpiredAt() { return planExpiredAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
