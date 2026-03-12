# Loafer Blog 功能修复 - 实现计划

## [x] 任务 1: 修复文件大小限制配置
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 扩展配置表，添加三个文件大小限制配置项：图片文件限制、视频文件限制、其他文件限制
  - 修改FileSizeLimitDTO类，支持三个配置项
  - 修改FileLimitController，处理三个配置项的读写
  - 修改MultipartConfig类，从数据库中读取配置
  - 实现动态更新容器文件解析配置的逻辑
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: 验证配置表是否包含三个文件大小限制配置项
  - `programmatic` TR-1.2: 验证FileLimitController是否能正确处理三个配置项
  - `programmatic` TR-1.3: 验证MultipartConfig是否从数据库读取配置
  - `programmatic` TR-1.4: 验证修改配置后系统上传限制是否更新
- **Notes**: 需要考虑配置更新后如何动态调整容器的文件上传限制

## [x] 任务 2: 修复邮箱注册功能
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 调整数据库user表的email字段长度，确保能存储加密后的密文
  - 修改前端注册表单，取消邮箱的必填限制
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-2.1: 验证user表email字段长度是否足够
  - `human-judgment` TR-2.2: 验证前端注册表单是否取消邮箱必填限制
- **Notes**: 加密后的邮箱密文长度可能超过500字符，需要调整字段长度

## [x] 任务 3: 实现个人资料页面邮箱回显功能
- **Priority**: P1
- **Depends On**: 任务 2
- **Description**:
  - 修改UserServiceImpl，确保在返回用户信息时解密并脱敏邮箱
  - 修改前端个人资料页面，添加邮箱显示
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-3.1: 验证UserServiceImpl是否正确解密并脱敏邮箱
  - `human-judgment` TR-3.2: 验证个人资料页面是否显示脱敏邮箱
- **Notes**: 确保邮箱脱敏格式正确

## [x] 任务 4: 修复联系人列表的头像和名称显示
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 修改MessageService，确保联系人列表包含正确的头像和名称信息
  - 优先显示用户的昵称，昵称为空则显示用户名
  - 修复管理员用户显示为"未知用户"的问题
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-4.1: 验证MessageService返回的联系人信息是否包含正确的头像和名称
  - `human-judgment` TR-4.2: 验证联系人列表是否正确显示头像和名称
- **Notes**: 确保管理员用户也能正确显示名称

## [x] 任务 5: 为新注册用户生成默认头像
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 修改AuthServiceImpl，在用户注册时生成默认头像
  - 确保生成的头像保存到正确的目录并更新user表
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `programmatic` TR-5.1: 验证新注册用户是否生成默认头像
  - `programmatic` TR-5.2: 验证user表是否更新了头像信息
- **Notes**: 使用AvatarGenerator工具类生成默认头像

## [x] 任务 6: 修复消息系统问题
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 修复消息相关接口的返回参数，避免使用Map作为返回参数
  - 修复LocalDateTime字段强转Date字段的错误
  - 添加朋友功能入口
- **Acceptance Criteria Addressed**: AC-6
- **Test Requirements**:
  - `programmatic` TR-6.1: 验证消息接口返回参数是否正确
  - `programmatic` TR-6.2: 验证日期处理是否正确
  - `human-judgment` TR-6.3: 验证添加朋友功能是否可访问
- **Notes**: 确保消息显示的日期格式正确