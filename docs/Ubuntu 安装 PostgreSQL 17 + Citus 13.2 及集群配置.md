# 完整可复用：Ubuntu 安装 PostgreSQL 17 + Citus 13.2 （含集群配置）

## 说明

因 Citus 13.2 官方仅适配到 PostgreSQL 17，此文档为稳定兼容版本，包含「单节点安装」「集群配置」「功能验证」全流程，可直接复用。

## 一、环境准备



* 系统：Ubuntu 18.04+/20.04+/22.04+（64 位）

* 网络：集群节点间互通（开放 5432 端口）

* 权限：需 `sudo` 权限执行安装命令

## 二、单节点基础安装（所有节点均需执行）

### 1. 安装 PostgreSQL 17

#### （1）添加 PostgreSQL 官方仓库（适配 apt-key 弃用）



```
# 下载并导入 PostgreSQL 仓库密钥（使用密钥环管理）

wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo gpg --dearmor -o /usr/share/keyrings/postgresql-archive-keyring.gpg

# 创建仓库源配置文件

sudo sh -c 'echo "deb [signed-by=/usr/share/keyrings/postgresql-archive-keyring.gpg] http://apt.postgresql.org/pub/repos/apt/ $(lsb_release -cs)-pgdg main" > /etc/apt/sources.list.d/pgdg.list'

# 更新软件包索引

sudo apt-get update
```

#### （2）安装 PostgreSQL 17 核心组件



```
# 安装 PostgreSQL 17 服务器和客户端

sudo apt-get install -y postgresql-17 postgresql-client-17 postgresql-server-dev-17
```

#### （3）验证 PostgreSQL 安装



```
# 启动 PostgreSQL 17 服务

sudo systemctl start postgresql@17-main

# 检查服务状态（显示 active (running) 即为成功）

sudo systemctl status postgresql@17-main

# 免密登录测试（默认 postgres 用户无密码）

sudo -i -u postgres psql -c "SELECT version();"
```

### 2. 安装并集成 Citus 13.2

#### （1）添加 Citus 官方仓库



```
# 导入 Citus 仓库密钥并添加仓库

curl https://install.citusdata.com/community/deb.sh | sudo bash

# 安装适配 PostgreSQL 17 的 Citus 13.2

sudo apt-get install -y postgresql-17-citus-13.2
```

#### （2）配置 Citus 预加载



```
# 修改 postgresql.conf，添加 Citus 到共享预加载库

sudo pg_conftool 17 main set shared_preload_libraries citus

# 重启 PostgreSQL 使配置生效

sudo systemctl restart postgresql@17-main
```

#### （3）启用 Citus 扩展



```
# 切换到 postgres 用户并进入 PostgreSQL 命令行

sudo -i -u postgres psql

# 创建 Citus 扩展

CREATE EXTENSION citus;

# 验证 Citus 版本（应返回 13.2 相关信息）

SELECT citus_version();

# 退出 psql

\q
```

### 3. 初始安全配置（可选，生产环境推荐）



```
# 登录 PostgreSQL 设置 postgres 用户密码

sudo -i -u postgres psql

ALTER USER postgres PASSWORD 'YourStrongPassword123!';  # 替换为强密码

\q

# 配置允许密码认证（编辑 pg_hba.conf）

sudo nano /etc/postgresql/17/main/pg_hba.conf

# 将以下行的认证方式改为 scram-sha-256（示例）

# host    all             postgres        127.0.0.1/32            scram-sha-256

# host    all             postgres        ::1/128                 scram-sha-256

# 重启服务生效

sudo systemctl restart postgresql@17-main
```

## 三、Citus 集群配置（1 协调节点 + N 工作节点）

### 1. 集群规划（示例）



| 节点角色              | IP 地址         | 节点标识        |
| ----------------- | ------------- | ----------- |
| 协调节点（Coordinator） | 192.168.1.100 | coordinator |
| 工作节点 1（Worker）    | 192.168.1.101 | worker1     |
| 工作节点 2（Worker）    | 192.168.1.102 | worker2     |

### 2. 协调节点配置（仅 192.168.1.100 执行）

#### （1）修改 PostgreSQL 配置文件



```
# 编辑 postgresql.conf

sudo nano /etc/postgresql/17/main/postgresql.conf

# 添加/修改以下配置（按实际需求调整）

listen_addresses = '*'          # 允许所有IP访问（生产环境可指定节点IP）

max_connections = 1000          # 最大连接数（需大于工作节点连接总和）

citus.node_name = 'coordinator' # 节点唯一标识

citus.enable_version_checks = off # 关闭版本检查（避免兼容告警）
```

#### （2）配置客户端认证（允许工作节点连接）



```
# 编辑 pg_hba.conf

sudo nano /etc/postgresql/17/main/pg_hba.conf

# 添加以下行（允许集群网段内的节点访问）

host    all             all             192.168.1.0/24            scram-sha-256

# 重启服务生效

sudo systemctl restart postgresql@17-main
```

### 3. 工作节点配置（192.168.1.101、192.168.1.102 均执行）

#### （1）修改 PostgreSQL 配置文件



```
# 编辑 postgresql.conf

sudo nano /etc/postgresql/17/main/postgresql.conf

# 添加/修改以下配置

listen_addresses = '*'          # 允许所有IP访问

max_connections = 800           # 工作节点连接数（按需调整）

wal_level = logical             # Citus 依赖逻辑复制

max_wal_senders = 10            # 至少为集群节点数 + 2

wal_keep_size = 1GB             # 保留足够WAL日志供同步

citus.node_name = 'worker1'     # 每个工作节点修改为唯一标识（如 worker1、worker2）

citus.enable_version_checks = off
```

#### （2）配置客户端认证（允许协调节点连接）



```
# 编辑 pg_hba.conf

sudo nano /etc/postgresql/17/main/pg_hba.conf

# 添加以下行（允许协调节点IP访问）

host    all             all             192.168.1.100/32          scram-sha-256

host    all             all             192.168.1.0/24            scram-sha-256  # 可选，允许其他工作节点

# 重启服务生效

sudo systemctl restart postgresql@17-main
```

### 4. 集群初始化（在协调节点 192.168.1.100 执行）

#### （1）添加工作节点到集群



```
# 登录协调节点的 PostgreSQL

sudo -i -u postgres psql -h 192.168.1.100 -U postgres

# 添加工作节点（输入 postgres 用户密码）

SELECT citus_add_node('192.168.1.101', 5432);

SELECT citus_add_node('192.168.1.102', 5432);

# 验证节点状态（应显示所有工作节点）

SELECT * FROM citus_get_active_worker_nodes();

# 查看集群节点详情

SELECT * FROM citus_nodes;
```

#### （2）可选：设置节点标签（用于分片策略）



```
-- 为工作节点添加区域标签（示例）

SELECT citus_set_node_property('192.168.1.101', 'region', 'asia');

SELECT citus_set_node_property('192.168.1.102', 'region', 'europe');
```

## 四、集群功能验证

### 1. 创建分布式表



```
# 在协调节点执行

sudo -i -u postgres psql -h 192.168.1.100 -U postgres

# 创建普通表

CREATE TABLE user_behavior (
    id SERIAL PRIMARY KEY,
    user_id BIGINT,
    behavior_type TEXT,
    action_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    region TEXT
);

# 转换为分布式表（按 region 字段分片）

SELECT create_distributed_table('user_behavior', 'region');

# 查看分片信息（应显示分片分布在不同工作节点）

SELECT * FROM citus_shards WHERE table_name = 'user_behavior';
```

### 2. 插入数据并验证分布



```
-- 插入测试数据（不同 region 对应不同分片）

INSERT INTO user_behavior (user_id, behavior_type, region)
VALUES
(1001, 'click', 'asia'),
(1002, 'purchase', 'europe'),
(1003, 'browse', 'asia'),
(1004, 'collect', 'europe');

-- 查询所有数据（协调节点自动聚合工作节点结果）

SELECT * FROM user_behavior;

-- 验证数据分布（查看每个分片的行数）

SELECT shardid, count(*) FROM user_behavior GROUP BY shardid;
```

### 3. 执行分布式查询



```
-- 按 region 聚合统计

SELECT region, behavior_type, COUNT(*) AS total
FROM user_behavior
GROUP BY region, behavior_type;
```

## 五、后续管理操作

### 1. 新增工作节点



1. 按「二、单节点基础安装」在新节点完成 PostgreSQL 17 + Citus 13.2 安装；

2. 按「三、3. 工作节点配置」修改新节点的 `postgresql.conf` 和 `pg_hba.conf`；

3. 在协调节点执行：



```
SELECT citus_add_node('新节点IP', 5432);
```

### 2. 移除工作节点（需先迁移数据）



```
-- 在协调节点执行

SELECT citus_remove_node('要移除的节点IP', 5432);
```

### 3. 集群监控



```
-- 查看分布式查询活动

SELECT * FROM citus_stat_activity;

-- 检查节点健康状态

SELECT * FROM citus_node_health;

-- 查看分片分布状态

SELECT * FROM citus_shard_placement;
```

### 4. 服务启停命令



```
# 启动服务

sudo systemctl start postgresql@17-main

# 停止服务

sudo systemctl stop postgresql@17-main

# 重启服务

sudo systemctl restart postgresql@17-main

# 设置开机自启

sudo systemctl enable postgresql@17-main
```

## 六、常见问题排查



1. **节点添加失败**：

* 检查节点间网络连通性（`ping 节点IP`、`telnet 节点IP 5432`）；

* 确认 `pg_hba.conf` 已允许对应节点访问；

* 检查工作节点 PostgreSQL 服务是否正常运行。

1. **分布式表创建失败**：

* 确保表有主键（Citus 分布式表要求主键包含分片键）；

* 分片键字段需存在且类型合法。

1. **数据查询无结果**：

* 检查分片分布（`SELECT * FROM citus_shards`）；

* 确认工作节点数据插入成功（可直接登录工作节点查询本地表）。

> （注：文档部分内容可能由 AI 生成）