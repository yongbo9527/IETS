# IETS - Income and Expense Tracking System  
> 收支记录系统 · 基于 DDD + CQRS 的现代化财务管理系统

📅 **最后更新**：2025年10月11日  
🎯 **当前阶段**：完成收支记录模块重构

---

## 🏗️ 项目概述

IETS 是一个轻量级个人/家庭收支管理平台，旨在通过**领域驱动设计（DDD）** 和 **CQRS 架构模式**，构建高内聚、低耦合、易于扩展的财务核心模块。

本版本完成了 **收支记录模块** 的重构，为后续预算管理、报表分析、多账户支持等模块奠定基础。

---

## 📅 变更记录（Changelog）

| 日期       | 修改人  | 变更类型 | 模块     | 变更内容                           | 关联 Issue/PR |
| ---------- | ------- | -------- | -------- | ---------------------------------- | ------------- |
| 2024-10-30 | @ron.yu | 🆕 新增   | 构建项目 | 搭成收支系统框架                   | #PR23         |
| 2024-12-30 | @ron.yu | 🆕 新增   | 收支系统 | 收支记录、报表、类目               | #PR23         |
| 2025-10-11 | @ron.yu | 🛠️ 重构   | 收支记录 | 实现 CQRS 架构，分离命令与查询逻辑 | #PR23         |
|            |         |          |          |                                    |               |
|            |         |          |          |                                    |               |
|            |         |          |          |                                    |               |
|            |         |          |          |                                    |               |

## 🧱 架构设计

### ✅ 核心理念

- **领域驱动设计（DDD）**  
  以 `Finance` 领域为核心，划分聚合（Aggregate）、实体（Entity）、值对象（Value Object），确保业务逻辑内聚。

- **CQRS（命令查询职责分离）**  
  - **Command Side（写）**：通过领域模型处理业务规则，保证数据一致性。  
  - **Query Side（读）**：独立查询模型，支持高性能、灵活的表格展示。

- **分层架构**  
  严格遵循六边形架构（Hexagonal Architecture）思想：

[API 接口] → [应用层] → [领域层] → [基础设施]
                    ↖           ↙
                   [领域事件驱动]

---

## 🧩 当前模块：收支记录（Tally）

### 功能范围
- 新增/修改/删除收支记录
- 按日期范围查询明细
- 动态生成分类余额表格（支持父子类目嵌套）

### 领域模型
- **聚合根**：`DailyExpenseRecord`
- **实体**：`Category`
- **值对象**：`Money`（金额）、`Remark`（备注）
- **领域事件**：`BalanceRecordCreatedEvent`、`BalanceRecordUpdatedEvent`

### CQRS 实现
| 方向              | 实现方式                                                     |
| ----------------- | ------------------------------------------------------------ |
| **Command（写）** | `AddBalanceCommand` → `FinanceCommandHandler` → `FinanceAppService` → 领域聚合 |
| **Query（读）**   | `QueryBalanceTableQuery` → `BalanceQueryHandler` → 直接查询预计算视图或缓存 |

> 🔔 **读写分离策略**：  
> 写操作通过领域模型持久化到 MySQL；  
> 读操作通过 RabbitMQ 异步更新 Redis 缓存或物化视图，提升查询性能。

---

## ⚙️ 技术栈

| 类别         | 技术                                        |
| ------------ | ------------------------------------------- |
| **核心框架** | Spring Boot 3.x, Spring Security            |
| **数据库**   | MySQL（主库，存储领域数据）                 |
| **缓存**     | Redis（缓存查询结果、动态表头）             |
| **消息队列** | RabbitMQ（异步更新读模型、解耦领域事件）    |
| **安全**     | Spring Security + JWT（用户认证与权限控制） |
| **API 文档** | Swagger / OpenAPI                           |
| **部署**     | Docker + Nginx（可选）                      |

---

## 📂 推荐代码结构（模块化）

```bash
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── iets/
│   │           ├── tally/                  # 收支记录模块（限界上下文）
│   │           │   ├── application/        # 应用层：Command/Query Handler
│   │           │   ├── domain/             # 领域层：聚合、实体、服务、事件
│   │           │   ├── infrastructure/     # 基础设施：Repository、MQ、Redis 适配
│   │           │   └── interface/          # 接口层：Controller、VO、DTO
│   │           └── common/                 # 通用组件：Money、Result<T>
│   └── resources/
│       ├── application.yml
│       └── db/migration/                   # 数据库迁移脚本（Flyway/Liquibase）
```

## 🚀 后续规划

- [ ] 引入 **事件溯源（Event Sourcing）** 记录所有状态变更
- [ ] 实现 **动态报表引擎**，支持自定义维度分析
- [ ] 添加 **预算管理模块**
- [ ] 支持 **多用户 & 权限控制**
- [ ] 提供 **移动端 API 接口**

---

## 📚 参考资料

- 《领域驱动设计精粹》- Vaughn Vernon
- 《Implementing Domain-Driven Design》- Vaughn Vernon
- CQRS Pattern - Microsoft Docs
- Spring Data, Spring Security 官方文档