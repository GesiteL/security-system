import axios from 'axios'
import router from '../router'

// axios 实例：统一前缀 /api/v1，请求拦截注 Bearer，响应 401 清 token 跳登录
const request = axios.create({
  baseURL: '/api/v1',
  timeout: 10000
})

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('sec-admin-token')
  if (token) config.headers['Authorization'] = `Bearer ${token}`
  return config
})

request.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const status = error.response ? error.response.status : 0
    if (status === 401) {
      localStorage.removeItem('sec-admin-token')
      localStorage.removeItem('sec-admin-account')
      if (router.currentRoute.value.path !== '/login') {
        router.push({ path: '/login', query: { redirect: router.currentRoute.value.fullPath } })
      }
    }
    const msg = error.response && error.response.data && error.response.data.message
      ? error.response.data.message
      : (error.message || '网络错误')
    return Promise.reject(new Error(msg))
  }
)

export default request
