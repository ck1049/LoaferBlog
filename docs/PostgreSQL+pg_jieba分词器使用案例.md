# **PostgreSQL 17 + pg_jieba 分词器使用案例**

## 一、 加载插件

-- 加载插件（需超级用户权限）
CREATE EXTENSION IF NOT EXISTS pg_jieba;

## 二、 核心概念说明

1. 关键组件
jieba 分词函数：jieba_tokenize（基础分词）、jieba_parser（全文检索专用分词器）
全文检索类型：PostgreSQL 原生 tsvector（分词向量）、tsquery（检索查询）
自定义词典：pg_jieba 支持自定义词库，解决专业词汇分词不准确问题
2. 核心逻辑
将中文文本通过 jieba 分词器拆分为关键词（生成 tsvector）
将用户检索关键词同样分词（生成 tsquery）
通过 PostgreSQL 全文检索算子（@@）匹配 tsvector 和 tsquery

### 三、 完整实例：实现文章中文全文检索

1. 创建测试表
创建一个文章表，包含标题、内容字段，用于存储需要检索的中文内容：
```sql
-- 创建文章表
CREATE TABLE IF NOT EXISTS articles (
    id SERIAL PRIMARY KEY,          -- 文章ID（自增主键）
    title VARCHAR(200) NOT NULL,    -- 文章标题
    content TEXT NOT NULL,          -- 文章内容
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    -- 生成列：自动分词 title + content（可选，提升检索效率）
    ts_vector_col tsvector  GENERATED ALWAYS AS (
        to_tsvector('jiebacfg', title || ' ' || content)
    ) STORED
);

-- 为分词向量创建索引（核心：提升检索速度）
CREATE INDEX idx_articles_ts_vector ON articles USING gin(ts_vector_col);
```
2. 插入测试数据
插入包含中文的测试文章，并自动生成分词向量：
```sql
-- 插入数据（同时生成 ts_vector 分词向量）
-- 插入数据（同时生成 ts_vector 分词向量）
INSERT INTO articles (title, content)
VALUES
(
    'PostgreSQL 集成 jieba 分词实现中文检索',
    'PostgreSQL 是一款强大的开源关系型数据库，集成 jieba 分词器后可以高效实现中文全文检索，解决原生分词对中文支持差的问题。'
),
(
    'Python 与 jieba 分词的使用',
    'jieba 是 Python 中最常用的中文分词库，支持精确模式、全模式和搜索引擎模式，可用于文本处理和检索场景。'
),
(
    'PostgreSQL 性能优化技巧',
    '优化 PostgreSQL 索引和查询语句可以显著提升数据库性能，全文检索的 gin 索引是提升检索速度的关键。'
);

-- 查询验证
SELECT id, title, ts_vector_col FROM articles;
```
3. 实现中文全文检索
```sql
-- 方式1：直接使用分词器检索（实时分词）
SELECT 
    id, title, content 
FROM 
    articles 
WHERE 
    to_tsvector('jiebacfg', title || ' ' || content) @@ to_tsquery('jiebacfg', 'PostgreSQL & 中文检索');

-- 方式2：使用预生成的 ts_vector_col（效率更高，推荐）
SELECT 
    id, title, content 
FROM 
    articles 
WHERE 
    ts_vector_col @@ to_tsquery('jiebacfg', 'PostgreSQL & 中文检索');

-- 模糊/或条件
SELECT 
    id, title 
FROM 
    articles 
WHERE 
    ts_vector_col @@ to_tsquery('jiebacfg', 'jieba | PostgreSQL');

-- 检索结果按照匹配度排序
SELECT
    id, title,
    ts_rank(ts_vector_col, to_tsquery('jiebacfg', 'PostgreSQL | 分词')) AS rank,
    -- 标题权重为 A，内容权重为 B
    ts_rank(
        setweight(to_tsvector('jiebacfg', title), 'A')
        || setweight(to_tsvector('jiebacfg', content), 'B'),
        to_tsquery('jiebacfg', 'PostgreSQL | 分词')
    ) AS rank_wight
FROM
    articles
WHERE ts_vector_col @@ to_tsquery('jiebacfg', 'PostgreSQL | 分词')
ORDER BY rank_wight DESC, rank DESC;

-- 检索结果高亮
SELECT
    id,
    -- 高亮标题：指定 jieba 分词配置
    ts_headline('jiebacfg', title, to_tsquery('jiebacfg', 'PostgreSQL | 分词')) AS highlighted_title,
    -- 高亮内容：指定 jieba 分词配置
    ts_headline('jiebacfg', content, to_tsquery('jiebacfg', 'PostgreSQL | 分词')) AS highlighted_content,
    -- 自定义选项（key=value 格式，多个用逗号分隔）
    ts_headline('jiebacfg', title, to_tsquery('jiebacfg', 'PostgreSQL | 分词'), 'StartSel="<font color=""red"">", StopSel="</font>", MaxWords=30, MinWords=10, ShortWord=3, HighlightAll=FALSE, MaxFragments=1') AS highlighted_custom_label,
    -- 匹配度排序
    ts_rank(ts_vector_col, to_tsquery('jiebacfg', 'PostgreSQL | 分词')) AS rank
FROM articles
WHERE ts_vector_col @@ to_tsquery('jiebacfg', 'PostgreSQL | 分词')
ORDER BY rank DESC;

-- 将 tsvector 转为纯词汇数组
SELECT
    tsvector_to_array(ts_vector_col) AS tokens
FROM articles;

-- 搜索文章标题或内容包含"Postgresql全文检索"(支持跨步)，按照匹配度排序(标题权重'A'，内容权重'B')，命中结果高亮，文字添加"highLight"类名
SELECT
    ts_headline(
            'jiebacfg',
            title,
            to_tsquery('jiebacfg', b.search_condition),
            'StartSel="<span class=""highLight"">",StopSel="</span>",HighlightAll=TRUE'
    ) AS highlighted_title,
    ts_headline(
            'jiebacfg',
            content,
            to_tsquery('jiebacfg', b.search_condition),
            'StartSel="<span class=""highLight"">",StopSel="</span>",HighlightAll=TRUE'
    ) AS highlighted_custom_content,
    ts_rank(
            setweight(to_tsvector('jiebacfg', title), 'A')
            || setweight(to_tsvector('jiebacfg', content), 'B'),
            to_tsquery('jiebacfg', b.search_condition)
    ) AS rank
FROM articles a,
(SELECT array_to_string(tsvector_to_array(to_tsvector('jiebacfg', 'PostgreSQL全文检索')), ' | ') AS search_condition) b
WHERE a.ts_vector_col @@ to_tsquery('jiebacfg', b.search_condition)
ORDER BY rank DESC;


-- 自定义词典
-- 更新词典前先执行一次查询
SELECT to_tsvector('jiebacfg', '咕咕嘎嘎');
|'咕咕':1 | '嘎嘎':2|

-- 修改自定义词典文件 echo "咕咕嘎嘎" > /usr/share/postgresql/17/tsearch_data/jieba_user.dict
-- 重启 PostgreSQL 服务 （网上有动态加载词典函数jieba.load_userdict(file_name) ，但是小编并没有成功）
sudo service postgresql restart

-- 再次执行一次查询
SELECT to_tsvector('jiebacfg', '咕咕嘎嘎');
'咕咕嘎嘎':1
```