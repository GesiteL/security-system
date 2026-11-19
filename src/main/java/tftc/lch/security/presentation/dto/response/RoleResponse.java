package tftc.lch.security.presentation.dto.response;

import tftc.lch.security.domain.role.Role;
import java.time.LocalDateTime;

/** 角色响应 DTO。 */
public class RoleResponse {
    private int id;
    private String roleName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RoleResponse fromDomain(Role role) {
        RoleResponse r = new RoleResponse();
        r.id = role.getId();
        r.roleName = role.getRoleName();
        r.description = role.getDescription();
        r.createdAt = role.getCreatedAt();
        r.updatedAt = role.getUpdatedAt();
        return r;
    }

    public int getId() { return id; }
    public String getRoleName() { return roleName; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
