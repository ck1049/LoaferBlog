# SQL 项目规则

## 1. 建表语句规范

### 1.1 IF NOT EXISTS 判断
- 所有 `CREATE TABLE` 语句必须添加 `IF NOT EXISTS` 判断，避免表已存在时的错误
- 示例：
  ```sql
  CREATE TABLE IF NOT EXISTS table_name (
      -- 字段定义
  );
  ```

### 1.2 表注释
- 所有表必须添加表级注释，使用 `COMMENT ON TABLE` 语句
- 示例：
  ```sql
  COMMENT ON TABLE table_name IS '表描述';
  ```

### 1.3 字段注释
- 所有字段必须添加字段级注释，使用 `COMMENT ON COLUMN` 语句
- 示例：
  ```sql
  COMMENT ON COLUMN table_name.column_name IS '字段描述';
  ```

### 1.4 字段内联注释
- 除了使用 `COMMENT ON COLUMN` 语句外，建议在字段定义后添加内联注释，提高代码可读性
- 示例：
  ```sql
  id SERIAL PRIMARY KEY,              -- 字段描述
  ```

## 2. 数据操作规范

### 2.1 插入数据
- 插入数据时，建议明确指定字段名，避免因表结构变化导致的错误
- 示例：
  ```sql
  INSERT INTO table_name (column1, column2) VALUES
  (value1, value2);
  ```

### 2.2 删除数据
- 删除表时，必须使用 `IF EXISTS` 判断，避免表不存在时的错误
- 示例：
  ```sql
  DROP TABLE IF EXISTS table_name;
  ```

## 3. 命名规范

### 3.1 表名
- 表名使用小写字母，单词之间用下划线分隔
- 示例：`user_role`、`post_category`

### 3.2 字段名
- 字段名使用小写字母，单词之间用下划线分隔
- 示例：`user_id`、`created_at`

## 4. 其他规范

### 4.1 时间戳字段
- 建议为所有表添加 `created_at` 和 `updated_at` 字段，默认值为 `CURRENT_TIMESTAMP`

### 4.2 状态字段
- 建议使用 `status` 字段表示记录状态，1 表示正常/启用，0 表示禁用/草稿

### 4.3 外键约束
- 建立外键关系时，建议添加 `ON DELETE CASCADE` 选项，确保数据一致性

### 4.4 唯一约束
- 对于需要唯一的字段组合，建议添加 `UNIQUE` 约束
- 示例：
  ```sql
  UNIQUE(user_id, role_id)
  ```