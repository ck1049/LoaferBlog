# LoaferBlog 技术博客系统

## 项目介绍

LoaferBlog是一个功能完整的技术博客系统，采用前后端分离架构，提供了用户认证、文章管理、消息系统、好友系统等核心功能。系统设计注重用户体验和安全性，支持响应式布局，适配不同设备。

### 代码完成情况
- 95% 由 TraeCN 完成
- 1% 由 Cursor 完成
- 1% 手写代码
- 3% 由其他 AI 工具完成

## 功能特性

### 用户系统
- **用户注册与登录**：支持邮箱验证和密码加密
- **个人资料管理**：修改昵称、头像、个人简介
- **角色权限控制**：管理员和普通用户权限分离

### 文章系统
- **文章发布与编辑**：支持Markdown编辑器
- **分类与标签管理**：文章分类和标签系统
- **文章搜索**：支持关键词搜索
- **评论系统**：支持文章评论
- **文章互动**：点赞、收藏、浏览历史

### 消息系统
- **好友消息**：支持文本、图片、视频、文件消息
- **未读消息提醒**：红点显示未读消息数，位置优化至头像右上角
- **消息历史记录**：完整的消息历史
- **消息状态**：支持消息已读/未读状态管理

### 好友系统
- **添加好友**：搜索用户并发送好友请求
- **好友请求管理**：接受或拒绝好友请求
- **好友列表**：查看所有好友
- **好友请求通知**：点击导航栏按钮时触发好友请求检查
- **请求状态提示**：发送好友请求后显示等待验证提示

### 管理员系统
- **用户管理**：管理用户账号
- **文章管理**：审核和管理文章
- **分类管理**：管理文章分类
- **标签管理**：管理文章标签
- **敏感词管理**：管理敏感词库（注：该词库暂时未投入使用，预计后续接入帖子评论模块）
- **文件大小限制管理**：设置不同类型文件的上传限制
- **公告管理**：发布系统公告

## 技术栈

### 前端
- **框架**：Vue 3
- **语言**：TypeScript
- **状态管理**：Pinia
- **路由**：Vue Router
- **HTTP客户端**：Axios
- **Markdown编辑器**：@kangc/v-md-editor
- **构建工具**：Vite

### 后端
- **框架**：Spring Boot 3.5.7
- **语言**：Java 21
- **ORM**：MyBatis-Plus
- **数据库**：PostgreSQL
- **安全**：Spring Security + JWT
- **加密**：RSA 非对称加密
- **文件处理**：Apache Commons IO
- **工具库**：Hutool

## 项目亮点

1. **完整的用户认证系统**：支持JWT令牌认证，密码加密存储
2. **安全防护**：RSA加密保护敏感信息，XSS攻击防护，敏感词过滤
3. **实时消息系统**：支持多种消息类型，未读消息提醒，消息已读/未读状态管理
4. **好友关系管理**：完整的好友请求和管理流程，请求状态提示
5. **响应式设计**：适配PC端和移动端，移动端布局优化
6. **丰富的管理功能**：管理员可以管理用户、文章、分类、标签、敏感词等
7. **文件上传系统**：支持多种文件类型，可配置文件大小限制
8. **全文搜索**：支持文章关键词搜索
9. **图标优化**：通讯录图标与消息图标区分，提升用户体验
10. **交互优化**：点击导航栏按钮时触发好友请求检查，提升实时性

## 环境要求

### 前端
- Node.js 16.0+
- npm 7.0+

### 后端
- Java 21
- Maven 3.6+
- PostgreSQL 15+
- Redis (可选，用于缓存)

## 部署流程

### 1. 数据库准备

1. 创建 PostgreSQL 数据库：
   ```sql
   CREATE DATABASE loafer_blog;
   ```

2. 执行初始化SQL脚本：
   - 运行 `backend/src/main/resources/init.sql` 创建表结构
   - 运行 `backend/src/main/resources/init_jieba.sql` 初始化分词器（如果使用全文搜索）

### 2. 后端部署

1. 配置 application.yml：
   - 修改数据库连接信息
   - 修改 JWT 密钥
   - 配置文件上传路径
   - 按需修改文件访问域名配置

2. 编译打包：
   ```bash
   cd backend
   mvn clean package
   ```

3. 运行应用：
   ```bash
   java -jar target/loafer-blog-0.0.1-SNAPSHOT.jar
   ```

### 3. 前端部署

1. 安装依赖：
   ```bash
   cd frontend
   npm install
   ```

2. 构建项目：
   ```bash
   npm run build
   ```

3. 部署静态文件：
   - 将 `dist` 目录下的文件部署到 Nginx 或其他静态文件服务器
   - 配置反向代理指向后端 API

### 4. 配置 Nginx（可选）

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态文件
    location / {
        root /path/to/frontend/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端 API 代理
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    # 文件上传路径
    location /uploads {
        alias /path/to/backend/uploads;
        expires 30d;
    }
}
```

**注**：文件访问除了通过 nginx 代理外，还可以通过 Java 的静态资源映射配置来实现 http 访问

## 使用说明

### 1. 管理员账号
- 默认管理员账号：admin
- 默认密码：admin123
- 登录后可进入管理后台

### 2. 普通用户
- 注册新账号
- 登录后可发布文章、添加好友、发送消息

### 3. 功能使用
- **发布文章**：登录后点击首页的发布按钮
- **添加好友**：点击导航栏的通讯录，搜索用户并发送好友请求，发送后会显示等待验证提示
- **发送消息**：点击导航栏的消息，选择好友发送消息
- **查看未读消息**：导航栏消息按钮和联系人列表会显示红点提示未读消息数
- **管理好友请求**：登录后系统会自动检查好友请求，点击相关通知处理
- **管理后台**：管理员登录后点击导航栏的管理按钮

## 项目结构

### 前端
```
frontend/
├── src/
│   ├── assets/          # 静态资源
│   ├── components/      # 组件
│   ├── constants/       # 常量
│   ├── router/          # 路由
│   ├── stores/          # 状态管理
│   ├── views/           # 页面
│   ├── App.vue          # 根组件
│   └── main.ts          # 入口文件
├── .env.development     # 开发环境配置
├── package.json         # 依赖配置
└── vite.config.ts       # Vite配置
```

### 后端
```
backend/
├── src/main/
│   ├── java/com/loafer/blog/
│   │   ├── common/      # 通用工具
│   │   ├── config/       # 配置
│   │   ├── controller/   # 控制器
│   │   ├── mapper/       # 数据访问
│   │   ├── model/        # 数据模型
│   │   ├── service/      # 业务逻辑
│   │   ├── utils/        # 工具类
│   │   └── LoaferBlogApplication.java  # 应用入口
│   └── resources/
│       ├── mapper/       # MyBatis映射文件
│       ├── application.yml  # 应用配置
│       └── init.sql      # 初始化脚本
└── pom.xml              # Maven配置
```

## 常见问题

### 1. 数据库连接失败
- 检查数据库服务是否运行
- 检查 application.yml 中的数据库连接信息是否正确
- 检查数据库用户权限

### 2. 前端无法访问后端 API
- 检查后端服务是否运行
- 检查 CORS 配置
- 检查 Nginx 反向代理配置

### 3. 文件上传失败
- 检查文件大小是否超过限制
- 检查文件上传路径权限
- 检查后端文件上传配置

## 许可证

MIT License

## 联系方式

如有问题或建议，欢迎联系项目维护者。