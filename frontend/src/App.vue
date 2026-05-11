<script setup>
import { computed, onMounted, ref } from 'vue'
import {
  Bell,
  Calendar,
  Check,
  CircleCheck,
  Clock,
  DataBoard,
  Filter,
  Grid,
  List,
  MagicStick,
  MessageBox,
  Search,
  Setting
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { fetchTasks } from './api/tasks'

const loading = ref(true)
const tasks = ref([])
const keyword = ref('')
const activeView = ref('today')

const navItems = [
  { key: 'today', label: '今日任务', icon: Calendar, countKey: 'today' },
  { key: 'inbox', label: '收件箱', icon: MessageBox, countKey: 'todo' },
  { key: 'upcoming', label: '即将截止', icon: Clock, countKey: 'upcoming' },
  { key: 'done', label: '已完成', icon: CircleCheck, countKey: 'done' }
]

const priorityMeta = {
  LOW: { label: '低', tone: 'low' },
  MEDIUM: { label: '中', tone: 'medium' },
  HIGH: { label: '高', tone: 'high' },
  URGENT: { label: '紧急', tone: 'urgent' }
}

const statusMeta = {
  TODO: { label: '待处理', tone: 'todo' },
  DOING: { label: '进行中', tone: 'doing' },
  DONE: { label: '已完成', tone: 'done' },
  ARCHIVED: { label: '已归档', tone: 'archived' }
}

const filteredTasks = computed(() => {
  const value = keyword.value.trim().toLowerCase()

  return tasks.value.filter((task) => {
    const matchesKeyword =
      !value ||
      task.title?.toLowerCase().includes(value) ||
      task.description?.toLowerCase().includes(value) ||
      task.categoryName?.toLowerCase().includes(value)

    if (!matchesKeyword) {
      return false
    }

    if (activeView.value === 'done') {
      return task.status === 'DONE'
    }

    if (activeView.value === 'today') {
      return isToday(task.dueTime) || isOverdue(task)
    }

    if (activeView.value === 'upcoming') {
      return !isDone(task) && !isOverdue(task) && Boolean(task.dueTime)
    }

    return !isDone(task)
  })
})

const counts = computed(() => ({
  today: tasks.value.filter((task) => isToday(task.dueTime) || isOverdue(task)).length,
  todo: tasks.value.filter((task) => !isDone(task)).length,
  upcoming: tasks.value.filter((task) => !isDone(task) && !isOverdue(task) && task.dueTime).length,
  done: tasks.value.filter((task) => task.status === 'DONE').length
}))

const overdueCount = computed(() => tasks.value.filter(isOverdue).length)
const urgentCount = computed(() => tasks.value.filter((task) => task.priority === 'URGENT' && !isDone(task)).length)
const completionRate = computed(() => {
  if (!tasks.value.length) {
    return 0
  }
  return Math.round((counts.value.done / tasks.value.length) * 100)
})

const todayTasks = computed(() => tasks.value.filter((task) => isToday(task.dueTime) && !isDone(task)).slice(0, 4))
const nextDeadline = computed(() =>
  tasks.value
    .filter((task) => task.dueTime && !isDone(task))
    .sort((a, b) => new Date(a.dueTime) - new Date(b.dueTime))[0]
)

onMounted(async () => {
  try {
    tasks.value = await fetchTasks()
  } catch (error) {
    ElMessage.error('任务数据加载失败，请确认后端服务已启动。')
  } finally {
    loading.value = false
  }
})

function isDone(task) {
  return task.status === 'DONE' || task.status === 'ARCHIVED'
}

function isToday(value) {
  if (!value) {
    return false
  }

  const target = new Date(value)
  const now = new Date()
  return (
    target.getFullYear() === now.getFullYear() &&
    target.getMonth() === now.getMonth() &&
    target.getDate() === now.getDate()
  )
}

function isOverdue(task) {
  if (!task.dueTime || isDone(task)) {
    return false
  }

  return new Date(task.dueTime).getTime() < Date.now()
}

function formatDateTime(value) {
  if (!value) {
    return '未设置'
  }

  return new Intl.DateTimeFormat('zh-CN', {
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  }).format(new Date(value))
}

function dueLabel(task) {
  if (!task.dueTime) {
    return '无截止时间'
  }

  if (isOverdue(task)) {
    return '已逾期'
  }

  if (isToday(task.dueTime)) {
    return '今天截止'
  }

  return formatDateTime(task.dueTime)
}
</script>

<template>
  <div class="workspace-shell">
    <aside class="sidebar" aria-label="任务导航">
      <div class="brand">
        <div class="brand-mark">
          <el-icon><DataBoard /></el-icon>
        </div>
        <div>
          <p class="eyebrow">Personal Base</p>
          <h1>TodoList</h1>
        </div>
      </div>

      <nav class="nav-list">
        <button
          v-for="item in navItems"
          :key="item.key"
          class="nav-item"
          :class="{ active: activeView === item.key }"
          type="button"
          @click="activeView = item.key"
        >
          <span>
            <el-icon><component :is="item.icon" /></el-icon>
            {{ item.label }}
          </span>
          <strong>{{ counts[item.countKey] }}</strong>
        </button>
      </nav>

      <div class="sidebar-panel">
        <p class="panel-title">提醒状态</p>
        <div class="reminder-row">
          <el-icon><Bell /></el-icon>
          <span>{{ nextDeadline ? dueLabel(nextDeadline) : '暂无待办提醒' }}</span>
        </div>
      </div>

      <button class="settings-button" type="button">
        <el-icon><Setting /></el-icon>
        设置
      </button>
    </aside>

    <main class="main-panel">
      <header class="topbar">
        <div>
          <p class="eyebrow">Workspace Overview</p>
          <h2>今天先处理最重要的事</h2>
        </div>
        <div class="topbar-actions">
          <el-button :icon="Grid">视图</el-button>
          <el-button type="primary" :icon="MagicStick">新建任务</el-button>
        </div>
      </header>

      <section class="metric-grid" aria-label="任务概览">
        <article class="metric-card accent-blue">
          <span>今日任务</span>
          <strong>{{ counts.today }}</strong>
          <p>包含今天截止和已逾期事项</p>
        </article>
        <article class="metric-card accent-green">
          <span>完成率</span>
          <strong>{{ completionRate }}%</strong>
          <p>来自当前任务样例数据</p>
        </article>
        <article class="metric-card accent-orange">
          <span>紧急事项</span>
          <strong>{{ urgentCount }}</strong>
          <p>优先级为紧急且未完成</p>
        </article>
        <article class="metric-card accent-red">
          <span>已逾期</span>
          <strong>{{ overdueCount }}</strong>
          <p>需要尽快重新安排</p>
        </article>
      </section>

      <section class="content-grid">
        <div class="task-board">
          <div class="board-toolbar">
            <div>
              <p class="eyebrow">Task Table</p>
              <h3>任务清单</h3>
            </div>
            <div class="toolbar-controls">
              <el-input
                v-model="keyword"
                class="search-input"
                :prefix-icon="Search"
                placeholder="搜索任务、分类或描述"
                clearable
              />
              <el-button :icon="Filter">筛选</el-button>
            </div>
          </div>

          <el-skeleton v-if="loading" :rows="7" animated />

          <el-empty
            v-else-if="!filteredTasks.length"
            description="当前视图没有匹配的任务"
          />

          <div v-else class="task-list">
            <article v-for="task in filteredTasks" :key="task.id" class="task-row">
              <div class="task-check" :class="{ done: isDone(task) }">
                <el-icon><Check /></el-icon>
              </div>
              <div class="task-main">
                <div class="task-title-line">
                  <h4>{{ task.title }}</h4>
                  <span class="status-pill" :class="statusMeta[task.status]?.tone">
                    {{ statusMeta[task.status]?.label || task.status }}
                  </span>
                </div>
                <p>{{ task.description }}</p>
                <div class="task-meta">
                  <span class="category-chip" :style="{ '--chip-color': task.categoryColor || '#64748b' }">
                    {{ task.categoryName || '未分类' }}
                  </span>
                  <span class="priority-chip" :class="priorityMeta[task.priority]?.tone">
                    {{ priorityMeta[task.priority]?.label || task.priority }}
                  </span>
                  <span class="due-chip" :class="{ overdue: isOverdue(task), today: isToday(task.dueTime) }">
                    <el-icon><Clock /></el-icon>
                    {{ dueLabel(task) }}
                  </span>
                </div>
              </div>
            </article>
          </div>
        </div>

        <aside class="right-rail" aria-label="今日摘要">
          <section class="summary-card">
            <div class="summary-header">
              <p class="eyebrow">Today</p>
              <h3>今日焦点</h3>
            </div>
            <div v-if="todayTasks.length" class="focus-list">
              <div v-for="task in todayTasks" :key="task.id" class="focus-item">
                <span :style="{ backgroundColor: task.categoryColor || '#64748b' }"></span>
                <div>
                  <strong>{{ task.title }}</strong>
                  <p>{{ dueLabel(task) }}</p>
                </div>
              </div>
            </div>
            <el-empty v-else description="今天没有待办任务" />
          </section>

          <section class="summary-card compact">
            <div class="summary-line">
              <el-icon><List /></el-icon>
              <span>未完成任务</span>
              <strong>{{ counts.todo }}</strong>
            </div>
            <div class="summary-line">
              <el-icon><Calendar /></el-icon>
              <span>即将截止</span>
              <strong>{{ counts.upcoming }}</strong>
            </div>
            <div class="summary-line">
              <el-icon><CircleCheck /></el-icon>
              <span>已完成</span>
              <strong>{{ counts.done }}</strong>
            </div>
          </section>
        </aside>
      </section>
    </main>
  </div>
</template>
