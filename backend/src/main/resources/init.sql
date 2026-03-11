-- PostgreSQL数据库初始化脚本
-- 版本: 1.0
-- 日期: 2026-03-07

-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS post_view_history;
DROP TABLE IF EXISTS post_like;
DROP TABLE IF EXISTS post_favorite;
DROP TABLE IF EXISTS post_tag;
DROP TABLE IF EXISTS post_category;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS announcement;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS sensitive_word;

-- 创建角色表
CREATE TABLE IF NOT EXISTS role (
    id SERIAL PRIMARY KEY,              -- 角色ID
    name VARCHAR(50) NOT NULL UNIQUE,   -- 角色名称
    description VARCHAR(255),           -- 角色描述
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted INTEGER DEFAULT 0           -- 逻辑删除标记：0-未删除，1-已删除
);

-- 表注释
COMMENT ON TABLE role IS '角色表';
COMMENT ON COLUMN role.id IS '角色ID';
COMMENT ON COLUMN role.name IS '角色名称';
COMMENT ON COLUMN role.description IS '角色描述';
COMMENT ON COLUMN role.create_time IS '创建时间';
COMMENT ON COLUMN role.update_time IS '更新时间';
COMMENT ON COLUMN role.deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 创建用户表
CREATE TABLE IF NOT EXISTS "user" (
    id SERIAL PRIMARY KEY,              -- 用户ID
    username VARCHAR(50) NOT NULL UNIQUE, -- 用户名
    password VARCHAR(100) NOT NULL,     -- 密码（加密存储）
    nickname VARCHAR(50),               -- 昵称
    email VARCHAR(100),                 -- 邮箱
    avatar VARCHAR(255),                -- 头像URL
    bio TEXT,                           -- 个性签名
    status INTEGER DEFAULT 1,           -- 状态：1-正常，0-禁用
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted INTEGER DEFAULT 0           -- 逻辑删除标记：0-未删除，1-已删除
);

-- 表注释
COMMENT ON TABLE "user" IS '用户表';
COMMENT ON COLUMN "user".id IS '用户ID';
COMMENT ON COLUMN "user".username IS '用户名';
COMMENT ON COLUMN "user".password IS '密码（加密存储）';
COMMENT ON COLUMN "user".nickname IS '昵称';
COMMENT ON COLUMN "user".email IS '邮箱';
COMMENT ON COLUMN "user".avatar IS '头像URL';
COMMENT ON COLUMN "user".bio IS '个性签名';
COMMENT ON COLUMN "user".status IS '状态：1-正常，0-禁用';
COMMENT ON COLUMN "user".create_time IS '创建时间';
COMMENT ON COLUMN "user".update_time IS '更新时间';
COMMENT ON COLUMN "user".deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 创建用户角色关联表
CREATE TABLE IF NOT EXISTS user_role (
    id SERIAL PRIMARY KEY,              -- 关联ID
    user_id INTEGER NOT NULL,            -- 用户ID
    role_id INTEGER NOT NULL,            -- 角色ID
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    deleted INTEGER DEFAULT 0,           -- 逻辑删除标记：0-未删除，1-已删除
    UNIQUE(user_id, role_id)            -- 唯一约束：用户与角色的组合
);

-- 表注释
COMMENT ON TABLE user_role IS '用户角色关联表';
COMMENT ON COLUMN user_role.id IS '关联ID';
COMMENT ON COLUMN user_role.user_id IS '用户ID';
COMMENT ON COLUMN user_role.role_id IS '角色ID';
COMMENT ON COLUMN user_role.create_time IS '创建时间';
COMMENT ON COLUMN user_role.deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 创建公告表
CREATE TABLE IF NOT EXISTS announcement (
    id SERIAL PRIMARY KEY,              -- 公告ID
    title VARCHAR(255) NOT NULL,        -- 公告标题
    content TEXT NOT NULL,              -- 公告内容
    author_id INTEGER NOT NULL,          -- 作者ID
    status INTEGER DEFAULT 1,           -- 状态：1-发布，0-草稿
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted INTEGER DEFAULT 0           -- 逻辑删除标记：0-未删除，1-已删除
);

-- 表注释
COMMENT ON TABLE announcement IS '公告表';
COMMENT ON COLUMN announcement.id IS '公告ID';
COMMENT ON COLUMN announcement.title IS '公告标题';
COMMENT ON COLUMN announcement.content IS '公告内容';
COMMENT ON COLUMN announcement.author_id IS '作者ID';
COMMENT ON COLUMN announcement.status IS '状态：1-发布，0-草稿';
COMMENT ON COLUMN announcement.create_time IS '创建时间';
COMMENT ON COLUMN announcement.update_time IS '更新时间';
COMMENT ON COLUMN announcement.deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 创建分类表
CREATE TABLE IF NOT EXISTS category (
    id SERIAL PRIMARY KEY,              -- 分类ID
    name VARCHAR(50) NOT NULL UNIQUE,   -- 分类名称
    description VARCHAR(255),           -- 分类描述
    status INTEGER DEFAULT 1,           -- 状态：1-启用，0-禁用
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted INTEGER DEFAULT 0           -- 逻辑删除标记：0-未删除，1-已删除
);

-- 表注释
COMMENT ON TABLE category IS '分类表';
COMMENT ON COLUMN category.id IS '分类ID';
COMMENT ON COLUMN category.name IS '分类名称';
COMMENT ON COLUMN category.description IS '分类描述';
COMMENT ON COLUMN category.status IS '状态：1-启用，0-禁用';
COMMENT ON COLUMN category.create_time IS '创建时间';
COMMENT ON COLUMN category.update_time IS '更新时间';
COMMENT ON COLUMN category.deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 创建标签表
CREATE TABLE IF NOT EXISTS tag (
    id SERIAL PRIMARY KEY,              -- 标签ID
    name VARCHAR(50) NOT NULL UNIQUE,   -- 标签名称
    description VARCHAR(255),           -- 标签描述
    status INTEGER DEFAULT 1,           -- 状态：1-启用，0-禁用
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted INTEGER DEFAULT 0           -- 逻辑删除标记：0-未删除，1-已删除
);

-- 表注释
COMMENT ON TABLE tag IS '标签表';
COMMENT ON COLUMN tag.id IS '标签ID';
COMMENT ON COLUMN tag.name IS '标签名称';
COMMENT ON COLUMN tag.description IS '标签描述';
COMMENT ON COLUMN tag.status IS '状态：1-启用，0-禁用';
COMMENT ON COLUMN tag.create_time IS '创建时间';
COMMENT ON COLUMN tag.update_time IS '更新时间';
COMMENT ON COLUMN tag.deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 创建技术贴表
CREATE TABLE IF NOT EXISTS post (
    id SERIAL PRIMARY KEY,              -- 帖子ID
    title VARCHAR(255) NOT NULL,        -- 帖子标题
    content TEXT NOT NULL,              -- 帖子内容
    author_id INTEGER NOT NULL,          -- 作者ID
    summary VARCHAR(255),               -- 帖子摘要
    view_count INTEGER DEFAULT 0,       -- 浏览次数
    comment_count INTEGER DEFAULT 0,    -- 评论次数
    like_count INTEGER DEFAULT 0,       -- 点赞次数
    status INTEGER DEFAULT 1,           -- 状态：1-发布，0-草稿
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted INTEGER DEFAULT 0           -- 逻辑删除标记：0-未删除，1-已删除
);

-- 表注释
COMMENT ON TABLE post IS '技术贴表';
COMMENT ON COLUMN post.id IS '帖子ID';
COMMENT ON COLUMN post.title IS '帖子标题';
COMMENT ON COLUMN post.content IS '帖子内容';
COMMENT ON COLUMN post.author_id IS '作者ID';
COMMENT ON COLUMN post.summary IS '帖子摘要';
COMMENT ON COLUMN post.view_count IS '浏览次数';
COMMENT ON COLUMN post.comment_count IS '评论次数';
COMMENT ON COLUMN post.like_count IS '点赞次数';
COMMENT ON COLUMN post.status IS '状态：1-发布，0-草稿';
COMMENT ON COLUMN post.create_time IS '创建时间';
COMMENT ON COLUMN post.update_time IS '更新时间';
COMMENT ON COLUMN post.deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 创建帖子分类关联表
CREATE TABLE IF NOT EXISTS post_category (
    id SERIAL PRIMARY KEY,              -- 关联ID
    post_id INTEGER NOT NULL,            -- 帖子ID
    category_id INTEGER NOT NULL,        -- 分类ID
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    deleted INTEGER DEFAULT 0,           -- 逻辑删除标记：0-未删除，1-已删除
    UNIQUE(post_id, category_id)        -- 唯一约束：帖子与分类的组合
);

-- 表注释
COMMENT ON TABLE post_category IS '帖子分类关联表';
COMMENT ON COLUMN post_category.id IS '关联ID';
COMMENT ON COLUMN post_category.post_id IS '帖子ID';
COMMENT ON COLUMN post_category.category_id IS '分类ID';
COMMENT ON COLUMN post_category.create_time IS '创建时间';
COMMENT ON COLUMN post_category.deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 创建帖子标签关联表
CREATE TABLE IF NOT EXISTS post_tag (
    id SERIAL PRIMARY KEY,              -- 关联ID
    post_id INTEGER NOT NULL,            -- 帖子ID
    tag_id INTEGER NOT NULL,             -- 标签ID
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    deleted INTEGER DEFAULT 0,           -- 逻辑删除标记：0-未删除，1-已删除
    UNIQUE(post_id, tag_id)             -- 唯一约束：帖子与标签的组合
);

-- 表注释
COMMENT ON TABLE post_tag IS '帖子标签关联表';
COMMENT ON COLUMN post_tag.id IS '关联ID';
COMMENT ON COLUMN post_tag.post_id IS '帖子ID';
COMMENT ON COLUMN post_tag.tag_id IS '标签ID';
COMMENT ON COLUMN post_tag.create_time IS '创建时间';
COMMENT ON COLUMN post_tag.deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 创建评论表
CREATE TABLE IF NOT EXISTS comment (
    id SERIAL PRIMARY KEY,              -- 评论ID
    post_id INTEGER NOT NULL,            -- 帖子ID
    user_id INTEGER NOT NULL,            -- 用户ID
    parent_id INTEGER,                   -- 父评论ID（用于回复）
    content TEXT NOT NULL,              -- 过滤后的评论内容
    original_content TEXT,              -- 原始评论内容
    status INTEGER DEFAULT 1,           -- 状态：1-正常，0-禁用
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted INTEGER DEFAULT 0           -- 逻辑删除标记：0-未删除，1-已删除
);

-- 表注释
COMMENT ON TABLE comment IS '评论表';
COMMENT ON COLUMN comment.id IS '评论ID';
COMMENT ON COLUMN comment.post_id IS '帖子ID';
COMMENT ON COLUMN comment.user_id IS '用户ID';
COMMENT ON COLUMN comment.parent_id IS '父评论ID（用于回复）';
COMMENT ON COLUMN comment.content IS '过滤后的评论内容';
COMMENT ON COLUMN comment.original_content IS '原始评论内容';
COMMENT ON COLUMN comment.status IS '状态：1-正常，0-禁用';
COMMENT ON COLUMN comment.create_time IS '创建时间';
COMMENT ON COLUMN comment.update_time IS '更新时间';
COMMENT ON COLUMN comment.deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 创建消息表
CREATE TABLE IF NOT EXISTS message (
    id SERIAL PRIMARY KEY,              -- 消息ID
    sender_id INTEGER NOT NULL,          -- 发送者ID
    receiver_id INTEGER NOT NULL,        -- 接收者ID
    content TEXT NOT NULL,              -- 过滤后的消息内容
    original_content TEXT,              -- 原始消息内容
    message_type INTEGER DEFAULT 1,      -- 消息类型：1-文本, 2-表情, 3-图片, 4-视频, 5-文件
    file_path TEXT,                     -- 存储文件路径
    file_name TEXT,                     -- 存储文件名
    file_size BIGINT,                   -- 存储文件大小
    send_status INTEGER DEFAULT 1,      -- 发送状态：1-发送成功, 0-发送失败
    error_message TEXT,                 -- 发送失败原因
    is_top INTEGER DEFAULT 0,           -- 是否置顶：1-置顶, 0-普通
    status INTEGER DEFAULT 1,           -- 状态：1-正常，0-禁用
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted INTEGER DEFAULT 0           -- 逻辑删除标记：0-未删除，1-已删除
);

-- 表注释
COMMENT ON TABLE message IS '消息表';
COMMENT ON COLUMN message.id IS '消息ID';
COMMENT ON COLUMN message.sender_id IS '发送者ID';
COMMENT ON COLUMN message.receiver_id IS '接收者ID';
COMMENT ON COLUMN message.content IS '过滤后的消息内容';
COMMENT ON COLUMN message.original_content IS '原始消息内容';
COMMENT ON COLUMN message.message_type IS '消息类型：1-文本, 2-表情, 3-图片, 4-视频, 5-文件';
COMMENT ON COLUMN message.file_path IS '存储文件路径';
COMMENT ON COLUMN message.file_name IS '存储文件名';
COMMENT ON COLUMN message.file_size IS '存储文件大小';
COMMENT ON COLUMN message.send_status IS '发送状态：1-发送成功, 0-发送失败';
COMMENT ON COLUMN message.error_message IS '发送失败原因';
COMMENT ON COLUMN message.is_top IS '是否置顶：1-置顶, 0-普通';
COMMENT ON COLUMN message.status IS '状态：1-正常，0-禁用';
COMMENT ON COLUMN message.create_time IS '创建时间';
COMMENT ON COLUMN message.update_time IS '更新时间';
COMMENT ON COLUMN message.deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 创建帖子点赞表
CREATE TABLE IF NOT EXISTS post_like (
    id SERIAL PRIMARY KEY,              -- 点赞ID
    post_id INTEGER NOT NULL,            -- 帖子ID
    user_id INTEGER NOT NULL,            -- 用户ID
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    deleted INTEGER DEFAULT 0,           -- 逻辑删除标记：0-未删除，1-已删除
    UNIQUE(post_id, user_id)            -- 唯一约束：一个用户对一个帖子只能点赞一次
);

-- 表注释
COMMENT ON TABLE post_like IS '帖子点赞表';
COMMENT ON COLUMN post_like.id IS '点赞ID';
COMMENT ON COLUMN post_like.post_id IS '帖子ID';
COMMENT ON COLUMN post_like.user_id IS '用户ID';
COMMENT ON COLUMN post_like.create_time IS '创建时间';
COMMENT ON COLUMN post_like.deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 创建帖子收藏表
CREATE TABLE IF NOT EXISTS post_favorite (
    id SERIAL PRIMARY KEY,              -- 收藏ID
    post_id INTEGER NOT NULL,            -- 帖子ID
    user_id INTEGER NOT NULL,            -- 用户ID
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    deleted INTEGER DEFAULT 0,           -- 逻辑删除标记：0-未删除，1-已删除
    UNIQUE(post_id, user_id)            -- 唯一约束：一个用户对一个帖子只能收藏一次
);

-- 表注释
COMMENT ON TABLE post_favorite IS '帖子收藏表';
COMMENT ON COLUMN post_favorite.id IS '收藏ID';
COMMENT ON COLUMN post_favorite.post_id IS '帖子ID';
COMMENT ON COLUMN post_favorite.user_id IS '用户ID';
COMMENT ON COLUMN post_favorite.create_time IS '创建时间';
COMMENT ON COLUMN post_favorite.deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 创建帖子浏览历史表
CREATE TABLE IF NOT EXISTS post_view_history (
    id SERIAL PRIMARY KEY,              -- 历史ID
    post_id INTEGER NOT NULL,            -- 帖子ID
    user_id INTEGER NOT NULL,            -- 用户ID
    viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 浏览时间
    deleted INTEGER DEFAULT 0,           -- 逻辑删除标记：0-未删除，1-已删除
    UNIQUE(post_id, user_id)            -- 唯一约束：一个用户对一个帖子只记录一次浏览
);

-- 表注释
COMMENT ON TABLE post_view_history IS '帖子浏览历史表';
COMMENT ON COLUMN post_view_history.id IS '历史ID';
COMMENT ON COLUMN post_view_history.post_id IS '帖子ID';
COMMENT ON COLUMN post_view_history.user_id IS '用户ID';
COMMENT ON COLUMN post_view_history.viewed_at IS '浏览时间';
COMMENT ON COLUMN post_view_history.deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 创建敏感词表
CREATE TABLE IF NOT EXISTS sensitive_word (
    id SERIAL PRIMARY KEY,              -- 敏感词ID
    word VARCHAR(50) NOT NULL UNIQUE,   -- 敏感词
    status INTEGER DEFAULT 1,           -- 状态：1-启用，0-禁用
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted INTEGER DEFAULT 0           -- 逻辑删除标记：0-未删除，1-已删除
);

-- 表注释
COMMENT ON TABLE sensitive_word IS '敏感词表';
COMMENT ON COLUMN sensitive_word.id IS '敏感词ID';
COMMENT ON COLUMN sensitive_word.word IS '敏感词';
COMMENT ON COLUMN sensitive_word.status IS '状态：1-启用，0-禁用';
COMMENT ON COLUMN sensitive_word.create_time IS '创建时间';
COMMENT ON COLUMN sensitive_word.update_time IS '更新时间';
COMMENT ON COLUMN sensitive_word.deleted IS '逻辑删除标记：0-未删除，1-已删除';

-- 插入初始数据

-- 插入角色数据
INSERT INTO role (name, description) VALUES
('admin', '管理员'),
('user', '普通用户'),
('guest', '游客');

-- 插入管理员用户（密码：admin123，已加密）
INSERT INTO "user" (username, password, nickname, email, status) VALUES
('admin', '$2a$10$S3KxRABwzmaePrPwJjYMKuU92zhuxeMOdHO8VU5lvMV3iW.3UUvQC', '管理员', 'admin@example.com', 1);

-- 插入普通用户（密码：user123，已加密）
INSERT INTO "user" (username, password, nickname, email, status) VALUES
('user', '$2a$10$AnaUbNUfR9X8k0f8V7gUPeJSaxkehkwefgwdpbTs6S9lnrnAidjf6', '普通用户', 'user@example.com', 1);

-- 关联用户角色
INSERT INTO user_role (user_id, role_id) VALUES
(1, 1), -- 管理员关联管理员角色
(2, 2); -- 普通用户关联普通用户角色

-- 插入初始分类
INSERT INTO category (name, description) VALUES
('前端', '前端开发相关'),
('后端', '后端开发相关'),
('AI应用', '人工智能应用'),
('Java', 'Java开发'),
('Python', 'Python开发'),
('C#', 'C#开发'),
('TypeScript', 'TypeScript开发');

-- 插入初始标签
INSERT INTO tag (name, description) VALUES
('AI', '人工智能'),
('Spring', 'Spring框架'),
('微服务', '微服务架构'),
('Vue', 'Vue框架'),
('React', 'React框架'),
('数据库', '数据库技术'),
('算法', '算法与数据结构');

-- 插入初始敏感词
INSERT INTO sensitive_word (word) VALUES
('敏感词1'),
('敏感词2'),
('敏感词3');

-- 插入初始公告
INSERT INTO announcement (title, content, author_id, status) VALUES
('欢迎使用技术博客', '欢迎各位开发者使用本技术博客平台，在这里你可以分享技术心得，交流开发经验。', 1, 1),
('平台更新通知', '平台已完成最新版本更新，新增了敏感词过滤功能和消息系统。', 1, 1);

-- 插入初始技术贴
INSERT INTO post (title, content, author_id, summary, status) VALUES
('Spring Boot 2.0 新特性', 'Spring Boot 2.0 带来了许多新特性，包括性能提升、新的依赖管理方式等。', 1, 'Spring Boot 2.0 新特性介绍', 1),
('Vue 3 Composition API 详解', 'Vue 3 的 Composition API 提供了更灵活的代码组织方式，本文将详细介绍其使用方法。', 1, 'Vue 3 Composition API 详解', 1);

-- 关联帖子分类
INSERT INTO post_category (post_id, category_id) VALUES
(1, 2), -- Spring Boot 属于后端
(1, 4), -- Spring Boot 属于Java
(2, 1), -- Vue 属于前端
(2, 7); -- Vue 属于TypeScript

-- 关联帖子标签
INSERT INTO post_tag (post_id, tag_id) VALUES
(1, 2), -- Spring Boot 关联Spring标签
(1, 3), -- Spring Boot 关联微服务标签
(2, 4), -- Vue 关联Vue标签
(2, 5); -- Vue 关联React标签（示例）

-- 提交事务
COMMIT;