package tftc.lch.security.application.role;

import tftc.lch.security.domain.role.Role;
import java.util.List;

/**
 * 角色管理应用服务接口（UC-09 至 UC-12）。
 *
 * @see <a href="../../../docs/architecture/arch10创建角色.html#seq-创建角色">创建角色时序图</a>
 */
public interface RoleApplicationService {

    /**
     * 查询角色列表（UC-09）。见 {@code UC-09#M5}。
     *
     * @param query 查询条件
     * @return 角色列表
     */
    List<Role> listRoles(GetRoleQuery query);

    /** 统计角色总数（UC-09 分页支持）。 */
    int countRoles(GetRoleQuery query);

    /**
     * 创建角色（UC-10）。见 {@code UC-10#M5}。
     *
     * @param cmd 创建角色指令
     * @return 创建成功的角色聚合根
     * @throws tftc.lch.security.domain.shared.DomainException 见 {@code UC-10#E6}
     */
    Role createRole(CreateRoleCommand cmd);

    /**
     * 编辑角色（UC-11）。见 {@code UC-11#M5}。
     *
     * @param cmd 更新指令
     * @return 更新后的角色聚合根
     */
    Role updateRole(UpdateRoleCommand cmd);

    /**
     * 删除角色（UC-12）：软删除（BR-25）。见 {@code UC-12#M5}。
     *
     * @param cmd 删除指令
     */
    void deleteRole(DeleteRoleCommand cmd);
}
