<template>
  <div class="login-wrap">
    <el-card class="login-card">
      <h2 class="login-title">🛡️ 信息安全管理系统 · 管理端</h2>
      <el-alert title="演示账号：admin / admin（种子数据）" type="info" :closable="false" style="margin-bottom:16px;" />
      <el-form :model="form" :rules="rules" ref="formRef" label-width="0" @keyup.enter="submit">
        <el-form-item prop="account">
          <el-input v-model="form.account" placeholder="账号" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" size="large" show-password />
        </el-form-item>
        <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="submit">登 录</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../api/auth'

const router = useRouter()
const route = useRoute()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ account: '', password: '' })
const rules = {
  account: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const submit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const res = await login({ account: form.account, password: form.password })
      localStorage.setItem('sec-admin-token', res.token)
      localStorage.setItem('sec-admin-account', res.account || form.account)
      ElMessage.success('登录成功')
      router.push(route.query.redirect || '/users')
    } catch (e) {
      ElMessage.error(e.message || '登录失败')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-wrap { display: flex; justify-content: center; align-items: center; height: 100vh; background: linear-gradient(135deg, #2b5876, #4e4376); }
.login-card { width: 420px; padding: 12px 8px; }
.login-title { text-align: center; margin: 4px 0 18px; }
</style>
