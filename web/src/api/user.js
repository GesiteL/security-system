import request from '../utils/request'

// 查询用户列表：GET /api/v1/users（accountKeyword/realNameKeyword/phoneKeyword 三筛选）
export function listUsers(params) {
  return request.get('/users', { params })
}

// 创建用户账号：POST /api/v1/users {account,password,realName,phone,email}
export function createUser(data) {
  return request.post('/users', data)
}

// 修改用户资料：PATCH /api/v1/users/{userId} {realName,phone,email,disabled}
export function updateUser(userId, data) {
  return request.patch(`/users/${userId}`, data)
}
