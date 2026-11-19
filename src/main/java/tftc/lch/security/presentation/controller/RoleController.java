package tftc.lch.security.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tftc.lch.security.presentation.dto.request.CreateRoleRequest;
import tftc.lch.security.presentation.dto.request.UpdateRoleRequest;
import tftc.lch.security.presentation.dto.response.PageResponse;
import tftc.lch.security.presentation.dto.response.RoleResponse;

/**
 * 角色管理 Controller 接口（UC-09 至 UC-12）。
 * 由 Tester/Developer 实现，加 {@code @RestController}。
 *
 * @see <a href="../../../docs/architecture/arch09查询角色列表.html#api-GET-roles">GET /api/v1/roles</a>
 */
@RequestMapping("/api/v1/roles")
public interface RoleController {

    @GetMapping
    ResponseEntity<PageResponse<RoleResponse>> listRoles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String nameKeyword
    );

    @PostMapping
    ResponseEntity<RoleResponse> createRole(@RequestBody CreateRoleRequest request);

    @PatchMapping("/{roleId}")
    ResponseEntity<RoleResponse> updateRole(@PathVariable int roleId,
                                             @RequestBody UpdateRoleRequest request);

    @DeleteMapping("/{roleId}")
    ResponseEntity<Void> deleteRole(@PathVariable int roleId);
}
