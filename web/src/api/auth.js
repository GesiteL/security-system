import request from '../utils/request'

// 登录（UC-02）：POST /api/v1/auth/login {account,password} → {token,userId,account}
export function login(data) {
  return request.post('/auth/login', data)
}

// 登出（UC-03）：POST /api/v1/auth/logout
export function logout() {
  return request.post('/auth/logout')
}
