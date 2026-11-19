package tftc.lch.security.application.auth;

import tftc.lch.security.domain.user.User;

/**
 * 认证应用服务接口（UC-01 至 UC-04）。
 *
 * @see <a href="../../../docs/architecture/arch01用户注册.html#seq-用户注册">注册时序图</a>
 * @see <a href="../../../docs/architecture/arch02用户登入.html#seq-用户登入">登入时序图</a>
 */
public interface AuthApplicationService {

    /**
     * 用户注册（UC-01）。见 {@code UC-01#M5}。
     *
     * @param cmd 注册指令
     * @return 注册成功的用户聚合根
     * @throws tftc.lch.security.domain.shared.DomainException BR-01，见 {@code UC-01#E6#3a}
     */
    User register(RegisterCommand cmd);

    /**
     * 用户登入（UC-02），返回 JWT token。见 {@code UC-02#M5}。
     *
     * @param cmd 登入指令
     * @return JWT token 字符串
     * @throws tftc.lch.security.domain.shared.DomainException BR-05，见 {@code UC-02#E6}
     */
    String login(LoginCommand cmd);

    /**
     * 用户登出（UC-03），credentials_version 自增使凭证失效（BR-07）。见 {@code UC-03#M5}。
     *
     * @param cmd 登出指令
     */
    void logout(LogoutCommand cmd);

    /**
     * 发送密码重置验证码（UC-04 第一步）。见 {@code UC-04#M5}。
     * 频率限制：同一账号5分钟内最多1次（BR-08）。
     *
     * @param cmd 发送验证码指令（联系方式类型 + 联系方式值）
     */
    void sendPasswordResetCode(ForgotPasswordCommand cmd);

    /**
     * 执行密码重置（UC-04 第二步）。见 {@code UC-04#M5}。
     * 重置成功后 credentials_version 自增（BR-07）。
     *
     * @param cmd 重置指令（联系方式 + 验证码 + 新密码 + 确认密码）
     * @throws tftc.lch.security.domain.shared.DomainException BR-02/BR-08，见 {@code UC-04#E6}
     */
    void resetPassword(ResetPasswordCommand cmd);
}
