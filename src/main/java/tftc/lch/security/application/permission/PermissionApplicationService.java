package tftc.lch.security.application.permission;

import tftc.lch.security.domain.permission.SysFunction;
import tftc.lch.security.domain.role.RolePermission;
import java.util.List;

/**
 * 系统功能与角色权限管理应用服务接口（UC-17 至 UC-24）。
 *
 * @see <a href="../../../docs/architecture/arch22授予角色权限.html#seq-授予角色权限">授予权限时序图</a>
 * @see <a href="../../../docs/architecture/arch24撤销角色权限.html#seq-撤销角色权限">撤销权限时序图</a>
 */
public interface PermissionApplicationService {

    /**
     * 查询系统功能列表（UC-17）。见 {@code UC-17#M5}。
     *
     * @param query 查询条件
     * @return 系统功能列表
     */
    List<SysFunction> listFunctions(GetFunctionQuery query);

    /** 统计系统功能总数（UC-17 分页支持）。 */
    int countFunctions(GetFunctionQuery query);

    /**
     * 注册系统功能（UC-18）。见 {@code UC-18#M5}。
     *
     * @param cmd 注册指令
     * @return 创建成功的系统功能聚合根
     * @throws tftc.lch.security.domain.shared.DomainException 见 {@code UC-18#E6}
     */
    SysFunction registerFunction(RegisterFunctionCommand cmd);

    /**
     * 编辑系统功能（UC-19）。见 {@code UC-19#M5}。
     *
     * @param cmd 更新指令
     * @return 更新后的系统功能聚合根
     */
    SysFunction updateFunction(UpdateFunctionCommand cmd);

    /**
     * 删除系统功能（UC-20）：软删除（BR-25）并级联将关联权限配置标记为失效。见 {@code UC-20#M5}。
     *
     * @param cmd 删除指令
     */
    void deleteFunction(DeleteFunctionCommand cmd);

    /**
     * 查询角色权限列表（UC-21），支持按功能名称过滤。见 {@code UC-21#M5}。
     *
     * @param roleId              角色 ID
     * @param functionNameKeyword 功能名称关键词（null 表示不过滤）
     * @return 角色权限授予列表
     */
    List<RolePermission> listRolePermissions(int roleId, String functionNameKeyword);

    /**
     * 为角色授予权限（UC-22）；含有效期（BR-24）。见 {@code UC-22#M5}。
     *
     * @param cmd 授予指令
     * @return 创建的授权记录
     * @throws tftc.lch.security.domain.shared.DomainException 见 {@code UC-22#E6}
     */
    RolePermission grantPermission(GrantPermissionCommand cmd);

    /**
     * 修改角色权限（UC-23）：全量替换策略，差异计算后新增缺少项、将多余项标记为失效（BR-25）。
     * 见 {@code UC-23#M5}。
     *
     * @param cmd 调整指令（含完整新权限集合）
     * @return 本次有效的全部授权记录
     * @throws tftc.lch.security.domain.shared.DomainException BR-24，见 {@code UC-23#E6}
     */
    List<RolePermission> adjustRolePermissions(AdjustRolePermissionsCommand cmd);

    /**
     * 撤销角色权限（UC-24）：软删除（BR-25）。见 {@code UC-24#M5}。
     *
     * @param cmd 撤销指令
     */
    void revokePermission(RevokePermissionCommand cmd);
}
