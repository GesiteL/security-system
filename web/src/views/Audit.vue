<template>
  <el-card>
    <el-alert type="info" :closable="false" style="margin-bottom:12px"
      title="审计中心（改造新增）：权限切面实时落库，每次带权限校验的接口调用都会记一条（操作人 / 动作 / 对象 / IP / 结果）。" />
    <el-table :data="rows" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="operatorId" label="操作人ID" width="90" />
      <el-table-column prop="operatorName" label="操作人" min-width="110" />
      <el-table-column prop="action" label="动作类型" min-width="140" />
      <el-table-column prop="targetType" label="对象类型" width="110" />
      <el-table-column prop="targetId" label="对象ID" width="90" />
      <el-table-column prop="targetName" label="对象名称" min-width="140" show-overflow-tooltip />
      <el-table-column label="结果" width="80">
        <template #default="{ row }"><el-tag :type="row.result ? 'success' : 'danger'">{{ row.result ? '成功' : '失败' }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="ipAddress" label="来源IP" width="130" />
      <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
      <el-table-column prop="operatedAt" label="操作时间" width="160" />
    </el-table>
    <div class="pager">
      <el-pagination background layout="total, prev, pager, next, sizes" :total="total"
        v-model:current-page="page" v-model:page-size="pageSize" :page-sizes="[10, 20, 50]"
        @current-change="load()" @size-change="load(1)" />
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listAuditLogs } from '../api/audit'

const rows = ref([]); const total = ref(0); const page = ref(1); const pageSize = ref(20)
const loading = ref(false)

const load = async (p) => {
  if (p) page.value = p
  loading.value = true
  try {
    const res = await listAuditLogs({ page: page.value, pageSize: pageSize.value })
    rows.value = res.items || []
    total.value = res.total || 0
  } catch (e) { ElMessage.error(e.message) } finally { loading.value = false }
}
onMounted(() => load())
</script>
