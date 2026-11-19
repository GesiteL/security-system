import request from '../utils/request'

// 查询角色列表：GET /api/v1/roles（nameKeyword 筛选）
export function listRoles(params) {
  return request.get('/roles', { params })
}

// 创建角色：POST /api/v1/roles {roleName,description}
export function createRole(data) {
  return request.post('/roles', data)
}

// 修改角色：PATCH /api/v1/roles/{roleId} {roleName,description}
export function updateRole(roleId, data) {
  return request.patch(`/roles/${roleId}`, data)
}
