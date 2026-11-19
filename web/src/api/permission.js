import request from '../utils/request'

// 查询角色已授功能：GET /api/v1/roles/{roleId}/permissions（functionNameKeyword 筛选）
export function listRolePermissions(roleId, params) {
  return request.get(`/roles/${roleId}/permissions`, { params })
}

// 授予角色功能：POST /api/v1/roles/{roleId}/permissions {functionId,planEffectiveAt,planExpiredAt}
export function grantPermission(roleId, data) {
  return request.post(`/roles/${roleId}/permissions`, data)
}

// 批量调整角色权限：PUT /api/v1/roles/{roleId}/permissions {permissions:[{functionId,planEffectiveAt,planExpiredAt}]}
export function adjustRolePermissions(roleId, data) {
  return request.put(`/roles/${roleId}/permissions`, data)
}

// 回收角色功能：DELETE /api/v1/roles/{roleId}/permissions/{grantId}
export function revokePermission(roleId, grantId) {
  return request.delete(`/roles/${roleId}/permissions/${grantId}`)
}
