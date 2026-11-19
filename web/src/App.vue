<template>
  <div v-if="$route.path === '/login'" class="app-root">
    <router-view />
  </div>
  <el-container v-else class="app-root">
    <el-aside width="200px">
      <div class="brand">🛡️ 信息安全管理</div>
      <el-menu :default-active="$route.path" router class="menu">
        <el-menu-item index="/users">用户管理</el-menu-item>
        <el-menu-item index="/roles">角色管理</el-menu-item>
        <el-menu-item index="/functions">功能管理</el-menu-item>
        <el-menu-item index="/permissions">权限分配</el-menu-item>
        <el-menu-item index="/user-roles">用户角色</el-menu-item>
        <el-menu-item index="/audit">审计中心</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span class="title">{{ $route.meta.title || '信息安全管理系统' }}</span>
        <el-dropdown @command="onCommand">
          <span class="account">{{ account }} ▾</span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main><router-view /></el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { logout } from './api/auth'

const router = useRouter()
const account = ref(localStorage.getItem('sec-admin-account') || 'admin')

const onCommand = async (cmd) => {
  if (cmd !== 'logout') return
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', { type: 'warning' })
    try { await logout() } catch (e) { /* 后端登出失败不阻断本地退出 */ }
    localStorage.removeItem('sec-admin-token')
    localStorage.removeItem('sec-admin-account')
    ElMessage.success('已退出登录')
    router.push('/login')
  } catch (e) { /* 用户取消 */ }
}
</script>

<style>
html, body, #app { height: 100%; margin: 0; }
.app-root { height: 100vh; }
.menu { height: 100%; border-right: none; }
.brand { height: 56px; line-height: 56px; text-align: center; font-weight: 700; color: #fff; background: #1f2d3d; }
.header { display: flex; justify-content: space-between; align-items: center; background: #409eff; color: #fff; }
.title { font-size: 16px; font-weight: 600; }
.account { color: #fff; cursor: pointer; }
.pager { margin-top: 14px; display: flex; justify-content: flex-end; }
</style>
