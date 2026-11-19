<template>
  <el-card>
    <el-form inline>
      <el-form-item label="选择角色">
        <el-select v-model="roleId" filterable placeholder="请选择角色" style="width:240px" @change="load(1)">
          <el-option v-for="r in roles" :key="r.id" :label="`${r.id} · ${r.roleName}`" :value="r.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="功能名"><el-input v-model="kw" placeholder="功能名筛选" clearable style="width:160px" @keyup.enter="load(1)" /></el-form-item>
      <el-form-item><el-button type="primary" @click="load(1)">查询</el-button></el-form-item>
      <el-form-item><el-button type="success" :disabled="!roleId" @click="openGrant">授予权限</el-button></el-form-item>
      <el-form-item><el-button type="warning" :disabled="!roleId || rows.length===0" :loading="adjusting" @click="adjust">批量同步(PUT)</el-button></el-form-item>
    </el-form>

    <el-table :data="rows" v-loading="loading" border stripe>
      <el-table-column prop="id" label="授权ID" width="90" />
      <el-table-column prop="roleId" label="角色ID" width="90" />
      <el-table-column prop="permissionId" label="功能ID" width="90" />
      <el-table-column label="功能名" min-width="150">
        <template #default="{ row }">{{ funcName(row.permissionId) }}</template>
      </el-table-column>
      <el-table-column prop="planEffectiveAt" label="计划生效" width="160" />
      <el-table-column prop="planExpiredAt" label="计划失效" width="160" />
      <el-table-column prop="createdAt" label="授予时间" width="160" />
      <el-table-column label="操作" width="90" fixed="right">
        <template #default="{ row }"><el-button size="small" type="danger" @click="revoke(row)">回收</el-button></template>
      </el-table-column>
    </el-table>
    <div class="pager">
      <el-pagination background layout="total, prev, pager, next" :total="total"
        v-model:current-page="page" v-model:page-size="pageSize" @current-change="load()" />
    </div>

    <el-dialog v-model="dialog" title="授予角色权限" width="460px" destroy-on-close>
      <el-form :model="form" label-width="100px">
        <el-form-item label="功能" required>
          <el-select v-model="form.functionId" filterable placeholder="选择功能" style="width:100%">
            <el-option v-for="f in functions" :key="f.id" :label="`${f.id} · ${f.functionName} (${f.module})`" :value="f.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划生效"><el-date-picker v-model="form.planEffectiveAt" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" /></el-form-item>
        <el-form-item label="计划失效"><el-date-picker v-model="form.planExpiredAt" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">授予</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listRolePermissions, grantPermission, adjustRolePermissions, revokePermission } from '../api/permission'
import { listRoles } from '../api/role'
import { listFunctions } from '../api/function'

const roles = ref([]); const functions = ref([])
const roleId = ref(null); const kw = ref('')
const rows = ref([]); const total = ref(0); const page = ref(1); const pageSize = ref(20)
const loading = ref(false); const saving = ref(false); const adjusting = ref(false); const dialog = ref(false)
const form = ref({ functionId: null, planEffectiveAt: null, planExpiredAt: null })

const funcName = (id) => { const f = functions.value.find((x) => x.id === id); return f ? `${id} · ${f.functionName}` : String(id) }

const load = async (p) => {
  if (!roleId.value) { rows.value = []; total.value = 0; return }
  if (p) page.value = p
  loading.value = true
  try {
    const res = await listRolePermissions(roleId.value, { page: page.value, pageSize: pageSize.value, functionNameKeyword: kw.value || undefined })
    rows.value = res.items || res || []
    total.value = res.total || (res.items ? res.items.length : 0)
  } catch (e) { ElMessage.error(e.message) } finally { loading.value = false }
}
const openGrant = () => { form.value = { functionId: null, planEffectiveAt: null, planExpiredAt: null }; dialog.value = true }
const save = async () => {
  if (!form.value.functionId) { ElMessage.warning('请选择功能'); return }
  saving.value = true
  try {
    await grantPermission(roleId.value, { functionId: form.value.functionId, planEffectiveAt: form.value.planEffectiveAt, planExpiredAt: form.value.planExpiredAt })
    ElMessage.success('授予成功'); dialog.value = false; load(1)
  } catch (e) { ElMessage.error(e.message) } finally { saving.value = false }
}
const adjust = async () => {
  adjusting.value = true
  try {
    await adjustRolePermissions(roleId.value, {
      permissions: rows.value.map((r) => ({ functionId: r.permissionId, planEffectiveAt: r.planEffectiveAt, planExpiredAt: r.planExpiredAt }))
    })
    ElMessage.success('批量同步成功'); load(1)
  } catch (e) { ElMessage.error(e.message) } finally { adjusting.value = false }
}
const revoke = async (row) => {
  try {
    await ElMessageBox.confirm(`确定回收授权ID ${row.id}（功能 ${funcName(row.permissionId)}）吗？`, '回收确认', { type: 'warning' })
    await revokePermission(roleId.value, row.id)
    ElMessage.success('已回收'); load()
  } catch (e) { if (e !== 'cancel' && e.message) ElMessage.error(e.message) }
}
onMounted(async () => {
  try {
    const r = await listRoles({ page: 1, pageSize: 100 })
    roles.value = r.items || []
    const f = await listFunctions({ page: 1, pageSize: 100 })
    functions.value = f.items || []
  } catch (e) { ElMessage.error(e.message) }
})
</script>
