# Loafer Blog 功能增强 - 实现计划

## [x] 任务 1: 更新数据库表结构，设置delete_time默认值
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 修改所有表的DDL语句，为delete_time字段设置默认值'00001-01-01 00:00:00'
  - 确保所有表都遵循此规范
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: 检查所有表的DDL语句，确认delete_time字段默认值为'00001-01-01 00:00:00'
- **Notes**: 参考SQL项目规则中的建表规范

## [x] 任务 2: 创建全局配置表
- **Priority**: P0
- **Depends On**: 任务 1
- **Description**:
  - 创建全局配置表，包含主键、模块、key、value、描述等字段
  - 添加表注释和字段注释
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-2.1: 检查表结构是否正确创建
  - `programmatic` TR-2.2: 验证表注释和字段注释是否添加
- **Notes**: 参考SQL项目规则中的建表规范

## [x] 任务 3: 实现配置管理相关类
- **Priority**: P0
- **Depends On**: 任务 2
- **Description**:
  - 创建Configuration实体类
  - 创建ConfigurationMapper接口
  - 创建ConfigurationService接口及实现类
  - 实现配置的获取和更新方法
- **Acceptance Criteria Addressed**: AC-2, AC-3
- **Test Requirements**:
  - `programmatic` TR-3.1: 验证配置实体类是否正确创建
  - `programmatic` TR-3.2: 验证配置服务是否能正常获取和更新配置
- **Notes**: 使用MyBatis Plus实现CRUD操作

## [x] 任务 4: 实现文件大小限制动态配置
- **Priority**: P1
- **Depends On**: 任务 3
- **Description**:
  - 创建FileSizeLimitDTO类
  - 修改FileLimitController，使用DTO接收和返回参数
  - 实现配置的存储和读取逻辑
  - 创建MultipartConfig类，动态设置文件上传限制
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-4.1: 验证文件大小限制配置是否能正常保存到配置表
  - `programmatic` TR-4.2: 验证系统上传限制是否能根据配置动态更新
- **Notes**: 项目启动时从配置表读取配置

## [x] 任务 5: 实现业务RSA密钥对管理
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 在application.yml中配置业务RSA密钥对存放路径
  - 创建BusinessRSAKeyManager类，管理密钥对的生成、存储和读取
  - 实现项目启动时的密钥对检查和加载逻辑
  - 将密钥对添加到全局缓存
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-5.1: 验证项目启动时是否能正确生成或读取密钥对
  - `programmatic` TR-5.2: 验证密钥对是否正确添加到全局缓存
- **Notes**: 与通信RSA密钥对分开使用

## [x] 任务 6: 修改用户注册功能，实现邮箱加密存储
- **Priority**: P1
- **Depends On**: 任务 5
- **Description**:
  - 修改RegisterDTO，使email字段为非必填
  - 修改AuthController，处理可选邮箱
  - 修改AuthServiceImpl，实现邮箱加密存储逻辑
  - 调整user表email字段长度，确保能存储密文
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `programmatic` TR-6.1: 验证注册时邮箱是否为非必填
  - `programmatic` TR-6.2: 验证邮箱是否被正确加密存储
  - `programmatic` TR-6.3: 验证user表email字段长度是否足够
- **Notes**: 使用业务RSA公钥加密邮箱

## [x] 任务 7: 实现个人资料页面邮箱回显与修改功能
- **Priority**: P1
- **Depends On**: 任务 5, 任务 6
- **Description**:
  - 修改UserServiceImpl，实现邮箱解密和脱敏逻辑
  - 修改个人资料相关接口，支持邮箱的回显与修改
  - 实现邮箱修改时的加密存储逻辑
- **Acceptance Criteria Addressed**: AC-6
- **Test Requirements**:
  - `programmatic` TR-7.1: 验证个人资料页面是否能正确显示脱敏邮箱
  - `programmatic` TR-7.2: 验证邮箱修改是否能正确处理
- **Notes**: 前端传参时需要判断邮箱是否为脱敏格式

## [x] 任务 8: 修复发送消息时sender_id为空的错误
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 修改MessageController，在发送消息时设置sender_id为当前用户ID
- **Acceptance Criteria Addressed**: AC-7
- **Test Requirements**:
  - `programmatic` TR-8.1: 验证发送消息时是否不再出现sender_id为空的错误
  - `programmatic` TR-8.2: 验证消息发送是否成功
- **Notes**: 从当前登录用户信息中获取sender_id

## [x] 任务 9: 实现默认头像生成功能
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 创建AvatarGenerator类，生成随机颜色背景、用户名首字母的默认头像
  - 修改用户注册逻辑，为新用户生成默认头像
  - 创建ApplicationInitializer类，在项目启动时为无头像用户生成默认头像
- **Acceptance Criteria Addressed**: AC-8
- **Test Requirements**:
  - `programmatic` TR-9.1: 验证新注册用户是否能自动生成默认头像
  - `programmatic` TR-9.2: 验证项目启动时是否能为无头像用户生成默认头像
  - `human-judgment` TR-9.3: 验证生成的默认头像是否美观，字体颜色与背景色有明显区分
- **Notes**: 使用Java AWT生成头像图片

## [x] 任务 10: 实现添加朋友功能
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 创建UserSearchController，实现用户搜索功能
  - 实现分页查询，按照用户ID排序
  - 支持前端传递上一页的最大用户ID，避免深度分页问题
- **Acceptance Criteria Addressed**: AC-9
- **Test Requirements**:
  - `programmatic` TR-10.1: 验证用户搜索功能是否正常
  - `programmatic` TR-10.2: 验证分页查询是否正常，支持加载更多
  - `programmatic` TR-10.3: 验证是否使用了用户ID大于参数的查询条件，避免深度分页问题
- **Notes**: 前端需要实现滑动到底部显示加载更多按钮的功能