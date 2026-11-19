import { createRouter, createWebHashHistory } from 'vue-router'
import Login from '../views/Login.vue'
import User from '../views/User.vue'
import Role from '../views/Role.vue'
import Function from '../views/Function.vue'
import Permission from '../views/Permission.vue'
import UserRole from '../views/UserRole.vue'
import Audit from '../views/Audit.vue'

const routes = [
  { path: '/', redirect: '/users' },
  { path: '/login', name: 'Login', component: Login },
  { path: '/users', name: 'User', component: User, meta: { title: '用户管理', requiresAuth: true } },
  { path: '/roles', name: 'Role', component: Role, meta: { title: '角色管理', requiresAuth: true } },
  { path: '/functions', name: 'Function', component: Function, meta: { title: '功能管理', requiresAuth: true } },
  { path: '/permissions', name: 'Permission', component: Permission, meta: { title: '权限分配', requiresAuth: true } },
  { path: '/user-roles', name: 'UserRole', component: UserRole, meta: { title: '用户角色', requiresAuth: true } },
  { path: '/audit', name: 'Audit', component: Audit, meta: { title: '审计中心', requiresAuth: true } }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 路由守卫：无 token 一律回登录页
router.beforeEach((to) => {
  if (to.meta.requiresAuth && !localStorage.getItem('sec-admin-token')) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (to.path === '/login' && localStorage.getItem('sec-admin-token')) {
    return { path: '/users' }
  }
})

export default router
