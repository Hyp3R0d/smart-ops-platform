# Smart Ops Platform（智能企业运营管理平台）

面向中型企业的前后端分离微服务运营管理平台，聚焦：**组织架构、采购流程、库存预警、经营看板** 四大业务域。项目采用 Maven 多模块 + Spring Cloud Alibaba 微服务体系，前端为 Vue3 + Element Plus 管理后台。

---

## 1. 技术栈

### 后端
- Java 17
- Spring Boot 3.x
- Spring Cloud Alibaba（Nacos、OpenFeign）
- Spring Security + JWT
- MyBatis Plus
- MySQL 8
- Redis
- Kafka
- Seata（分布式事务接入点）
- XXL-JOB（定时任务接入点）
- Knife4j / OpenAPI
- Lombok

### 前端
- Vue 3 + Vite
- Element Plus
- Pinia
- Vue Router
- Axios
- ECharts

### 部署
- Docker / Docker Compose
- Kubernetes YAML（目录预留）
- Nginx（目录预留）

---

## 2. 仓库结构

```text
smart-ops-platform
├─ backend
│  ├─ pom.xml
│  ├─ common
│  ├─ gateway-service
│  ├─ auth-service
│  ├─ system-service
│  ├─ organization-service
│  ├─ purchase-service
│  ├─ inventory-service
│  ├─ dashboard-service
│  ├─ job-service
│  └─ sql
├─ frontend
│  └─ admin-web
└─ deploy
   ├─ docker
   ├─ k8s
   ├─ nginx
   └─ scripts
```

---

## 3. 微服务端口规划

| 服务 | 端口 | 说明 |
|---|---:|---|
| gateway-service | 8080 | API 网关、统一鉴权入口 |
| auth-service | 8081 | 登录/登出/JWT 颁发 |
| system-service | 8082 | 用户/角色/菜单/权限 |
| organization-service | 8083 | 组织树/岗位/员工 |
| purchase-service | 8084 | 供应商/采购申请/审批/入库触发 |
| inventory-service | 8085 | 仓库/物料/库存/阈值/预警 |
| dashboard-service | 8086 | 看板聚合统计 |
| job-service | 8087 | 定时扫描/预警任务 |
| admin-web | 3000 | 前端管理台 |

---

## 4. 关键业务能力

1. **认证与权限（RBAC）**
   - 登录 / 登出 / 当前用户信息
   - 用户、角色、菜单、权限基础管理
   - JWT 鉴权 + 路由守卫 + 按钮权限控制（前端）

2. **组织架构管理**
   - 部门树（递归结构）
   - 岗位管理
   - 员工管理
   - 组织树与权限数据可接 Redis 缓存

3. **采购流程**
   - 供应商管理
   - 采购单：DRAFT → PENDING_APPROVAL → APPROVED/REJECTED → ORDERED → STOCKED
   - 采购入库事件通过 Kafka 异步流转至库存服务
   - OpenFeign 服务调用 + Seata 事务扩展位

4. **库存与预警**
   - 仓库、物料、库存、库存流水
   - 入库 / 出库
   - 阈值配置 + 低库存告警记录
   - XXL-JOB 风格定时扫描 + 邮件通知（支持 mock）

5. **经营看板**
   - 采购总量、待审批、低库存
   - 入库趋势、采购状态分布
   - 预警趋势、热门物料、部门采购排行

---

## 5. 本地开发启动（推荐）

> 以下步骤按 **Windows / macOS / Linux 通用思路** 编写，命令可按本机环境调整。

### 5.1 环境要求

- JDK 17+
- Maven 3.9+
- Node.js 18+
- MySQL 8
- Redis 7
- Kafka 3+
- Nacos 2+

### 5.2 启动基础中间件（示例）

可用本机已安装服务，也可使用 Docker（示例）启动：

```bash
# MySQL
docker run -d --name smart-mysql -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=smart_ops mysql:8

# Redis
docker run -d --name smart-redis -p 6379:6379 redis:7

# Nacos（单机）
docker run -d --name smart-nacos -p 8848:8848 \
  -e MODE=standalone nacos/nacos-server:v2.3.2

# Kafka（如果你已使用自有 Kafka，可跳过）
# 建议按团队标准镜像或现有集群启动
```

### 5.3 初始化数据库

1）创建数据库：

```sql
CREATE DATABASE IF NOT EXISTS smart_ops DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2）执行项目 SQL（建议放置于 `backend/sql` 目录并按顺序导入）：

- 建表脚本（schema）
- 初始化脚本（seed data）

> 默认管理员约定：
> - username: `admin`
> - password: `Admin@123`

### 5.4 编译后端

```bash
cd backend
mvn clean install -DskipTests
```

### 5.5 启动后端服务（多终端）

在 `backend` 目录下分别执行：

```bash
mvn -pl auth-service spring-boot:run
mvn -pl system-service spring-boot:run
mvn -pl organization-service spring-boot:run
mvn -pl purchase-service spring-boot:run
mvn -pl inventory-service spring-boot:run
mvn -pl dashboard-service spring-boot:run
mvn -pl job-service spring-boot:run
mvn -pl gateway-service spring-boot:run
```

> 建议最后启动网关（gateway-service）。

### 5.6 启动前端

```bash
cd frontend/admin-web
npm install
npm run dev
```

启动后访问：`http://localhost:3000`

---

## 6. 登录与访问地址

### 默认账号
- 用户名：`admin`
- 密码：`Admin@123`

### 主要访问入口
- 前端：`http://localhost:3000`
- 网关：`http://localhost:8080`

### 接口文档（按服务）
- Auth: `http://localhost:8081/doc.html`
- System: `http://localhost:8082/doc.html`
- Organization: `http://localhost:8083/doc.html`
- Purchase: `http://localhost:8084/doc.html`
- Inventory: `http://localhost:8085/doc.html`
- Dashboard: `http://localhost:8086/doc.html`
- Job: `http://localhost:8087/doc.html`

---

## 7. 推荐演示链路

1. 登录系统（admin）
2. 进入组织管理，查看/维护部门树与员工
3. 新建供应商 + 新建采购申请单
4. 采购审批通过并下单
5. 执行入库动作（触发 Kafka 事件）
6. 在库存模块查看库存变化与预警
7. 在看板首页查看实时统计结果

---

## 8. 配置说明（关键）

各服务 `application.yml` 统一支持环境变量覆盖，常用变量：

- `MYSQL_HOST` / `MYSQL_PORT` / `MYSQL_USER` / `MYSQL_PASSWORD`
- `REDIS_HOST` / `REDIS_PORT`
- `NACOS_ADDR`
- `KAFKA_BOOTSTRAP_SERVERS`
- `JWT_SECRET`

开发默认 JWT：
- secret: `smart-ops-dev-secret-key-please-change-in-production-123456`

---

## 9. 容器化与 K8s 部署说明

项目已预留目录：

- `deploy/docker`
- `deploy/k8s`
- `deploy/nginx`
- `deploy/scripts`

建议团队在上述目录维护：

1. `docker-compose.yml`（中间件 + 各服务 + 前端 + Nginx）
2. K8s `Deployment/Service/ConfigMap/Secret/Ingress`
3. Nginx 反向代理配置（`/api -> gateway:8080`）

---

## 10. 常见问题

### Q1：登录 401
- 检查网关 JWT 配置与各服务 JWT secret 是否一致。

### Q2：服务注册失败
- 检查 Nacos 是否可达（`NACOS_ADDR`）并确认服务名无冲突。

### Q3：采购入库后库存未更新
- 检查 Kafka topic、生产者/消费者配置、消费组与日志输出。

### Q4：前端菜单不显示
- 检查 `/auth/me` 与 `/system/auth/info` 返回数据结构是否完整。

---

## 11. 开发建议

- 以 `common` 模块作为统一基础能力（返回体、异常、JWT 工具、公共 DTO）。
- 业务迭代优先补齐 `backend/sql` 与 `deploy/*` 中的团队标准化资产。
- 上线前务必替换 JWT secret、数据库口令、Nacos/Kafka 凭据，并启用 HTTPS。

---

如需我继续下一步，我可以直接补齐：
1）完整 SQL（建表 + 初始化）
2）docker-compose + k8s + nginx 文件
3）前端缺失页面和 API 模块
