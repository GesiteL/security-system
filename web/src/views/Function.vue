<template>
  <el-card>
    <el-form inline>
      <el-form-item label="功能名"><el-input v-model="q.nameKeyword" placeholder="功能名" clearable style="width:160px" /></el-form-item>
      <el-form-item label="module"><el-input v-model="q.module" placeholder="如 GET:/api/v1/users" clearable style="width:200px" /></el-form-item>
      <el-form-item><el-button type="primary" @click="load(1)">查询</el-button></el-form-item>
      <el-form-item><el-button @click="q.nameKeyword=''; q.module=''; load(1)">重置</el-button></el-form-item>
      <el-form-item><el-button type="success" @click="openCreate">注册功能</el-button></el-form-item>
    </el-form>

    <el-table :data="rows" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="functionName" label="功能名" min-width="150" />
      <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
      <el-table-column prop="module" label="module(权限串)" min-width="210" show-overflow-tooltip />
      <el-table-column prop="planEffectiveAt" label="计划生效" width="160" />
      <el-table-column prop="planExpiredAt" label="计划失效" width="160" />
      <el-table-column label="操作" width="90" fixed="right">
        <template #default="{ row }"><el-button size="small" @click="openEdit(row)">编辑</el-button></template>
      </el-table-column>
    </el-table>
    <div class="pager">
      <el-pagination background layout="total, prev, pager, next" :total="total"
        v-model:current-page="page" v-model:page-size="pageSize" @current-change="load()" />
    </div>

    <el-dialog v-model="dialog" :title="editing ? '编辑功能' : '注册功能'" width="500px" destroy-on-close>
      <el-form :model="form" label-width="100px">
        <el-form-item label="功能名" required><el-input v-model="form.functionName" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="module" required>
          <el-input v-model="form.module" placeholder="METHOD:/api/v1/xxx" />
        </el-form-item>
        <el-form-item label="计划生效"><el-date-picker v-model="form.planEffectiveAt" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" /></el-form-item>
        <el-form-item label="计划失效"><el-date-picker v-model="form.planExpiredAt" type="datetime" value-format="YYYY-MM-DDTHH:mm:ss" /></el-form-item>
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
import { listFunctions, createFunction, updateFunction } from '../api/function'

const rows = ref([]); const total = ref(0); const page = ref(1); const pageSize = ref(20)
const loading = ref(false); const saving = ref(false); const dialog = ref(false); const editing = ref(false)
const q = reactive({ nameKeyword: '', module: '' })
const form = reactive({ id: null, functionName: '', description: '', module: '', planEffectiveAt: null, planExpiredAt: null })

const load = async (p) => {
  if (p) page.value = p
  loading.value = true
  try {
    const res = await listFunctions({ page: page.value, pageSize: pageSize.value, nameKeyword: q.nameKeyword || undefined, module: q.module || undefined })
    rows.value = res.items || []
    total.value = res.total || 0
  } catch (e) { ElMessage.error(e.message) } finally { loading.value = false }
}
const openCreate = () => { editing.value = false; Object.assign(form, { id: null, functionName: '', description: '', module: '', planEffectiveAt: null, planExpiredAt: null }); dialog.value = true }
const openEdit = (row) => {
  editing.value = true
  Object.assign(form, { id: row.id, functionName: row.functionName, description: row.description, module: row.module, planEffectiveAt: row.planEffectiveAt, planExpiredAt: row.planExpiredAt })
  dialog.value = true
}
const save = async () => {
  if (!form.functionName || !form.module) { ElMessage.warning('功能名与 module 必填'); return }
  const payload = { functionName: form.functionName, description: form.description, module: form.module, planEffectiveAt: form.planEffectiveAt, planExpiredAt: form.planExpiredAt }
  saving.value = true
  try {
    if (editing.value) { await updateFunction(form.id, payload); ElMessage.success('修改成功') }
    else { await createFunction(payload); ElMessage.success('注册成功') }
    dialog.value = false
    load()
  } catch (e) { ElMessage.error(e.message) } finally { saving.value = false }
}
onMounted(() => load())
</script>
