package tftc.lch.security.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tftc.lch.security.presentation.dto.request.LoginRequest;
import tftc.lch.security.presentation.dto.request.ResetPasswordRequest;
import tftc.lch.security.presentation.dto.request.RegisterRequest;
import tftc.lch.security.presentation.dto.request.SendResetCodeRequest;
import tftc.lch.security.presentation.dto.response.LoginResponse;

/**
 * 认证 Controller 接口（UC-01 至 UC-04）。
 * 由 Tester/Developer 实现，加 {@code @RestController}。
 *
 * @see <a href="../../../docs/architecture/arch01用户注册.html#api-POST-auth-register">POST /api/v1/auth/register</a>
 * @see <a href="../../../docs/architecture/arch02用户登入.html#api-POST-auth-login">POST /api/v1/auth/login</a>
 */
@RequestMapping("/api/v1/auth")
public interface AuthController {

    /**
     * 用户注册（UC-01）。见 {@code UC-01#M5}。
     *
     * @param request 注册入参
     * @return 201 Created
     */
    @PostMapping("/register")
    ResponseEntity<Void> register(@RequestBody RegisterRequest request);

    /**
     * 用户登入（UC-02），返回 JWT token。见 {@code UC-02#M5}。
     *
     * @param request 登入入参
     * @return 200 OK 含 LoginResponse（token）
     */
    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request);

    /**
     * 用户登出（UC-03）。见 {@code UC-03#M5}。
     *
     * @return 204 No Content
     */
    @PostMapping("/logout")
    ResponseEntity<Void> logout();

    /**
     * 发送密码重置验证码（UC-04 第一步）。见 {@code UC-04#M5}。
     *
     * @param request 联系方式类型 + 联系方式值
     * @return 201 Created（无论联系方式是否存在均返回，防账号枚举 NFR-SEC-06）
     * @see <a href="../../../docs/architecture/arch04忘记密码.html#api-POST-auth-password-reset-codes">POST /api/v1/auth/password-reset-codes</a>
     */
    @PostMapping("/password-reset-codes")
    ResponseEntity<Void> sendResetCode(@RequestBody SendResetCodeRequest request);

    /**
     * 执行密码重置（UC-04 第二步）。见 {@code UC-04#M5}。
     *
     * @param request 联系方式 + 验证码 + 新密码 + 确认密码
     * @return 204 No Content
     * @see <a href="../../../docs/architecture/arch04忘记密码.html#api-POST-auth-password-resets">POST /api/v1/auth/password-resets</a>
     */
    @PostMapping("/password-resets")
    ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request);
}
