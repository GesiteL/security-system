import request from '../utils/request'

// 分页查询审计日志（改造新增读取侧）：GET /api/v1/audit-logs?page&pageSize
export function listAuditLogs(params) {
  return request.get('/audit-logs', { params })
}
