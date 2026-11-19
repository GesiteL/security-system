# 信息安全管理系统（Spring Boot 后端）

> 课堂作业改造作品。本系统原设计为 AI Agent 驱动交互（管理员以自然语言下达指令、由 Agent 转译为 API 调用），课堂阶段未交付 Web 前端；本作品仓库含完整后端，最小 Vue3 管理端为改造新增（见下文「管理端」节）。

## 实跑验证（2026-11-19）

- Java 21.0.8 + Maven 3.9.11 + MySQL 9.4，本机实测
- `Started SecuritySystemApplication in 7.566 seconds`，端口 8080
- `POST /api/v1/auth/login`（account=admin / password=admin）→ 200，返回 JWT（156 字符）
- 携带 JWT 访问 `GET /api/v1/users` / `/roles` / `/functions` → 全部 200（审计切面日志佐证）
- 构建产物：security-system-1.0.0-SNAPSHOT.jar（36,844,787 B，同源打包管理端后的当前构建）

## 系统规模（代码级重数）

- 方法级 HTTP 接口 25 个（5 GET + 10 POST + 1 PUT + 4 PATCH + 5 DELETE），前缀 /api/v1
- 业务 Controller 6 个：Auth / Function / Role / RolePermission / User / UserRole
- 数据表 10 张：account_lock、audit_log、audit_log_item、login_attempt、password_reset、role、role_permission、sys_function、user、user_role
- 架构：presentation / application / domain / infrastructure 四层分包（tftc.lch.security）
- 提交史 129 次（Specifier→Architect→Tester 多角色流程 tag 可考）；测试类当前实存 26 个（`src/test` 计数），另有 6 个测试类在 git 删除史中可见

## 运行方式

前置：JDK 21+、MySQL 8+、Maven 3.9。

1. 建库：`mysql -uroot -p < db.sql`（建 security_system 库 + 10 表 + 种子数据，含演示账号 admin/admin）
2. 配置：application.yml 中 DB 密码与 JWT 密钥均已抠空，必须注入环境变量：
   - `DB_PASSWORD=<你的MySQL密码>`
   - `JWT_SECRET=<至少32字节的随机串>`（jjwt 0.12.6 要求 HMAC 密钥 ≥256 bit）
3. 防环境变量劫持端口（如本机存在 SERVER__PORT 之类变量）：`--server.port=8080`
4. `mvn -q -DskipTests package && DB_PASSWORD=... JWT_SECRET=... java -jar target/security-system-1.0.0-SNAPSHOT.jar --server.port=8080`

## 已知限制（如实标注）

- 原 application.yml 默认 JWT secret 长度不足 256 bit，直接以默认值启动会抛 WeakKeyException——本项目必须注入环境变量密钥
- 无 Web 前端（原设计为 Agent 驱动）；管理端为本作品改造新增
- 课堂后期删除 6 个测试类（git 史可见），当前以 -DskipTests 构建
- 未引入 actuator（/actuator/health 404 属正常）
- 历史 server.log 可见一次 SQL 重复键异常记录（启动早期）
- 密码重置接口无邮件/短信投递通道（验证码不落真实通道），属设计预留

## 安全说明

application.yml 的敏感默认值已清洗（密码、密钥均为 ${ENV:} 空默认），凭据只走环境变量，仓库内零硬编码凭据。

## 素材来源

课堂作业，个人独立完成；改造：敏感配置清洗、最小 Vue3 管理端、README。

## 管理端（改造新增）

- 最小 Vue3 管理界面：登录 + 用户管理 / 角色管理 / 功能管理 / 权限分配 / 用户角色 / 审计中心 六页
- 技术：Vue 3 + Vite + Element Plus + axios（JWT Bearer 拦截）+ hash 路由
- 同源部署：前端构建产物打进 Spring Boot static 目录，单 jar 同时服务页面与 API，无跨域配置
- 改造新增接口：GET /api/v1/audit-logs 分页查询，与其余接口同样受 `@RequirePermission` 鉴权（`sys_function` 已随 `db.sql` 注册并授予超级管理员）
- 审计写入侧补全：`AuditAspect` 在每次带权限校验的调用后落库一条真实记录（操作人 / 动作 = 权限串 / 对象类型 / IP / 结果），写失败不影响业务响应；审计中心的表格数据由此实时产生，非手工填充
- 接口总数：原始 25 个 + 新增 1 个 = 26 个方法级接口
- 演示账号：admin / admin；管理端未做入口的接口（密码重置、注册、删除用户）见后端完整 API
