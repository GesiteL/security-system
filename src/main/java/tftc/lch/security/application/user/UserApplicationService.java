package tftc.lch.security.application.user;

import tftc.lch.security.domain.user.User;
import java.util.List;

/**
 * 用户管理应用服务接口（UC-05 至 UC-08）。
 *
 * @see <a href="../../../docs/architecture/arch06创建用户.html#seq-创建用户">创建用户时序图</a>
 * @see <a href="../../../docs/architecture/arch08删除用户.html#seq-删除用户">删除用户时序图</a>
 */
public interface UserApplicationService {

    /**
     * 查询用户列表（UC-05）。见 {@code UC-05#M5}。
     *
     * @param query 查询条件
     * @return 用户列表
     */
    List<User> listUsers(GetUserQuery query);

    /**
     * 统计用户总数（UC-05 分页支持）。
     *
     * @param query 查询条件
     * @return 用户总数
     */
    int countUsers(GetUserQuery query);

    /**
     * 管理员创建用户（UC-06）。见 {@code UC-06#M5}。
     *
     * @param cmd 创建用户指令
     * @return 创建成功的用户聚合根
     * @throws tftc.lch.security.domain.shared.DomainException BR-01/BR-04，见 {@code UC-06#E6}
     */
    User createUser(CreateUserCommand cmd);

    /**
     * 编辑用户信息（UC-07）。见 {@code UC-07#M5}。
     *
     * @param cmd 更新指令
     * @return 更新后的用户聚合根
     * @throws tftc.lch.security.domain.shared.DomainException BR-04，见 {@code UC-07#E6}
     */
    User updateUser(UpdateUserCommand cmd);

    /**
     * 删除用户（UC-08）：软删除 + 凭证失效（BR-25, BR-07）。见 {@code UC-08#M5}。
     *
     * @param cmd 删除指令（含操作者 ID，用于 BR-10 自我删除检查）
     * @throws tftc.lch.security.domain.shared.DomainException BR-10，见 {@code UC-08#E6#3a}
     */
    void deleteUser(DeleteUserCommand cmd);
}
