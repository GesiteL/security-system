<template>
  <el-card>
    <el-form inline>
      <el-form-item label="角色名"><el-input v-model="q.nameKeyword" placeholder="角色名" clearable style="width:180px" /></el-form-item>
      <el-form-item><el-button type="primary" @click="load(1)">查询</el-button></el-form-item>
      <el-form-item><el-button @click="q.nameKeyword=''; load(1)">重置</el-button></el-form-item>
      <el-form-item><el-button type="success" @click="openCreate">新增角色</el-button></el-form-item>
    </el-form>

    <el-table :data="rows" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="roleName" label="角色名" min-width="140" />
      <el-table-column prop="description" label="描述" min-width="240" show-overflow-tooltip />
      <el-table-column prop="createdAt" label="创建时间" width="160" />
      <el-table-column prop="updatedAt" label="更新时间" width="160" />
      <el-table-column label="操作" width="90" fixed="right">
        <template #default="{ row }"><el-button size="small" @click="openEdit(row)">编辑</el-button></template>
      </el-table-column>
    </el-table>
    <div class="pager">
      <el-pagination background layout="total, prev, pager, next" :total="total"
        v-model:current-page="page" v-model:page-size="pageSize" @current-change="load()" />
    </div>

    <el-dialog v-model="dialog" :title="editing ? '编辑角色' : '新增角色'" width="440px" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-form-item label="角色名" required><el-input v-model="form.roleName" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="2" /></el-form-item>
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
import { listRoles, createRole, updateRole } from '../api/role'

const rows = ref([]); const total = ref(0); const page = ref(1); const pageSize = ref(20)
const loading = ref(false); const saving = ref(false); const dialog = ref(false); const editing = ref(false)
const q = reactive({ nameKeyword: '' })
const form = reactive({ id: null, roleName: '', description: '' })

const load = async (p) => {
  if (p) page.value = p
  loading.value = true
  try {
    const res = await listRoles({ page: page.value, pageSize: pageSize.value, nameKeyword: q.nameKeyword || undefined })
    rows.value = res.items || []
    total.value = res.total || 0
  } catch (e) { ElMessage.error(e.message) } finally { loading.value = false }
}
const openCreate = () => { editing.value = false; Object.assign(form, { id: null, roleName: '', description: '' }); dialog.value = true }
const openEdit = (row) => { editing.value = true; Object.assign(form, { id: row.id, roleName: row.roleName, description: row.description }); dialog.value = true }
const save = async () => {
  if (!form.roleName) { ElMessage.warning('请输入角色名'); return }
  saving.value = true
  try {
    if (editing.value) { await updateRole(form.id, { roleName: form.roleName, description: form.description }); ElMessage.success('修改成功') }
    else { await createRole({ roleName: form.roleName, description: form.description }); ElMessage.success('新增成功') }
    dialog.value = false
    load()
  } catch (e) { ElMessage.error(e.message) } finally { saving.value = false }
}
onMounted(() => load())
</script>
