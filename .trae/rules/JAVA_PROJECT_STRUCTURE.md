---
alwaysApply: false
globs: 
  - '**/backend/**'
---
# Java 工程结构规范

## 1. 目录结构
```
trae-project/
├── src/
│   ├── main/
│   │   ├── java/com/trae/project/
│   │   │   ├── common/      # 公共模块
│   │   │   ├── config/      # 配置类
│   │   │   ├── controller/  # 控制层
│   │   │   ├── service/     # 服务层
│   │   │   │   └── impl/    # 服务实现
│   │   │   ├── mapper/      # 数据访问层
│   │   │   ├── model/       # 数据模型
│   │   │   │   ├── entity/  # 数据库实体
│   │   │   │   ├── dto/     # 数据传输对象
│   │   │   │   └── vo/      # 视图对象
│   │   │   └── exception/   # 自定义异常
│   │   └── resources/
│   │       ├── application.yml  # 主配置文件
│   │       ├── application-dev.yml
│   │       ├── mapper/          # MyBatis映射文件
│   │       └── static/          # 静态资源
│   └── test/                    # 测试代码
└── pom.xml                      # Maven依赖
```

## 2. 模块职责
- **common**：公共常量、工具类、枚举等
- **config**：Spring Boot配置类
- **controller**：处理HTTP请求，实现RESTful API
- **service**：实现业务逻辑，处理事务管理
- **mapper**：数据访问层，定义数据库操作方法
- **model**：存放数据模型（entity、dto、vo）
- **exception**：定义自定义异常类