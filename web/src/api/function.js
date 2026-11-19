import request from '../utils/request'

// 查询系统功能列表：GET /api/v1/functions（nameKeyword/module 筛选）
export function listFunctions(params) {
  return request.get('/functions', { params })
}

// 注册系统功能：POST /api/v1/functions {functionName,description,module,planEffectiveAt,planExpiredAt}
export function createFunction(data) {
  return request.post('/functions', data)
}

// 修改系统功能：PATCH /api/v1/functions/{functionId}
export function updateFunction(functionId, data) {
  return request.patch(`/functions/${functionId}`, data)
}
