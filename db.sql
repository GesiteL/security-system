-- ============================================================
-- Security System · DDL
-- 数据库: security_system | 需求基线: req-v20260613-final | 架构版本: arch-S01
-- 严格白名单类型 | 严禁 ENUM | 无外键约束 | 每个索引加 COMMENT
-- 内建最高权限种子账号：admin / 默认口令：admin（仅保存 BCrypt 哈希值）
-- ============================================================

DROP DATABASE IF EXISTS `security_system`;
CREATE DATABASE `security_system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `security_system`;

-- ------------------------------------------------------------
-- 1. user — 系统用户表
-- 来源 UC: UC-01 用户注册, UC-02 用户登入, UC-04 忘记密码,
--          UC-05~08 用户管理, UC-25 系统权限控制
-- 业务规则: BR-01 账号唯一, BR-02 密码复杂度, BR-03 手机号唯一,
--           BR-04 邮箱唯一, BR-24 有效期, BR-25 软删除
-- ------------------------------------------------------------
CREATE TABLE `user` (
  `id`                    INT UNSIGNED    NOT NULL AUTO_INCREMENT            COMMENT '主键',
  `username`              VARCHAR(64)     NOT NULL                           COMMENT '登入账号（BR-01：在未删除记录内唯一，不可修改）',
  `display_name`          VARCHAR(128)    NOT NULL                           COMMENT '用户姓名/昵称（可修改）',
  `password_hash`         VARCHAR(255)    NOT NULL                           COMMENT '加密后的密码（BCrypt 等，不存明文）',
  `phone`                 VARCHAR(20)     NOT NULL                           COMMENT '手机号（BR-03：在未删除记录内唯一）',
  `email`                 VARCHAR(254)    NOT NULL                           COMMENT '电子邮箱（BR-04：在未删除记录内唯一）',
  `status`                TINYINT(1)      NOT NULL DEFAULT 1                 COMMENT '账号状态：1=已激活，0=已禁用',
  `is_deleted`            TINYINT(1)      NOT NULL DEFAULT 0                 COMMENT '软删除标记：0=正常，1=已删除（BR-25）',
  `deleted_token`         BIGINT UNSIGNED NOT NULL DEFAULT 0                 COMMENT '软删除版本号：活动记录固定为0；软删除时写入当前记录主键值，支持同账号多次重建与多次删除',
  `force_change_password` TINYINT(1)      NOT NULL DEFAULT 0                 COMMENT '首次登入强制改密：0=否，1=是（UC-06 管理员创建用户时置1）',
  `credentials_version`   INT UNSIGNED    NOT NULL DEFAULT 1                 COMMENT '凭证版本号：密码重置/账号禁用时自增，UC-25 以此实现单会话策略',
  `plan_effective_at`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账号计划生效时间（BR-24）',
  `plan_expired_at`       DATETIME            NULL DEFAULT NULL              COMMENT '账号计划失效时间，NULL表示不限期（BR-24）',
  `last_login_at`         DATETIME            NULL DEFAULT NULL              COMMENT '最近一次登入时间',
  `last_login_ip`         VARCHAR(45)         NULL DEFAULT NULL              COMMENT '最近一次登入来源IP（支持IPv6）',
  `created_at`            DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`            DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_username_deleted_token` (`username`, `deleted_token`)     COMMENT '账号在活动记录内唯一；活动记录 deleted_token=0，软删除后写入删除版本，支持多次重建/删除（BR-01,BR-25）',
  UNIQUE INDEX `uq_phone_deleted_token` (`phone`, `deleted_token`)           COMMENT '手机号在活动记录内唯一；活动记录 deleted_token=0，软删除后写入删除版本（BR-03,BR-25）',
  UNIQUE INDEX `uq_email_deleted_token` (`email`, `deleted_token`)           COMMENT '邮箱在活动记录内唯一；活动记录 deleted_token=0，软删除后写入删除版本（BR-04,BR-25）',
  INDEX `idx_status_deleted` (`status`, `is_deleted`)                        COMMENT '按状态与删除标记联合过滤（UC-05 查询用户列表）',
  INDEX `idx_plan_effective_at` (`plan_effective_at`)                        COMMENT '按生效时间过滤（UC-25 有效期校验 BR-24）',
  INDEX `idx_plan_expired_at` (`plan_expired_at`)                            COMMENT '按失效时间过滤（UC-25 有效期校验 BR-24）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='系统用户表；账号/手机号/邮箱在活动记录内唯一；deleted_token 支持软删除后多次重建与多次删除；含凭证版本号实现单会话策略';

-- ------------------------------------------------------------
-- 2. role — 角色表
-- 来源 UC: UC-09~12 角色管理, UC-13~16 用户角色管理
-- 业务规则: BR-11 角色名称唯一, BR-24 有效期, BR-25 软删除
-- ------------------------------------------------------------
CREATE TABLE `role` (
  `id`                INT UNSIGNED    NOT NULL AUTO_INCREMENT            COMMENT '主键',
  `role_name`         VARCHAR(128)    NOT NULL                           COMMENT '角色名称（BR-11：在未删除记录内唯一）',
  `description`       VARCHAR(500)        NULL DEFAULT NULL              COMMENT '角色描述（选填）',
  `is_deleted`        TINYINT(1)      NOT NULL DEFAULT 0                 COMMENT '软删除标记：0=正常，1=已删除（BR-25）',
  `deleted_token`     BIGINT UNSIGNED NOT NULL DEFAULT 0                 COMMENT '软删除版本号：活动记录固定为0；软删除时写入当前记录主键值',
  `plan_effective_at` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '角色计划生效时间（BR-24）',
  `plan_expired_at`   DATETIME            NULL DEFAULT NULL              COMMENT '角色计划失效时间，NULL表示不限期（BR-24）',
  `created_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_role_name_deleted_token` (`role_name`, `deleted_token`) COMMENT '角色名称在活动记录内唯一；deleted_token 支持多次重建/删除（BR-11,BR-25）',
  INDEX `idx_is_deleted` (`is_deleted`)                                  COMMENT '过滤软删除记录（UC-09 查询角色列表）',
  INDEX `idx_plan_expired_at` (`plan_expired_at`)                        COMMENT '按失效时间过滤（BR-24 有效期校验）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='角色表；名称在活动记录内唯一；deleted_token 支持软删除后多次重建与多次删除；含有效期支持时效性角色（BR-24）';

-- ------------------------------------------------------------
-- 3. sys_function — 系统功能/权限点表
-- 来源 UC: UC-17~20 系统功能管理, UC-21~24 角色权限管理,
--          UC-25 系统权限控制
-- 业务规则: BR-14 功能名称唯一, BR-24 有效期, BR-25 软删除
-- ------------------------------------------------------------
CREATE TABLE `sys_function` (
  `id`                INT UNSIGNED    NOT NULL AUTO_INCREMENT            COMMENT '主键',
  `function_name`     VARCHAR(128)    NOT NULL                           COMMENT '功能名称（BR-14：在未删除记录内唯一）',
  `description`       VARCHAR(500)        NULL DEFAULT NULL              COMMENT '功能描述（选填）',
  `module`            VARCHAR(128)    NOT NULL                           COMMENT '权限码，格式 HTTP_METHOD:URI（如 GET:/api/v1/users），与 ApiAuthorizationFilter 精确匹配，零转换',
  `is_deleted`        TINYINT(1)      NOT NULL DEFAULT 0                 COMMENT '软删除标记：0=正常，1=已删除（BR-25）',
  `deleted_token`     BIGINT UNSIGNED NOT NULL DEFAULT 0                 COMMENT '软删除版本号：活动记录固定为0；软删除时写入当前记录主键值',
  `plan_effective_at` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '功能计划生效时间（BR-24）',
  `plan_expired_at`   DATETIME            NULL DEFAULT NULL              COMMENT '功能计划失效时间，NULL表示不限期（BR-24）',
  `created_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uq_function_name_deleted_token` (`function_name`, `deleted_token`) COMMENT '功能名称在活动记录内唯一；deleted_token 支持多次重建/删除（BR-14,BR-25）',
  INDEX `idx_module` (`module`)                                          COMMENT '按权限码精确过滤（UC-17 按模块筛选，UC-25 权限验证）',
  INDEX `idx_is_deleted` (`is_deleted`)                                  COMMENT '过滤软删除记录',
  INDEX `idx_plan_expired_at` (`plan_expired_at`)                        COMMENT '按失效时间过滤（BR-24 有效期校验）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='系统功能/权限点表；名称在活动记录内唯一；module 存储权限码 METHOD:URI；deleted_token 支持软删除后多次重建与多次删除；含有效期（BR-14,BR-24,BR-25）';

-- ------------------------------------------------------------
-- 4. user_role — 用户角色关联表（N:M）
-- 来源 UC: UC-13 查询用户角色, UC-14 分配用户角色,
--          UC-15 修改用户角色, UC-16 移除用户角色
-- 业务规则: BR-13 不重复分配, BR-24 有效期, BR-25 软删除
-- ------------------------------------------------------------
CREATE TABLE `user_role` (
  `id`                INT UNSIGNED    NOT NULL AUTO_INCREMENT            COMMENT '主键',
  `user_id`           INT UNSIGNED    NOT NULL                           COMMENT '用户ID（关联 user.id）',
  `role_id`           INT UNSIGNED    NOT NULL                           COMMENT '角色ID（关联 role.id）',
  `is_deleted`        TINYINT(1)      NOT NULL DEFAULT 0                 COMMENT '软删除/失效标记：0=有效，1=已失效（BR-25）',
  `deleted_token`     BIGINT UNSIGNED NOT NULL DEFAULT 0                 COMMENT '软删除版本号：活动记录固定为0；软删除时写入当前记录主键值，支持同一用户角色关系多次分配与多次移除',
  `plan_effective_at` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分配计划生效时间（BR-24）',
  `plan_expired_at`   DATETIME            NULL DEFAULT NULL              COMMENT '分配计划失效时间，NULL表示不限期（BR-24）',
  `assigned_by`       INT UNSIGNED        NULL DEFAULT NULL              COMMENT '执行分配操作的管理员ID（关联 user.id）',
  `assigned_at`       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分配记录创建时间（UC-13 查询结果展示用）',
  `created_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id`)                                        COMMENT '按用户查询其所有角色（UC-13,UC-15,UC-16,UC-25）',
  INDEX `idx_role_id` (`role_id`)                                        COMMENT '按角色查询其所有用户（UC-09 关联用户数统计）',
  UNIQUE INDEX `uq_user_role_deleted_token` (`user_id`, `role_id`, `deleted_token`) COMMENT '用户-角色关系在活动记录内唯一；deleted_token 支持多次分配/移除（BR-13,BR-25）',
  INDEX `idx_user_role_deleted` (`user_id`, `role_id`, `is_deleted`)     COMMENT '按活动记录判断用户是否已持有某角色（UC-14 重复分配校验 BR-13）',
  INDEX `idx_plan_expired_at` (`plan_expired_at`)                        COMMENT '按失效时间过滤（BR-24 有效期校验）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='用户角色关联表（N:M）；支持时效性分配（BR-24）；软删除（BR-25）；活动记录唯一；支持同一关系多次分配与多次移除（BR-13）';

-- ------------------------------------------------------------
-- 5. role_permission — 角色权限关联表（N:M）
-- 来源 UC: UC-21 查询角色权限, UC-22 授予角色权限,
--          UC-23 修改角色权限, UC-24 撤销角色权限,
--          UC-25 系统权限控制
-- 业务规则: BR-17 不重复授权, BR-24 有效期, BR-25 软删除
-- ------------------------------------------------------------
CREATE TABLE `role_permission` (
  `id`                INT UNSIGNED    NOT NULL AUTO_INCREMENT            COMMENT '主键',
  `role_id`           INT UNSIGNED    NOT NULL                           COMMENT '角色ID（关联 role.id）',
  `function_id`       INT UNSIGNED    NOT NULL                           COMMENT '功能ID（关联 sys_function.id）',
  `is_deleted`        TINYINT(1)      NOT NULL DEFAULT 0                 COMMENT '软删除/失效标记：0=有效，1=已失效（BR-25）',
  `deleted_token`     BIGINT UNSIGNED NOT NULL DEFAULT 0                 COMMENT '软删除版本号：活动记录固定为0；软删除时写入当前记录主键值，支持同一角色权限关系多次授权与多次撤销',
  `plan_effective_at` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权计划生效时间（BR-24）',
  `plan_expired_at`   DATETIME            NULL DEFAULT NULL              COMMENT '授权计划失效时间，NULL表示不限期（BR-24）',
  `granted_by`        INT UNSIGNED        NULL DEFAULT NULL              COMMENT '执行授权操作的管理员ID（关联 user.id）',
  `granted_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '授权记录创建时间',
  `created_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_role_id` (`role_id`)                                        COMMENT '按角色查询其所有权限（UC-21,UC-25）',
  INDEX `idx_function_id` (`function_id`)                                COMMENT '按功能查询其所有关联角色（UC-20 删除前检查引用）',
  UNIQUE INDEX `uq_role_permission_deleted_token` (`role_id`, `function_id`, `deleted_token`) COMMENT '角色-功能关系在活动记录内唯一；deleted_token 支持多次授权/撤销（BR-17,BR-25）',
  INDEX `idx_role_func_deleted` (`role_id`, `function_id`, `is_deleted`) COMMENT '按活动记录判断角色是否已有某功能权限（UC-22 重复授权校验 BR-17）',
  INDEX `idx_plan_expired_at` (`plan_expired_at`)                        COMMENT '按失效时间过滤（BR-24 有效期校验，UC-25 取有效权限集）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='角色权限关联表（N:M）；支持时效性授权（BR-24）；软删除（BR-25）；活动记录唯一；支持同一关系多次授权与多次撤销（BR-17）';

-- ------------------------------------------------------------
-- 6. login_attempt — 登入尝试记录表
-- 来源 UC: UC-02 用户登入（账号锁定机制）
-- 业务规则: BR-05 连续失败锁定阈值
-- ------------------------------------------------------------
CREATE TABLE `login_attempt` (
  `id`           INT UNSIGNED    NOT NULL AUTO_INCREMENT            COMMENT '主键',
  `username`     VARCHAR(64)     NOT NULL                           COMMENT '尝试登入的账号（存账号字符串，允许账号不存在时也记录）',
  `ip_address`   VARCHAR(45)     NOT NULL                           COMMENT '来源IP地址（支持IPv6）',
  `is_success`   TINYINT(1)      NOT NULL DEFAULT 0                 COMMENT '是否成功：0=失败，1=成功',
  `fail_reason`  VARCHAR(128)        NULL DEFAULT NULL              COMMENT '失败原因描述（内部记录，不对外展示）',
  `attempted_at` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '尝试时间',
  `created_at`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_username_attempted` (`username`, `attempted_at`)        COMMENT '按账号+时间查询连续失败次数（UC-02 锁定阈值判断 BR-05）',
  INDEX `idx_attempted_at` (`attempted_at`)                          COMMENT '按时间清理过期记录（历史数据归档）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='登入尝试记录表；记录每次登入成功/失败；用于账号锁定连续失败计数（BR-05）';

-- ------------------------------------------------------------
-- 7. account_lock — 账号锁定状态表
-- 来源 UC: UC-02 用户登入（扩展情节 5b: 连续失败达到锁定阈值）
-- 业务规则: BR-05 锁定阈值与锁定时长
-- ------------------------------------------------------------
CREATE TABLE `account_lock` (
  `id`              INT UNSIGNED    NOT NULL AUTO_INCREMENT            COMMENT '主键',
  `user_id`         INT UNSIGNED    NOT NULL                           COMMENT '被锁定的用户ID（关联 user.id）',
  `locked_at`       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '锁定触发时间',
  `unlock_at`       DATETIME        NOT NULL                           COMMENT '自动解锁时间（BR-05 锁定时长决定）',
  `lock_reason`     VARCHAR(256)        NULL DEFAULT NULL              COMMENT '锁定原因（如：连续失败N次）',
  `is_active`       TINYINT(1)      NOT NULL DEFAULT 1                 COMMENT '锁定是否仍有效：1=锁定中，0=已解锁',
  `created_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id_active` (`user_id`, `is_active`)                  COMMENT '查询用户当前是否处于锁定状态（UC-02 4c 扩展情节）',
  INDEX `idx_unlock_at` (`unlock_at`)                                   COMMENT '按解锁时间自动过期处理'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='账号锁定状态表；连续登入失败超阈值时写入（BR-05）；到期自动解锁';

-- ------------------------------------------------------------
-- 8. password_reset — 密码重置（忘记密码验证码）表
-- 来源 UC: UC-04 忘记密码
-- 业务规则: BR-02 密码复杂度, BR-07 重置后旧凭证立即失效,
--           BR-08 验证码有效期5分钟/频率限制
-- ------------------------------------------------------------
CREATE TABLE `password_reset` (
  `id`             INT UNSIGNED    NOT NULL AUTO_INCREMENT            COMMENT '主键',
  `user_id`        INT UNSIGNED    NOT NULL                           COMMENT '申请重置的用户ID（关联 user.id）',
  `contact_type`   VARCHAR(16)     NOT NULL                           COMMENT '联系方式类型：phone=手机号, email=邮箱',
  `contact_value`  VARCHAR(254)    NOT NULL                           COMMENT '实际使用的联系方式值',
  `verify_code`    VARCHAR(16)     NOT NULL                           COMMENT '6位随机验证码（已加密或明文，视实现策略）',
  `is_used`        TINYINT(1)      NOT NULL DEFAULT 0                 COMMENT '验证码是否已使用：0=未使用，1=已使用',
  `expired_at`     DATETIME        NOT NULL                           COMMENT '验证码过期时间（BR-08：生成后5分钟）',
  `used_at`        DATETIME            NULL DEFAULT NULL              COMMENT '验证码实际使用时间',
  `created_at`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id`)                                     COMMENT '按用户查询其验证码记录（UC-04 频率限制判断 BR-08）',
  INDEX `idx_expired_at` (`expired_at`)                               COMMENT '按过期时间过滤有效验证码（UC-04 8a 验证码过期扩展情节）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='密码重置验证码表；有效期5分钟（BR-08）；使用后标记is_used；不存储旧密码';

-- ------------------------------------------------------------
-- 9. audit_log — 审计日志主表（创建后只读）
-- 来源 UC: UC-02~08, UC-10~16, UC-18~25 所有写操作均写审计日志
-- 业务规则: BR-25 历史日志永久保留
-- ------------------------------------------------------------
CREATE TABLE `audit_log` (
  `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT            COMMENT '主键（使用BIGINT适应大量审计记录）',
  `operator_id`    INT UNSIGNED        NULL DEFAULT NULL              COMMENT '执行操作的用户ID（NULL表示系统自动触发）',
  `operator_name`  VARCHAR(128)        NULL DEFAULT NULL              COMMENT '执行操作时的账号快照（防止账号改名后历史记录失真）',
  `action`         VARCHAR(64)     NOT NULL                           COMMENT '操作类型（如：USER_CREATE, ROLE_ASSIGN, LOGIN_SUCCESS, PASSWORD_RESET 等）',
  `target_type`    VARCHAR(64)         NULL DEFAULT NULL              COMMENT '被操作对象类型（如：user, role, sys_function, role_permission）',
  `target_id`      INT UNSIGNED        NULL DEFAULT NULL              COMMENT '被操作对象ID',
  `target_name`    VARCHAR(256)        NULL DEFAULT NULL              COMMENT '被操作对象名称快照（防止删除后历史记录失真）',
  `result`         TINYINT(1)      NOT NULL DEFAULT 1                 COMMENT '操作结果：1=成功，0=失败',
  `ip_address`     VARCHAR(45)         NULL DEFAULT NULL              COMMENT '操作来源IP（支持IPv6）',
  `remark`         VARCHAR(500)        NULL DEFAULT NULL              COMMENT '备注信息（如失败原因、特殊说明）',
  `operated_at`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作发生时间',
  `created_at`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `updated_at`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间（正常情况下与created_at相同，审计记录不可修改）',
  PRIMARY KEY (`id`),
  INDEX `idx_operator_id` (`operator_id`)                             COMMENT '按操作人查询其操作历史',
  INDEX `idx_action` (`action`)                                       COMMENT '按操作类型过滤（如查询所有登入记录）',
  INDEX `idx_target` (`target_type`, `target_id`)                     COMMENT '按被操作对象查询相关审计记录',
  INDEX `idx_operated_at` (`operated_at`)                             COMMENT '按操作时间范围查询（审计报表）',
  INDEX `idx_result` (`result`)                                       COMMENT '按操作结果过滤（如查询所有失败操作）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='审计日志主表；记录所有关键操作；只允许INSERT不允许UPDATE/DELETE（BR-25）';

-- ------------------------------------------------------------
-- 10. audit_log_item — 审计日志明细表（1:N 附属于 audit_log）
-- 来源 UC: UC-07 编辑用户信息（含前后值对比）,
--          UC-11 编辑角色, UC-15 修改用户角色, UC-23 修改角色权限
-- 业务规则: BR-25 变更前后值永久保留
-- ------------------------------------------------------------
CREATE TABLE `audit_log_item` (
  `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT            COMMENT '主键',
  `audit_log_id`  BIGINT UNSIGNED NOT NULL                           COMMENT '关联的审计日志主表ID（关联 audit_log.id）',
  `field_name`    VARCHAR(128)    NOT NULL                           COMMENT '变更字段名称（业务语义名，如：手机号、账号状态）',
  `old_value`     TEXT                NULL DEFAULT NULL              COMMENT '变更前值（NULL表示新增操作无旧值）',
  `new_value`     TEXT                NULL DEFAULT NULL              COMMENT '变更后值（NULL表示删除操作无新值）',
  `created_at`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_audit_log_id` (`audit_log_id`)                          COMMENT '按主日志ID查询其所有明细变更项'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
  COMMENT='审计日志明细表（1:N）；记录字段级变更前后值；只允许INSERT（BR-25）';

-- ------------------------------------------------------------
-- 11. 种子数据 — 最高权限账号 admin / admin
-- 用途: 自动化测试、人工验收、首次系统引导
-- 约束: 数据库中仅保存 BCrypt 哈希值，禁止明文密码落库
-- ------------------------------------------------------------
INSERT INTO `role`
(`id`, `role_name`, `description`, `is_deleted`, `deleted_token`, `plan_effective_at`, `plan_expired_at`, `created_at`, `updated_at`)
VALUES
(1, '超级管理员', '系统内建最高权限角色；默认分配给种子账号 admin', 0, 0, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO `user`
(`id`, `username`, `display_name`, `password_hash`, `phone`, `email`, `status`, `is_deleted`, `deleted_token`,
 `force_change_password`, `credentials_version`, `plan_effective_at`, `plan_expired_at`,
 `last_login_at`, `last_login_ip`, `created_at`, `updated_at`)
VALUES
(1, 'admin', '系统管理员', '$2a$10$Vgan3lridaj9YInauFB5KuYTovVu2VA.Bo.Rm8Cuc1e2hkH0tPopi',
 '13800000000', 'admin@security-system.local', 1, 0, 0,
 0, 1, CURRENT_TIMESTAMP, NULL,
 NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ── 系统功能种子数据（25 条，权限码 = HTTP_METHOD:URI）──
-- module 字段存储权限码，格式：HTTP_METHOD:URI，与 ApiAuthorizationFilter 精确匹配，零转换
INSERT INTO `sys_function`
(`id`, `function_name`, `description`, `module`, `is_deleted`, `deleted_token`,
 `plan_effective_at`, `plan_expired_at`, `created_at`, `updated_at`)
VALUES
-- 认证鉴权（UC-01~04）
( 1, '用户注册',           '提交注册信息并创建账号',        'POST:/api/v1/auth/register',            0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 2, '用户登录',           '提交凭证并签发访问令牌',        'POST:/api/v1/auth/login',               0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 3, '用户登出',           '注销当前会话并使令牌失效',      'POST:/api/v1/auth/logout',              0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 4, '发送重置密码验证码', '发起重置密码验证码申请',        'POST:/api/v1/auth/password-reset-codes',0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 5, '重置登录密码',       '校验验证码后更新登录密码',      'POST:/api/v1/auth/password-resets',     0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 用户管理（UC-05~08）
( 6, '查询用户列表',       '查看系统所有用户账号',          'GET:/api/v1/users',                    0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 7, '新增用户账号',       '创建新的用户账号',              'POST:/api/v1/users',                   0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 8, '修改用户资料',       '编辑用户基本信息',              'PATCH:/api/v1/users',                  0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 9, '停用或删除用户',     '停用或删除用户账号',            'DELETE:/api/v1/users',                 0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 角色管理（UC-09~12）
(10, '查询角色列表',       '查看系统所有角色',              'GET:/api/v1/roles',                    0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, '新增角色',           '创建新的业务角色',              'POST:/api/v1/roles',                   0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, '修改角色',           '编辑角色名称或描述',            'PATCH:/api/v1/roles',                  0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, '删除角色',           '删除不再使用的角色',            'DELETE:/api/v1/roles',                 0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 用户角色（UC-13~16）
(14, '查询用户已分配角色', '查看指定用户的角色列表',        'GET:/api/v1/user-roles',               0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, '分配角色给用户',     '为用户绑定业务角色',            'POST:/api/v1/user-roles',              0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(16, '修改用户角色有效期', '调整角色绑定的有效时间段',      'PATCH:/api/v1/user-roles',             0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(17, '撤销用户角色',       '解除用户与角色的绑定',          'DELETE:/api/v1/user-roles',            0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 系统功能管理（UC-17~20）
(18, '查询系统功能列表',   '查看所有已注册的系统功能',      'GET:/api/v1/functions',                0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(19, '注册系统功能',       '向系统注册新的功能点',          'POST:/api/v1/functions',               0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(20, '修改系统功能',       '编辑已注册的功能点信息',        'PATCH:/api/v1/functions',              0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(21, '删除系统功能',       '注销不再使用的功能点',          'DELETE:/api/v1/functions',             0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- 角色权限（UC-21~24）
(22, '查询角色已授权限',   '查看角色拥有的权限列表',        'GET:/api/v1/role-permissions',         0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(23, '授予角色权限',       '为角色绑定系统功能点权限',      'POST:/api/v1/role-permissions',        0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(24, '修改授权有效期',     '调整角色权限的有效时间段',      'PUT:/api/v1/role-permissions',         0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(25, '撤销角色权限',       '解除角色与功能点的权限绑定',    'DELETE:/api/v1/role-permissions',      0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO `user_role`
(`id`, `user_id`, `role_id`, `is_deleted`, `deleted_token`, `plan_effective_at`, `plan_expired_at`,
 `assigned_by`, `assigned_at`, `created_at`, `updated_at`)
VALUES
(1, 1, 1, 0, 0, CURRENT_TIMESTAMP, NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ── 角色权限种子数据（role 1 超级管理员持有全部 25 个功能点）──
INSERT INTO `role_permission`
(`id`, `role_id`, `function_id`, `is_deleted`, `deleted_token`, `plan_effective_at`, `plan_expired_at`,
 `granted_by`, `granted_at`, `created_at`, `updated_at`)
VALUES
( 1, 1,  1, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 2, 1,  2, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 3, 1,  3, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 4, 1,  4, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 5, 1,  5, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 6, 1,  6, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 7, 1,  7, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 8, 1,  8, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
( 9, 1,  9, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 1, 10, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 1, 11, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 1, 12, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 1, 13, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, 1, 14, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, 1, 15, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(16, 1, 16, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(17, 1, 17, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(18, 1, 18, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(19, 1, 19, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(20, 1, 20, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(21, 1, 21, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(22, 1, 22, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(23, 1, 23, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(24, 1, 24, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(25, 1, 25, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 12. 改造新增：审计日志读取（W02-sec 读取接口 / W03-sec 权限项）
-- ============================================================
INSERT INTO `sys_function`
(`id`, `function_name`, `description`, `module`, `is_deleted`, `deleted_token`,
 `plan_effective_at`, `plan_expired_at`, `created_at`, `updated_at`)
VALUES
(26, '查询审计日志', '分页查询审计日志（改造新增）', 'GET:/api/v1/audit-logs', 0, 0, '2026-01-01 00:00:00', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO `role_permission`
(`id`, `role_id`, `function_id`, `is_deleted`, `deleted_token`, `plan_effective_at`, `plan_expired_at`,
 `granted_by`, `granted_at`, `created_at`, `updated_at`)
VALUES
(26, 1, 26, 0, 0, '2026-01-01 00:00:00', NULL, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
