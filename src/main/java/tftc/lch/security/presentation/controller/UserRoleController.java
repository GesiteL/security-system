package tftc.lch.security.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tftc.lch.security.presentation.dto.request.AssignRoleRequest;
import tftc.lch.security.presentation.dto.request.UpdateUserRoleRequest;
import tftc.lch.security.presentation.dto.response.PageResponse;
import tftc.lch.security.presentation.dto.response.UserRoleResponse;

/**
 * 用户角色 Controller 接口（UC-13 至 UC-16）。
 * 由 Tester/Developer 实现，加 {@code @RestController}。
 *
 * @see <a href="../../../docs/architecture/arch16移除用户角色.html#api-DELETE-users-id-roles-id">DELETE /api/v1/users/{userId}/roles/{userRoleId}</a>
 */
@RequestMapping("/api/v1/users/{userId}/roles")
public interface UserRoleController {

    /**
     * 查询用户角色列表（UC-13），支持多维过滤与分页。见 {@code UC-13#M5}。
     *
     * @param userId      目标用户 ID（路径参数）
     * @param displayName 用户姓名关键词（可选）
     * @param phone       手机号关键词（可选）
     * @param email       邮箱关键词（可选）
     * @param roleName    角色名称关键词（可选）
     * @param page        页码（默认 0）
     * @param pageSize    每页大小（默认 20）
     * @return 200 OK 含 PageResponse&lt;UserRoleResponse&gt;
     */
    @GetMapping
    ResponseEntity<PageResponse<UserRoleResponse>> listUserRoles(
            @PathVariable int userId,
            @RequestParam(required = false) String displayName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String roleName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize
    );

    @PostMapping
    ResponseEntity<UserRoleResponse> assignRole(@PathVariable int userId,
                                                  @RequestBody AssignRoleRequest request);

    @PatchMapping("/{userRoleId}")
    ResponseEntity<UserRoleResponse> updateUserRole(@PathVariable int userId,
                                                      @PathVariable int userRoleId,
                                                      @RequestBody UpdateUserRoleRequest request);

    @DeleteMapping("/{userRoleId}")
    ResponseEntity<Void> removeRole(@PathVariable int userId,
                                     @PathVariable int userRoleId);
}
