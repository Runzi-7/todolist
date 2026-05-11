# TodoList

一个基于 **Java Spring Boot + Vue 3** 的个人任务管理应用。当前版本是可运行的前后端分离骨架：后端提供健康检查和任务列表接口，前端提供 Airtable 风格的任务工作台，用于后续继续扩展 CRUD、分类、提醒和数据导出。

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-6DB33F?logo=springboot&logoColor=white)
![Vue](https://img.shields.io/badge/Vue-3-42B883?logo=vuedotjs&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-6-646CFF?logo=vite&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-blue)

## Features

- 前后端分离架构，后端 REST API，前端通过 Axios 调用接口。
- Spring Boot 后端分层：Controller、Service、Repository、Entity、DTO、Config。
- H2 内存数据库内置示例任务数据，启动后即可查看。
- Vue 3 工作台页面，包含左侧导航、任务概览、任务列表、今日摘要。
- Airtable 风格视觉：白底、细边框、彩色标签、紧凑任务表格。
- Vite 本地代理，前端请求 `/api` 自动转发到后端 `18080`。
- Swagger UI 和 H2 Console 已预留，方便后续开发调试。

## Tech Stack

| Layer | Technology |
| --- | --- |
| Backend | Java 21, Spring Boot 3, Spring Web, Spring Data JPA, Validation |
| Database | H2 for local scaffold, MySQL-ready structure |
| API Docs | springdoc-openapi |
| Frontend | Vue 3, Vite, Element Plus, Axios |
| Build | Maven Wrapper, npm |

## Project Structure

```text
todolist/
  backend/
    pom.xml
    mvnw
    src/main/java/com/runzi/todolist/
      config/
      controller/
      dto/
      entity/
      repository/
      service/
    src/main/resources/
      application.yml
      data.sql
  frontend/
    package.json
    vite.config.js
    src/
      api/
      assets/
      App.vue
      main.js
  TECHNICAL_PLAN.md
  README.md
```

## Quick Start

### 1. Start Backend

```bash
cd backend
./mvnw spring-boot:run
```

Backend runs at:

- Health check: [http://localhost:18080/api/health](http://localhost:18080/api/health)
- Tasks API: [http://localhost:18080/api/tasks](http://localhost:18080/api/tasks)
- Swagger UI: [http://localhost:18080/swagger-ui.html](http://localhost:18080/swagger-ui.html)
- H2 Console: [http://localhost:18080/h2-console](http://localhost:18080/h2-console)

H2 connection:

```text
JDBC URL: jdbc:h2:mem:todolist
User: sa
Password:
```

### 2. Start Frontend

Open a second terminal:

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at:

- [http://localhost:5173](http://localhost:5173)

## API Overview

| Method | Path | Description |
| --- | --- | --- |
| GET | `/api/health` | Backend health status |
| GET | `/api/tasks` | List scaffold sample tasks |

Current task response fields:

```json
{
  "id": 1,
  "title": "整理 TodoList 项目需求",
  "description": "沉淀第一版任务管理、截止提醒和前后端分离架构。",
  "status": "DOING",
  "priority": "HIGH",
  "categoryName": "工作",
  "categoryColor": "#2F80ED",
  "dueTime": "2026-05-11T18:00:00",
  "reminderTime": "2026-05-11T16:00:00",
  "reminded": false,
  "completedTime": null,
  "createdTime": "2026-05-11T10:00:00",
  "updatedTime": "2026-05-11T10:00:00"
}
```

## Scripts

Backend:

```bash
cd backend
./mvnw test
./mvnw spring-boot:run
```

Frontend:

```bash
cd frontend
npm install
npm run build
npm run dev
```

## MySQL Profile Roadmap

当前默认使用 H2，便于快速启动。切换到 MySQL 时建议新增 `application-mysql.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/todolist?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
```

启动时使用：

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

## Roadmap

- [ ] 任务新增、编辑、完成、删除。
- [ ] 分类管理和优先级筛选。
- [ ] 截止时间、逾期状态和提醒轮询。
- [ ] 浏览器通知。
- [ ] 数据导出 JSON / CSV。
- [ ] MySQL profile 和数据库迁移管理。
- [ ] 登录注册和多设备同步。

## Screenshot

当前仓库不提交截图文件。启动前后端后，访问 `http://localhost:5173` 即可查看工作台界面。

## Contributing

这是个人 TodoList 项目的初始骨架。建议后续每次只推进一个核心能力，并在提交前完成：

- 后端 `./mvnw test`
- 前端 `npm run build`
- 浏览器手动验证核心页面

## License

MIT
