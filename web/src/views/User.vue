<template>
  <el-card>
    <el-form inline>
      <el-form-item label="账号"><el-input v-model="q.accountKeyword" placeholder="账号" clearable style="width:140px" /></el-form-item>
      <el-form-item label="姓名"><el-input v-model="q.realNameKeyword" placeholder="姓名" clearable style="width:140px" /></el-form-item>
      <el-form-item label="手机"><el-input v-model="q.phoneKeyword" placeholder="手机号" clearable style="width:140px" /></el-form-item>
      <el-form-item><el-button type="primary" @click="load(1)">查询</el-button></el-form-item>
      <el-form-item><el-button @click="reset">重置</el-button></el-form-item>
      <el-form-item><el-button type="success" @click="openCreate">新增账号</el-button></el-form-item>
    </el-form>

    <el-table :data="rows" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="username" label="账号" min-width="110" />
      <el-table-column prop="displayName" label="姓名" min-width="110" />
      <el-table-column prop="phone" label="手机" width="130" />
      <el-table-column prop="email" label="邮箱" min-width="170" show-overflow-tooltip />
      <el-table-column label="状态" width="90">
        <template #default="{ row }"><el-tag :type="row.disabled ? 'danger' : 'success'">{{ row.disabled ? '停用' : '正常' }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="160" />
      <el-table-column label="操作" width="90" fixed="right">
        <template #default="{ row }"><el-button size="small" @click="openEdit(row)">编辑</el-button></template>
      </el-table-column>
    </el-table>
    <div class="pager">
      <el-pagination background layout="total, prev, pager, next, sizes" :total="total"
        v-model:current-page="page" v-model:page-size="pageSize" :page-sizes="[10, 20, 50]"
        @current-change="load()" @size-change="load(1)" />
    </div>

    <el-dialog v-model="dialog" :title="editing ? '编辑用户' : '新增账号'" width="480px" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="账号" required><el-input v-model="form.account" :disabled="editing" /></el-form-item>
        <el-form-item v-if="!editing" label="密码" required><el-input v-model="form.password" show-password /></el-form-item>
        <el-form-item label="姓名" required><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="手机" required><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="邮箱" required><el-input v-model="form.email" /></el-form-item>
        <el-form-item v-if="editing" label="状态">
          <el-switch v-model="form.disabled" active-text="停用" inactive-text="正常" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listUsers, createUser, updateUser } from '../api/user'

const rows = ref([]); const total = ref(0); const page = ref(1); const pageSize = ref(20)
const loading = ref(false); const saving = ref(false); const dialog = ref(false); const editing = ref(false)
const q = reactive({ accountKeyword: '', realNameKeyword: '', phoneKeyword: '' })
const form = reactive({ id: null, account: '', password: '', realName: '', phone: '', email: '', disabled: false })

const load = async (p) => {
  if (p) page.value = p
  loading.value = true
  try {
    const res = await listUsers({ page: page.value, pageSize: pageSize.value, ...clean(q) })
    rows.value = res.items || []
    total.value = res.total || 0
  } catch (e) { ElMessage.error(e.message) } finally { loading.value = false }
}
const clean = (o) => Object.fromEntries(Object.entries(o).filter(([, v]) => v !== '' && v !== null && v !== undefined))

const reset = () => { q.accountKeyword = ''; q.realNameKeyword = ''; q.phoneKeyword = ''; load(1) }
const openCreate = () => { editing.value = false; Object.assign(form, { id: null, account: '', password: '', realName: '', phone: '', email: '', disabled: false }); dialog.value = true }
const openEdit = (row) => {
  editing.value = true
  Object.assign(form, { id: row.id, account: row.username, password: '', realName: row.displayName, phone: row.phone, email: row.email, disabled: row.disabled })
  dialog.value = true
}
const save = async () => {
  saving.value = true
  try {
    if (editing.value) {
      await updateUser(form.id, { realName: form.realName, phone: form.phone, email: form.email, disabled: form.disabled })
      ElMessage.success('修改成功')
    } else {
      await createUser({ account: form.account, password: form.password, realName: form.realName, phone: form.phone, email: form.email })
      ElMessage.success('新增成功')
    }
    dialog.value = false
    load()
  } catch (e) { ElMessage.error(e.message) } finally { saving.value = false }
}
onMounted(() => load())
</script>
