import request from '../utils/request'

// 查询用户已分配角色：GET /api/v1/users/{userId}/roles（displayName/phone/email/roleName 筛选 + 分页）
export function listUserRoles(userId, params) {
  return request.get(`/users/${userId}/roles`, { params })
}

// 给用户分配角色：POST /api/v1/users/{userId}/roles {roleId,planEffectiveAt,planExpiredAt}
export function assignRole(userId, data) {
  return request.post(`/users/${userId}/roles`, data)
}

// 修改用户角色生效期：PATCH /api/v1/users/{userId}/roles/{userRoleId} {planEffectiveAt,planExpiredAt}
export function updateUserRole(userId, userRoleId, data) {
  return request.patch(`/users/${userId}/roles/${userRoleId}`, data)
}

// 移除用户角色：DELETE /api/v1/users/{userId}/roles/{userRoleId}
export function removeUserRole(userId, userRoleId) {
  return request.delete(`/users/${userId}/roles/${userRoleId}`)
}
