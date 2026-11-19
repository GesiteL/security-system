# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

**信息安全管理系统** — 福建省大专 JavaEE 课程期末考项目（Spring Boot + Maven）。当前为 S01 迭代：需求基线已完成（`req-v20260613-final`）；架构阶段自审 APPROVED（`arch-s01-v202606172119`，质量门 37 项全部通过），等待 Tester 交叉审查后方可推进 Developer 阶段。仓库现含 `docs/` 需求与架构文档层，以及 Architect 产出的 Java 接口契约骨架（`src/main/java/`，仅接口定义，业务实现尚未填充）。实时进度见 `docs/pm/project-status.html`。

## 文档结构

```
docs/requirement/
  requirement.html            # 需求基线总览（用例图、交付物清单、待议事项）
  user-story.html             # 用户故事基线（soft-manager 维护）
  traceability.html           # 追踪矩阵（需求来源 → 用例 → NFR/BR）
  uc{N}-{english-slug}.html   # 25 份 Cockburn 风格用例描述（soft-specifier 维护）
                              # 示例：uc01-user-register.html ~ uc25-access-control.html

docs/architecture/
  architecture.html           # Architect 阶段汇总索引
  api-index.html              # 所有 API 端点汇总
  db.html / db.sql            # ERD + DDL
  traceability.html           # 需求→用例→接口→API→DB 追踪矩阵
  arch{N}-{english-slug}.html # 25 份接口设计文档（soft-architect 维护）
                              # 示例：arch01-user-register.html ~ arch25-access-control.html
```

每份 HTML 均为单文件自包含（CSS+JS 内联，零外部依赖），PlantUML 图通过 `~h` 十六进制编码发往 plantuml.com 渲染。

## 技能工作流

各阶段由对应 Claude Code 技能驱动，按顺序执行：

| 阶段 | 技能 | 主要产出 |
|------|------|----------|
| 项目管理 | `soft-manager` | user-story.html、project-status.html |
| 需求建模 | `soft-specifier` | uc*.html、requirement.html、traceability.html |
| 接口设计 | `soft-architect` | arch*.html、db.sql、api-index.html |
| 测试驱动 | `soft-tester` | 测试骨架、接口骨架 |
| 功能实现 | `soft-developer` | 业务实现代码 |

技能规范文件位于 `~/.claude/skills/soft-{specifier,architect,tester,developer,manager}/`。

## Git 版本标签规范

各阶段基线以 tag 管理，不生成 change-log 文件：

```
req-vYYYYMMDD   # 需求基线（soft-specifier 产出）
arch-vYYYYMMDD  # 架构基线（soft-architect 产出）
test-vYYYYMMDD  # 测试基线（soft-tester 产出）
impl-vYYYYMMDD  # 实现基线（soft-developer 产出）
```

当前已有：`req-v20260608`

## 用例文件关键约束

规则全文见 `~/.claude/skills/soft-specifier/modeling-rules.md`，以下为最容易踩的约束：

- **RULE-UC-TEXT-21**：`#M5` 主要成功情节每步须为断言（"系统确认/写入/返回…"），不得用过程描述（"系统对…进行校验"）
- **RULE-UC-TEXT-22**：`#E6` 扩展情节标题须陈述触发条件（"3a：格式不合规"），不得描述参与者意图（"3a：用户输入错误"）
- **RULE-UC-TEXT-23**：情节步骤禁止出现技术词汇（BCrypt、JWT、HTTP 状态码、表名、字段名、框架名、错误码常量）；技术细节移至 `#N13`（备注）
- **RULE-UC-TEXT-24**：步骤中的参与者名称必须与 `#2` 声明一致；禁止使用"前端"/"用户端"/"客户端"/"浏览器"

## HTML 主题系统

所有 HTML 文件使用固定深色主题，不提供主题切换功能。修改 HTML 时参照 `~/.claude/skills/soft-specifier/html-spec.md` 中的 CSS 变量模板。

## 章节与步骤代号规范

在讨论用例文档时统一使用以下代号，`#` 为分隔符，避免章节号与步骤号混淆：

| 代号格式 | 含义 | 示例 |
|----------|------|------|
| `UC{N}#M5#S` | UC-N 的 #M5 主要成功情节第 S 步 | `UC25#M5#4` = UC-25 主要成功情节第4步 |
| `UC{N}#E6#Xa` | UC-N 的 #E6 扩展情节步骤 Xa | `UC25#E6#3b` = UC-25 扩展情节 3b |
| `UC{N}#X8` | UC-N 的 #X8 异常处理章节 | `UC25#X8` |
| `UC{N}#Q12` | UC-N 的 #Q12 未决事项章节 | `UC25#Q12` |
| `UC{N}#N13` | UC-N 的 #N13 备注章节 | `UC25#N13` |
| `UC{N}#2`、`#4` 等 | 其余章节直接用编号 | `UC25#2` = 参与者章节 |
| 无 UC 前缀时 | 泛指任意用例的该章节 | `#M5` 步骤、`#E6` 扩展 |

字母含义备忘：**M**ain（主）/ **E**xtension（扩展）/ e**X**ception（异常）/ **Q**uestion（未决）/ **N**ote（备注）

## SSOT 层级

当用例文本、关系语义、活动图、用例图之间出现冲突时，优先级从高到低：
> 用例文本（uc*.html `#M5`/`#E6`）→ 关系语义 → 活动图 → 用例图 → 追踪矩阵
