-- PostgreSQL数据库初始化脚本
-- 版本: 1.0
-- 日期: 2026-03-07

-- 删除已存在的表（如果存在）
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
CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建用户表
CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(100),
    avatar VARCHAR(255),
    status INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建用户角色关联表
CREATE TABLE user_role (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    role_id INTEGER NOT NULL REFERENCES role(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, role_id)
);

-- 创建公告表
CREATE TABLE announcement (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    status INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建分类表
CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    status INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建标签表
CREATE TABLE tag (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    status INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建技术贴表
CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    summary VARCHAR(255),
    view_count INTEGER DEFAULT 0,
    comment_count INTEGER DEFAULT 0,
    status INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建帖子分类关联表
CREATE TABLE post_category (
    id SERIAL PRIMARY KEY,
    post_id INTEGER NOT NULL REFERENCES post(id) ON DELETE CASCADE,
    category_id INTEGER NOT NULL REFERENCES category(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(post_id, category_id)
);

-- 创建帖子标签关联表
CREATE TABLE post_tag (
    id SERIAL PRIMARY KEY,
    post_id INTEGER NOT NULL REFERENCES post(id) ON DELETE CASCADE,
    tag_id INTEGER NOT NULL REFERENCES tag(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(post_id, tag_id)
);

-- 创建评论表
CREATE TABLE comment (
    id SERIAL PRIMARY KEY,
    post_id INTEGER NOT NULL REFERENCES post(id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    parent_id INTEGER REFERENCES comment(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    original_content TEXT,
    status INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建消息表
CREATE TABLE message (
    id SERIAL PRIMARY KEY,
    sender_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    receiver_id INTEGER NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    original_content TEXT,
    status INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建敏感词表
CREATE TABLE sensitive_word (
    id SERIAL PRIMARY KEY,
    word VARCHAR(50) NOT NULL UNIQUE,
    status INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入初始数据

-- 插入角色数据
INSERT INTO role (name, description) VALUES
('admin', '管理员'),
('user', '普通用户'),
('guest', '游客');

-- 插入管理员用户（密码：admin123，已加密）
INSERT INTO "user" (username, password, nickname, email, status) VALUES
('admin', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '管理员', 'admin@example.com', 1);

-- 插入普通用户（密码：user123，已加密）
INSERT INTO "user" (username, password, nickname, email, status) VALUES
('user', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '普通用户', 'user@example.com', 1);

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

-- 插入初始评论
INSERT INTO comment (post_id, user_id, parent_id, content, status) VALUES
(1, 2, NULL, '这篇文章写得很详细，谢谢分享！', 1),
(1, 1, 1, '不客气，很高兴对你有帮助。', 1),
(2, 2, NULL, 'Composition API 确实比 Options API 更灵活。', 1);

-- 插入初始消息
INSERT INTO message (sender_id, receiver_id, content, status) VALUES
(2, 1, '您好，我有一个关于Spring Boot的问题想请教您。', 1);

-- 提交事务
COMMIT;