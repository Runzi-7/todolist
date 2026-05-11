# TodoList 个人任务管理项目技术文档

## 1. 项目定位

这是一个给个人使用的 TodoList 工具，用来管理每天要做的事情、查看哪些任务还没有完成，并在任务接近截止时间时提醒你。

项目不建议一开始做得很复杂。第一版重点是把“创建任务 -> 查看任务 -> 修改任务 -> 完成任务 -> 截止提醒”这个闭环做通。等你真正用起来之后，再考虑重复任务、日历、多端同步、桌面通知等增强功能。

因为你希望使用 Java 相关技术栈，并且采用前后端分离，所以本项目推荐：

- 后端：Java + Spring Boot。
- 前端：Vue 3 + Vite。
- 通信方式：REST API。
- 数据库：开发期先用 H2 或 MySQL，后续稳定后使用 MySQL。

## 2. 推荐技术栈

### 2.1 总体架构

```text
浏览器前端 Vue 3
        |
        | HTTP / JSON
        v
Java 后端 Spring Boot
        |
        | JPA / SQL
        v
数据库 H2 / MySQL
```

前后端分离的意思是：

- 前端负责页面、按钮、表单、交互。
- 后端负责业务逻辑、数据保存、提醒判断、接口。
- 前端通过 HTTP 请求调用后端接口。
- 后端返回 JSON 数据给前端。

### 2.2 后端技术栈

| 模块 | 技术 | 说明 |
| --- | --- | --- |
| 编程语言 | Java 21 或 Java 17 | 推荐 Java 21，如果环境配置困难可用 Java 17 |
| 后端框架 | Spring Boot 3 | Java Web 开发主流框架 |
| Web 接口 | Spring Web | 提供 REST API |
| 数据访问 | Spring Data JPA | 简化数据库增删改查 |
| 数据库 | H2 / MySQL | H2 适合入门开发，MySQL 适合长期使用 |
| 参数校验 | Spring Validation | 校验标题不能为空、时间格式等 |
| 定时任务 | Spring Scheduling | 定时检查哪些任务需要提醒 |
| 构建工具 | Maven | Java 项目依赖管理工具 |
| 接口文档 | springdoc-openapi，可选 | 自动生成 Swagger 接口页面 |

### 2.3 前端技术栈

| 模块 | 技术 | 说明 |
| --- | --- | --- |
| 前端框架 | Vue 3 | 入门相对友好，适合做管理类页面 |
| 构建工具 | Vite | 启动快，配置简单 |
| 页面路由 | Vue Router | 管理今日任务、全部任务、设置等页面 |
| 状态管理 | Pinia，可选 | 任务状态复杂后再使用 |
| 网络请求 | Axios | 调用后端 REST API |
| UI 组件库 | Element Plus | 快速做表格、表单、弹窗、日期选择 |
| 样式 | CSS / Element Plus 样式 | 第一版不需要复杂设计系统 |

### 2.4 为什么这里不使用 Next.js

Next.js 是 React 生态里的全栈框架，它可以同时做前端和后端，但本项目已经明确采用 Java 作为后端技术栈。继续使用 Next.js 会让技术边界变得不清晰：

- 需要额外引入 React 和 Next.js 服务端机制。
- 后端主线会偏离 Java + Spring Boot。
- 前后端分离架构不如 Vue + Spring Boot 组合直观。

所以更适合你的路线是：

- 后端采用 Java + Spring Boot。
- 前端采用 Vue。
- 两边通过 REST API 连接。

## 3. 项目结构建议

建议把前端和后端放在同一个大目录下，但各自独立运行：

```text
todolist/
  backend/
    pom.xml
    src/
      main/
        java/
          com/example/todolist/
            TodolistApplication.java
            controller/
            service/
            repository/
            entity/
            dto/
            config/
        resources/
          application.yml
  frontend/
    package.json
    index.html
    src/
      main.js
      App.vue
      api/
      views/
      components/
      router/
```

后端分层建议：

| 层 | 目录 | 负责内容 |
| --- | --- | --- |
| Controller | controller | 接收前端请求，返回 JSON |
| Service | service | 处理业务逻辑 |
| Repository | repository | 操作数据库 |
| Entity | entity | 数据库表对应的 Java 类 |
| DTO | dto | 前后端传输的数据对象 |
| Config | config | 跨域、定时任务等配置 |

## 4. 核心功能范围

### 4.1 第一版必须完成

- 创建任务：标题、描述、截止时间、优先级、分类。
- 查看任务列表：全部、今天、即将截止、已逾期、已完成。
- 编辑任务：修改标题、描述、截止时间、优先级、分类。
- 完成任务：勾选完成，记录完成时间。
- 删除任务：删除不再需要的任务。
- 筛选任务：按状态、优先级、分类、截止时间筛选。
- 基础提醒：后端定时检查到期任务，前端展示提醒信息。

### 4.2 第二阶段功能

- 浏览器通知。
- 重复任务：每天、每周、每月。
- 子任务。
- 标签。
- 搜索。
- 日历视图。
- 数据导出 JSON / CSV。
- 简单统计：完成数量、逾期数量。

### 4.3 后期增强功能

- 登录注册。
- 多设备同步。
- 邮件提醒。
- 桌面通知。
- 手机提醒。
- 数据备份。
- 番茄钟。
- 自然语言输入，例如“明天下午 3 点交报告”。

## 5. 数据库设计

### 5.1 task 表

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | BIGINT | 主键，自增 |
| title | VARCHAR | 任务标题 |
| description | TEXT | 任务描述 |
| status | VARCHAR | TODO / DOING / DONE / ARCHIVED |
| priority | VARCHAR | LOW / MEDIUM / HIGH / URGENT |
| due_time | DATETIME | 截止时间 |
| reminder_time | DATETIME | 提醒时间 |
| reminded | BOOLEAN | 是否已经提醒过 |
| completed_time | DATETIME | 完成时间 |
| category_id | BIGINT | 分类 ID |
| created_time | DATETIME | 创建时间 |
| updated_time | DATETIME | 更新时间 |

### 5.2 category 表

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | BIGINT | 主键，自增 |
| name | VARCHAR | 分类名称，例如工作、学习、生活 |
| color | VARCHAR | 分类颜色 |
| created_time | DATETIME | 创建时间 |

### 5.3 reminder_log 表

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | BIGINT | 主键，自增 |
| task_id | BIGINT | 对应任务 ID |
| remind_time | DATETIME | 实际提醒时间 |
| channel | VARCHAR | IN_APP / BROWSER / EMAIL |
| status | VARCHAR | SENT / FAILED |
| created_time | DATETIME | 创建时间 |

## 6. 后端 API 设计

### 6.1 任务接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | /api/tasks | 获取任务列表 |
| POST | /api/tasks | 创建任务 |
| GET | /api/tasks/{id} | 获取任务详情 |
| PUT | /api/tasks/{id} | 更新任务 |
| DELETE | /api/tasks/{id} | 删除任务 |
| PATCH | /api/tasks/{id}/complete | 标记完成 |
| PATCH | /api/tasks/{id}/reopen | 重新打开任务 |

列表接口可以支持这些查询参数：

```text
GET /api/tasks?status=TODO&priority=HIGH&categoryId=1&keyword=报告
```

### 6.2 分类接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | /api/categories | 获取分类列表 |
| POST | /api/categories | 创建分类 |
| PUT | /api/categories/{id} | 更新分类 |
| DELETE | /api/categories/{id} | 删除分类 |

### 6.3 提醒接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | /api/reminders/due | 获取当前应该提醒的任务 |
| PATCH | /api/tasks/{id}/reminded | 标记任务已提醒 |

第一版可以先让前端每隔 30 秒请求一次 `/api/reminders/due`，如果有需要提醒的任务，就在页面顶部显示提醒。

后续再做浏览器通知或邮件通知。

## 7. 前端页面设计

### 7.1 今日任务页

打开应用后的默认页面。显示：

- 今天截止的任务。
- 已经逾期但未完成的任务。
- 高优先级任务。
- 即将提醒的任务。

### 7.2 全部任务页

显示所有任务，并提供筛选：

- 状态：未完成、进行中、已完成。
- 优先级：低、中、高、紧急。
- 分类：工作、学习、生活。
- 时间：今天、本周、已逾期、无截止时间。

### 7.3 新建 / 编辑任务弹窗

字段：

- 任务标题。
- 任务描述。
- 截止时间。
- 提醒时间。
- 优先级。
- 分类。

建议先用 Element Plus 的表单组件和日期选择器，提高开发效率并减少重复 UI 工作。

### 7.4 设置页

第一版可以很简单：

- 默认提醒提前时间。
- 数据导出。
- 关于项目版本。

## 8. 提醒功能方案

提醒是这个项目里最容易变复杂的功能，建议分阶段实现。

### 8.1 第一阶段：页面内提醒

实现方式：

- 后端保存 `reminder_time`。
- 前端每隔 30 秒请求 `/api/reminders/due`。
- 如果发现需要提醒的任务，就在页面显示弹窗或顶部通知。
- 用户点击“知道了”后，调用接口把任务标记为已提醒。

优点：

- 实现简单。
- 适合入门。

限制：

- 只有打开网页时才能提醒。

### 8.2 第二阶段：浏览器通知

实现方式：

- 前端申请浏览器通知权限。
- 页面打开时，如果任务到期，通过浏览器 Notification API 弹通知。

限制：

- 用户必须授权。
- 页面通常需要保持打开。
- 不同浏览器策略不完全一样。

### 8.3 第三阶段：更可靠提醒

如果你后续真的依赖它来安排生活，可以考虑：

- 后端定时任务 + 邮件提醒。
- 后端定时任务 + 企业微信 / Telegram / 钉钉机器人。
- 桌面应用通知。
- 手机 App 或小程序提醒。

推荐的提醒升级路线是：

1. 先做页面内提醒。
2. 再做浏览器通知。
3. 最后做邮件提醒。

## 9. 前期准备

### 9.1 开发环境准备

- JDK 21 或 JDK 17。
- IntelliJ IDEA Community。
- Maven。
- Node.js。
- VS Code，可选，用于写前端。
- MySQL，可选，第一版可以先用 H2。
- Apifox / Postman，可选，用于测试接口。

### 9.2 项目初始化准备

- 使用 Spring Initializr 创建后端项目。
- 使用 Vite 创建 Vue 前端项目。
- 建立 Git 仓库。
- 先用 H2 数据库跑通流程。
- 等功能稳定后再切换 MySQL。

### 9.3 配置准备

- 后端配置跨域，允许前端本地开发地址访问接口。
- 约定接口统一前缀，例如 `/api`。
- 约定统一返回格式，例如 `code`、`message`、`data`。
- 约定时间格式，例如统一使用 `yyyy-MM-dd HH:mm:ss` 或 ISO 8601。
- 准备开发、测试、生产三套配置文件，例如 `application-dev.yml`、`application-test.yml`、`application-prod.yml`。

## 10. 中期开发计划

### 10.1 第一阶段：后端基础

目标：后端能独立完成任务数据的增删改查。

工作内容：

- 创建 Spring Boot 项目。
- 配置 Maven 依赖。
- 创建 Task 实体类。
- 创建 TaskRepository。
- 创建 TaskService。
- 创建 TaskController。
- 实现任务 CRUD 接口。
- 用 Apifox 或 Postman 测试接口。

验收标准：

- `POST /api/tasks` 可以创建任务。
- `GET /api/tasks` 可以查看任务。
- `PUT /api/tasks/{id}` 可以修改任务。
- `DELETE /api/tasks/{id}` 可以删除任务。
- 数据保存在数据库中。

### 10.2 第二阶段：前端基础

目标：前端页面可以调用后端接口。

工作内容：

- 创建 Vue 3 + Vite 项目。
- 安装 Element Plus 和 Axios。
- 创建任务列表页面。
- 创建新增任务弹窗。
- 调用后端接口获取任务。
- 调用后端接口创建、编辑、完成、删除任务。
- 处理加载状态和错误提示。

验收标准：

- 页面可以显示后端返回的任务。
- 在页面新建任务后，刷新仍然存在。
- 点击完成后，任务状态正确变化。

### 10.3 第三阶段：任务管理体验

目标：让项目真正可用。

工作内容：

- 增加分类。
- 增加优先级。
- 增加截止时间。
- 增加今日任务视图。
- 增加逾期任务显示。
- 增加筛选和搜索。
- 增加基础提醒。

验收标准：

- 可以快速知道今天该做什么。
- 可以看到哪些任务已经逾期。
- 可以按分类和优先级找到任务。
- 到提醒时间后，页面能提示。

### 10.4 第四阶段：优化和增强

目标：提高长期使用价值。

工作内容：

- 增加浏览器通知。
- 增加数据导出。
- 增加重复任务。
- 增加简单统计。
- 增加接口文档。
- 增加基础测试。

验收标准：

- 任务数据可以备份。
- 常用接口有文档。
- 核心业务逻辑有测试。
- 连续使用一周不会明显混乱。

## 11. 后期维护

### 11.1 数据维护

- 定期导出任务数据。
- 如果使用 MySQL，定期备份数据库。
- 修改表结构时，记录每次变更。

### 11.2 功能维护

- 每次只增加一个主要功能。
- 优先修复影响日常使用的问题。
- 不要一开始做太多“看起来很高级但自己不常用”的功能。

### 11.3 代码维护

- Controller 不写复杂业务逻辑，业务逻辑放 Service。
- Entity 不直接暴露给前端，复杂项目中使用 DTO。
- 后端统一返回格式。
- 前端接口请求统一放到 `src/api/` 目录。
- 提交代码前至少手动测试核心流程。

## 12. 推荐开发顺序

1. 搭建 Spring Boot 后端。
2. 跑通一个 `GET /api/health` 测试接口。
3. 创建 task 表和 Task 实体。
4. 实现任务新增接口。
5. 实现任务列表接口。
6. 搭建 Vue 前端。
7. 前端请求任务列表接口。
8. 前端实现新增任务。
9. 实现编辑、完成、删除。
10. 增加截止时间和逾期状态。
11. 增加分类和优先级。
12. 增加页面内提醒。
13. 增加导出和备份。

## 13. 里程碑

| 阶段 | 时间建议 | 目标 |
| --- | --- | --- |
| 后端 MVP | 3-5 天 | 完成任务 CRUD API |
| 前端 MVP | 3-5 天 | 完成任务页面和接口调用 |
| 功能完善 | 5-10 天 | 分类、优先级、截止时间、提醒 |
| 稳定使用 | 1-2 周 | 根据真实使用修复问题 |
| 后期增强 | 按需 | 通知、导出、统计、同步 |

## 14. 第一版建议不要做的功能

第一版暂时不做：

- 登录注册。
- 多人协作。
- 权限管理。
- 复杂项目管理。
- 手机 App。
- AI 自动规划。
- 复杂统计报表。

这些功能不是不能做，而是会显著增加项目复杂度。第一版先把个人 TodoList 做顺手，比功能多更重要。

## 15. 结论

这个项目很适合作为 Java 后端 + 前端分离项目，因为它覆盖了真实开发中最常见的一套流程：

- 数据库表设计。
- Java 实体类。
- Spring Boot 接口。
- 前端页面。
- 前后端通过 JSON 通信。
- 定时任务和提醒逻辑。

推荐最终技术路线：

```text
后端：Java 21 + Spring Boot 3 + Spring Web + Spring Data JPA + H2/MySQL
前端：Vue 3 + Vite + Element Plus + Axios
架构：前后端分离，REST API 通信
第一版提醒：页面内提醒
后续提醒：浏览器通知或邮件提醒
```

你可以先把目标定为：做出一个自己真的能连续使用一周的 TodoList。只要这个目标达成，后面再加功能就会很自然。
