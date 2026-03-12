# Loafer Blog Bug 修复 v2 - 实施计划

## [x] 任务 1: 还原UserSettingsView.vue
- **优先级**: P0
- **依赖**: 无
- **描述**:
  - 重新创建UserSettingsView.vue文件
  - 确保其包含邮箱修改等功能
- **相关验收标准**: AC-1
- **测试要求**:
  - `human-judgment` TR-1.1: 验证UserSettingsView.vue能正常显示
  - `human-judgment` TR-1.2: 验证UserSettingsView.vue包含邮箱修改功能
- **备注**: 需要根据之前的代码重新创建UserSettingsView.vue文件

## [x] 任务 2: 实现文件大小限制的全局缓存
- **优先级**: P0
- **依赖**: 无
- **描述**:
  - 实现全局缓存机制，存储文件大小限制配置
  - 修改FileSizeLimitFilter，从全局缓存读取配置
  - 修改FileLimitController，在更新配置时清空缓存
- **相关验收标准**: AC-2
- **测试要求**:
  - `programmatic` TR-2.1: 验证FileSizeLimitFilter从全局缓存读取配置
  - `programmatic` TR-2.2: 验证配置更新时缓存被清空
- **备注**: 可以使用Spring的缓存机制或自定义缓存

## [x] 任务 3: 修复聊天消息渲染问题
- **优先级**: P0
- **依赖**: 无
- **描述**:
  - 检查聊天消息的数据结构
  - 修复前端渲染逻辑
  - 确保消息内容和发送时间能正确显示
- **相关验收标准**: AC-3
- **测试要求**:
  - `human-judgment` TR-3.1: 验证聊天消息能正确渲染
  - `human-judgment` TR-3.2: 验证消息内容和发送时间能正确显示
- **备注**: 需要检查MessageDetailView.vue和messageStore

## [x] 任务 4: 改进文件类型判断逻辑
- **优先级**: P0
- **依赖**: 任务 2
- **描述**:
  - 修改FileSizeLimitFilter.getMaxFileSizeByType方法
  - 通过文件类型而不是URI判断
  - 确保能正确识别不同类型的文件
- **相关验收标准**: AC-4
- **测试要求**:
  - `programmatic` TR-4.1: 验证FileSizeLimitFilter能根据文件类型使用正确的大小限制
  - `programmatic` TR-4.2: 验证能正确识别不同类型的文件
- **备注**: 需要从请求中解析文件类型
