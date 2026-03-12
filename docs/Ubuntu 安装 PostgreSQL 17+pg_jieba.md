# **PostgreSQL 17 + pg_jieba 部署与运维手册**

适用环境：Ubuntu 22.04 / 24.04 LTS  
数据库版本：PostgreSQL 17.x  
插件版本：pg_jieba (基于最新 master 分支)  
更新日期：2025-11-22

---

## **一、 前置准备 (Root 权限)**

### **1. 安装 PostgreSQL 17**

如果您尚未安装数据库，请执行以下步骤。如果已安装，请跳至第2步。

Bash

# 1. 导入官方签名并添加仓库  
sudo apt-get update  
sudo apt-get install -y dirmngr ca-certificates software-properties-common gnupg gnupg2 lsb-release curl  
curl -fSsL https://www.postgresql.org/media/keys/ACCC4CF8.asc | sudo gpg --dearmor | sudo tee /usr/share/keyrings/postgresql.gpg > /dev/null  
echo "deb [signed-by=/usr/share/keyrings/postgresql.gpg] http://apt.postgresql.org/pub/repos/apt $(lsb_release -cs)-pgdg main" | sudo tee /etc/apt/sources.list.d/pgdg.list

# 2. 安装 Server  
sudo apt-get update  
sudo apt-get install -y postgresql-17

### **2. 安装编译依赖 (关键)**

**注意**：必须安装 postgresql-server-dev-17，否则会出现 postgres.h not found 错误。

Bash

sudo apt-get install -y postgresql-server-dev-17 git cmake build-essential

---

## **二、 编译安装 pg_jieba**

此步骤修正了常见的“版本不匹配”问题。我们通过**强制指定 PATH** 确保 cmake 找到的是 PG 17 而不是系统旧版本。

### **1. 下载源码**

Bash

cd /tmp  
git clone https://github.com/jaiminpan/pg_jieba.git  
cd pg_jieba  
# 拉取子模块 (必须执行，否则编译会缺文件)  
git submodule update --init --recursive

### **2. 编译构建**

Bash

mkdir build  
cd build

# --- 关键修正步骤开始 ---  
# 1. 清理旧环境，防止缓存干扰  
rm -rf *

# 2. 强制将 PG 17 的 bin 目录加入 PATH 首位  
# 这能避免出现 "incompatible library: version mismatch" 错误  
export PATH=/usr/lib/postgresql/17/bin:$PATH

# 3. 验证当前 cmake 能找到正确的 pg_config (应输出 PostgreSQL 17.x)  
pg_config --version 

# 4. 生成构建文件  
cmake ..

# 5. 编译并安装  
make  
sudo make install  
# --- 关键修正步骤结束 ---

---

## **三、 配置与启用**

### **1. 修改 postgresql.conf**

为了获得最佳性能，将插件加入预加载库。

Bash

# 编辑配置文件 (路径视实际情况而定)  
sudo nano /etc/postgresql/17/main/postgresql.conf

找到 shared_preload_libraries 参数，修改如下（注意使用单引号）：

Ini, TOML

shared_preload_libraries = 'pg_jieba'

### **2. 重启服务**

Bash

sudo systemctl restart postgresql

验证服务状态：  
执行 sudo systemctl status postgresql。  
如果看到 Active: active (exited) 或 active (running) 且没有报错日志，说明加载成功。

### **3. 数据库内创建扩展**

切换到 postgres 用户登录数据库：

Bash

sudo -u postgres psql

在 SQL 终端执行：

SQL

-- 切换到你的业务数据库  
\c postgres

-- 创建插件  
CREATE EXTENSION pg_jieba;

-- 验证分词 (测试是否能切出 'pg_jieba')  
SELECT * FROM to_tsvector('jiebacfg', 'PostgreSQL17成功安装pg_jieba');

---

## **四、 运维与故障恢复**

### **1. 词典路径与自定义词库**

pg_jieba 是基于文本文件的词库，不支持 SQL 动态插入，需修改文件。

* **词典位置**：/usr/share/postgresql/17/tsearch_data/  
* **自定义词典文件**：jieba_user.dict (或其他后缀为 .dict 的文件)  
* **生效方式**：修改文件后，必须**重启 PostgreSQL 服务**。

### **2. 紧急救援 (如果数据库启动失败)**

如果在修改 shared_preload_libraries 后数据库无法启动（报错 incompatible library 或 file not found）：

1. **查看报错**：tail -n 20 /var/log/postgresql/postgresql-17-main.log  
2. **回滚配置**：  
   * 编辑 postgresql.conf。  
   * 注释掉那一行：# shared_preload_libraries = 'pg_jieba'。  
3. **启动数据库**：sudo systemctl start postgresql。  
4. **重新检查**：确认 pg_jieba.so 是否是用 PG 17 的头文件编译的（参考本文第二部分的 export PATH 步骤）。

---

### **存档备注**

* **常见坑点 1**：只装了 postgresql-17 没装 postgresql-server-dev-17，导致无法编译。  
* **常见坑点 2**：系统里有旧版 PG (如 pg13/14)，cmake 自动链接了旧版，导致启动时报 Version Mismatch。**解决方案是 export PATH。**