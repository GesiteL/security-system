package tftc.lch.security.application.userrole;

import tftc.lch.security.domain.user.UserRole;
import java.util.List;

/**
 * 用户角色管理应用服务接口（UC-13 至 UC-16）。
 *
 * @see <a href="../../../docs/architecture/arch14分配用户角色.html#seq-分配用户角色">分配角色时序图</a>
 * @see <a href="../../../docs/architecture/arch16移除用户角色.html#seq-移除用户角色">移除角色时序图</a>
 */
public interface UserRoleApplicationService {

    /**
     * 查询用户角色列表（UC-13），支持多维过滤与分页。见 {@code UC-13#M5}。
     *
     * @param query 查询条件（含 userId、过滤字段、分页参数）
     * @return 用户角色绑定列表
     */
    List<UserRole> listUserRoles(GetUserRoleQuery query);

    /**
     * 统计用户角色总数（UC-13 分页支持）。
     *
     * @param query 查询条件
     * @return 满足条件的绑定记录总数
     */
    int countUserRoles(GetUserRoleQuery query);

    /**
     * 为用户分配角色（UC-14）。见 {@code UC-14#M5}。
     *
     * @param cmd 分配指令（含有效期 BR-24）
     * @return 创建的绑定记录
     * @throws tftc.lch.security.domain.shared.DomainException 见 {@code UC-14#E6}
     */
    UserRole assignRole(AssignUserRoleCommand cmd);

    /**
     * 修改用户角色有效期（UC-15）。见 {@code UC-15#M5}。
     *
     * @param cmd 更新指令
     * @return 更新后的绑定记录
     */
    UserRole updateUserRole(UpdateUserRoleCommand cmd);

    /**
     * 移除用户角色绑定（UC-16）：软删除（BR-25）。见 {@code UC-16#M5}。
     *
     * @param cmd 移除指令
     */
    void removeUserRole(RemoveUserRoleCommand cmd);
}
