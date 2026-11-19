<template>
  <el-card>
    <el-form inline>
      <el-form-item label="选择用户">
        <el-select v-model="userId" filterable placeholder="请选择用户" style="width:240px" @change="load(1)">
          <el-option v-for="u in users" :key="u.id" :label="`${u.id} · ${u.username}`" :value="u.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="角色名"><el-input v-model="kw.roleName" placeholder="角色名筛选" clearable style="width:150px" @keyup.enter="load(1)" /></el-form-item>
      <el-form-item><el-button type="primary" @click="load(1)">查询</el-button></el-form-item>
      <el-form-item><el-button type="success" :disabled="!userId" @click="openAssign">分配角色</el-button></el-form-item>
    </el-form>

    <el-table :data="rows" v-loading="loading" border stripe>
      <el-table-column prop="id" label="记录ID" width="90" />
      <el-table-column prop="userId" label="用户ID" width="90" />
      <el-table-column label="角色" min-width="150">
        <template #default="{ row }">{{ roleName(row.roleId) }}</template>
      </el-table-column>
      <el-table-column prop="planEffectiveAt" label="生效时间" width="160" />
      <el-table-column prop="planExpiredAt" label="失效时间" width="160" />
      <el-table-column prop="createdAt" label="创建时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openEdit(row)">改生效期</el-button>
          <el-button size="small" type="danger" @click="remove(row)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pager">
      <el-pagination background layout="total, prev, pager, next" :total="total"
        v-model:current-page="page" v-model:page-size="pageSize" @current-change="load()" />
    </div>

    <el-dialog v-model="dialog" :title="editing ? '修改角色生效期' : '分配角色'" width="460px" destroy-on-close>
      <el-form :model="form" label-width="100px">
        <el-form-item v-if="!editing" label="角色" required>
          <el-select v-model="form.roleId" filterable placeholder="选择角色" style="width:100%">
            <el-option v-for="r in roles" :key="r.id" :label="`${r.id} · ${r.roleName}`" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="生效时间"><el-date-picker v-model="form.planEffectiveAt" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" /></el-form-item>
        <el-form-item label="失效时间"><el-date-picker v-model="form.planExpiredAt" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" /></el-form-item>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { listUserRoles, assignRole, updateUserRole, removeUserRole } from '../api/userRole'
import { listUsers } from '../api/user'
import { listRoles } from '../api/role'

const users = ref([]); const roles = ref([])
const userId = ref(null); const loading = ref(false); const saving = ref(false)
const rows = ref([]); const total = ref(0); const page = ref(1); const pageSize = ref(20)
const dialog = ref(false); const editing = ref(false)
const kw = reactive({ roleName: '' })
const form = reactive({ userRoleId: null, roleId: null, planEffectiveAt: null, planExpiredAt: null })

const roleName = (id) => { const r = roles.value.find((x) => x.id === id); return r ? `${id} · ${r.roleName}` : String(id) }

const load = async (p) => {
  if (!userId.value) { rows.value = []; total.value = 0; return }
  if (p) page.value = p
  loading.value = true
  try {
    const res = await listUserRoles(userId.value, { page: page.value, pageSize: pageSize.value, roleName: kw.roleName || undefined })
    rows.value = res.items || []
    total.value = res.total || 0
  } catch (e) { ElMessage.error(e.message) } finally { loading.value = false }
}
const openAssign = () => { editing.value = false; Object.assign(form, { userRoleId: null, roleId: null, planEffectiveAt: null, planExpiredAt: null }); dialog.value = true }
const openEdit = (row) => { editing.value = true; Object.assign(form, { userRoleId: row.id, roleId: row.roleId, planEffectiveAt: row.planEffectiveAt, planExpiredAt: row.planExpiredAt }); dialog.value = true }
const save = async () => {
  saving.value = true
  try {
    if (editing.value) {
      await updateUserRole(userId.value, form.userRoleId, { planEffectiveAt: form.planEffectiveAt, planExpiredAt: form.planExpiredAt })
      ElMessage.success('修改成功')
    } else {
      if (!form.roleId) { ElMessage.warning('请选择角色'); saving.value = false; return }
      await assignRole(userId.value, { roleId: form.roleId, planEffectiveAt: form.planEffectiveAt, planExpiredAt: form.planExpiredAt })
      ElMessage.success('分配成功')
    }
    dialog.value = false
    load(1)
  } catch (e) { ElMessage.error(e.message) } finally { saving.value = false }
}
const remove = async (row) => {
  try {
    await ElMessageBox.confirm(`确定移除用户 ${userId.value} 的角色 ${roleName(row.roleId)} 吗？`, '移除确认', { type: 'warning' })
    await removeUserRole(userId.value, row.id)
    ElMessage.success('已移除'); load()
  } catch (e) { if (e !== 'cancel' && e.message) ElMessage.error(e.message) }
}
onMounted(async () => {
  try {
    const u = await listUsers({ page: 1, pageSize: 100 })
    users.value = u.items || []
    const r = await listRoles({ page: 1, pageSize: 100 })
    roles.value = r.items || []
  } catch (e) { ElMessage.error(e.message) }
})
</script>
