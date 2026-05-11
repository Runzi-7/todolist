INSERT INTO categories (id, name, color, created_time) VALUES
  (1, '工作', '#2F80ED', CURRENT_TIMESTAMP),
  (2, '学习', '#6C5CE7', CURRENT_TIMESTAMP),
  (3, '生活', '#00A676', CURRENT_TIMESTAMP);

INSERT INTO tasks (
  id,
  title,
  description,
  status,
  priority,
  due_time,
  reminder_time,
  reminded,
  completed_time,
  category_id,
  created_time,
  updated_time
) VALUES
  (
    1,
    '整理 TodoList 项目需求',
    '沉淀第一版任务管理、截止提醒和前后端分离架构。',
    'DOING',
    'HIGH',
    DATEADD('HOUR', 6, CURRENT_TIMESTAMP),
    DATEADD('HOUR', 2, CURRENT_TIMESTAMP),
    FALSE,
    NULL,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    2,
    '完成 Spring Boot 后端框架',
    '搭建健康检查、任务列表接口和 H2 初始化数据。',
    'TODO',
    'URGENT',
    DATEADD('DAY', 1, CURRENT_TIMESTAMP),
    DATEADD('HOUR', 20, CURRENT_TIMESTAMP),
    FALSE,
    NULL,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    3,
    '阅读 Vue 组件拆分示例',
    '把页面拆成导航、概览、任务列表和侧边摘要。',
    'TODO',
    'MEDIUM',
    DATEADD('DAY', 3, CURRENT_TIMESTAMP),
    DATEADD('DAY', 2, CURRENT_TIMESTAMP),
    FALSE,
    NULL,
    2,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    4,
    '晚间复盘今天的任务',
    '检查是否有遗漏事项，调整明天优先级。',
    'DONE',
    'LOW',
    DATEADD('DAY', -1, CURRENT_TIMESTAMP),
    DATEADD('DAY', -1, CURRENT_TIMESTAMP),
    TRUE,
    CURRENT_TIMESTAMP,
    3,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    5,
    '补充 README 框架说明',
    '把启动方式、接口说明和后续 Roadmap 写清楚。',
    'TODO',
    'HIGH',
    DATEADD('DAY', -1, CURRENT_TIMESTAMP),
    DATEADD('DAY', -1, CURRENT_TIMESTAMP),
    FALSE,
    NULL,
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  );
