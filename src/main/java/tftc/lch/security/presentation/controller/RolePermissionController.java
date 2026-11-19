package tftc.lch.security.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tftc.lch.security.presentation.dto.request.AdjustRolePermissionsRequest;
import tftc.lch.security.presentation.dto.request.GrantPermissionRequest;
import tftc.lch.security.presentation.dto.response.PermissionResponse;
import java.util.List;

/**
 * 角色权限 Controller 接口（UC-21 至 UC-24）。
 * 由 Tester/Developer 实现，加 {@code @RestController}。
 *
 * @see <a href="../../../docs/architecture/arch24撤销角色权限.html#api-DELETE-roles-id-permissions-id">DELETE /api/v1/roles/{roleId}/permissions/{grantId}</a>
 */
@RequestMapping("/api/v1/roles/{roleId}/permissions")
public interface RolePermissionController {

    /**
     * 查询角色权限列表（UC-21），支持按功能名称过滤。见 {@code UC-21#M5}。
     */
    @GetMapping
    ResponseEntity<List<PermissionResponse>> listRolePermissions(
            @PathVariable int roleId,
            @RequestParam(required = false) String functionNameKeyword
    );

    /**
     * 为角色授予权限（UC-22）。见 {@code UC-22#M5}。
     */
    @PostMapping
    ResponseEntity<PermissionResponse> grantPermission(@PathVariable int roleId,
                                                        @RequestBody GrantPermissionRequest request);

    /**
     * 修改角色权限（UC-23）：全量替换，差异计算后新增/标记失效（BR-25）。见 {@code UC-23#M5}。
     *
     * @return 200 OK 含本次有效的全部授权记录
     */
    @PutMapping
    ResponseEntity<List<PermissionResponse>> adjustRolePermissions(
            @PathVariable int roleId,
            @RequestBody AdjustRolePermissionsRequest request
    );

    /**
     * 撤销角色权限（UC-24）：软删除（BR-25）。见 {@code UC-24#M5}。
     */
    @DeleteMapping("/{grantId}")
    ResponseEntity<Void> revokePermission(@PathVariable int roleId,
                                          @PathVariable int grantId);
}
