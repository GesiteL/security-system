package tftc.lch.security.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tftc.lch.security.presentation.dto.request.CreateUserRequest;
import tftc.lch.security.presentation.dto.request.UpdateUserRequest;
import tftc.lch.security.presentation.dto.response.PageResponse;
import tftc.lch.security.presentation.dto.response.UserResponse;

/**
 * 用户管理 Controller 接口（UC-05 至 UC-08）。
 * 由 Tester/Developer 实现，加 {@code @RestController}。
 *
 * @see <a href="../../../docs/architecture/arch05查询用户列表.html#api-GET-users">GET /api/v1/users</a>
 * @see <a href="../../../docs/architecture/arch08删除用户.html#api-DELETE-users-id">DELETE /api/v1/users/{userId}</a>
 */
@RequestMapping("/api/v1/users")
public interface UserController {

    /**
     * 查询用户列表（UC-05）。见 {@code UC-05#M5}。
     *
     * @param page     页码（从 1 开始）
     * @param pageSize 每页大小
     * @return 200 OK 含 PageResponse&lt;UserResponse&gt;
     */
    @GetMapping
    ResponseEntity<PageResponse<UserResponse>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String accountKeyword,
            @RequestParam(required = false) String realNameKeyword,
            @RequestParam(required = false) String phoneKeyword
    );

    /**
     * 创建用户（UC-06）。见 {@code UC-06#M5}。
     *
     * @param request 创建入参
     * @return 201 Created 含 UserResponse
     */
    @PostMapping
    ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request);

    /**
     * 编辑用户信息（UC-07）。见 {@code UC-07#M5}。
     *
     * @param userId  目标用户 ID
     * @param request 更新入参
     * @return 200 OK 含 UserResponse
     */
    @PatchMapping("/{userId}")
    ResponseEntity<UserResponse> updateUser(@PathVariable int userId,
                                             @RequestBody UpdateUserRequest request);

    /**
     * 删除用户（UC-08）：软删除（BR-25）+ 凭证失效（BR-07）。见 {@code UC-08#M5}。
     *
     * @param userId 目标用户 ID
     * @return 204 No Content
     */
    @DeleteMapping("/{userId}")
    ResponseEntity<Void> deleteUser(@PathVariable int userId);
}
