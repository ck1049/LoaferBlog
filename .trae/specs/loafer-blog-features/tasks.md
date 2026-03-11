# LoaferBlog 功能增强 - 实现计划

## [x] Task 1: 实现新用户注册后管理员自动发送欢迎消息
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 修改后端注册逻辑，在用户注册成功后，自动创建一条从管理员到新用户的欢迎消息
  - 消息内容为："Hi，欢迎加入LoaferBlog。"
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: 新用户注册后，数据库中应存在一条管理员发送的欢迎消息
  - `programmatic` TR-1.2: 新用户登录后应能看到欢迎消息
- **Notes**: 需要确定管理员账户的ID或标识

## [x] Task 2: 修复管理员管理按钮bug
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 分析并修复前端路由错误，解决"Cannot read properties of undefined (reading 'config')"错误
  - 确保管理员点击"管理"按钮后能正常打开管理页面
- **Acceptance Criteria Addressed**: AC-6
- **Test Requirements**:
  - `programmatic` TR-2.1: 管理员登录后点击"管理"按钮应能正常打开管理页面
  - `programmatic` TR-2.2: 控制台无vue-router相关错误
- **Notes**: 错误发生在base-editor.js中，可能与markdown编辑器的配置有关

## [x] Task 3: 修复评论提交403错误
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 分析并修复评论提交时的403错误
  - 检查后端权限控制和前端请求头设置
- **Acceptance Criteria Addressed**: AC-7
- **Test Requirements**:
  - `programmatic` TR-3.1: 用户登录后提交评论应成功，无403错误
  - `programmatic` TR-3.2: 评论应正确存储到数据库中
- **Notes**: 可能是CSRF token或权限验证问题

## [x] Task 4: 修复分类查询403错误
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 分析并修复分类查询时的403错误
  - 检查后端权限控制和前端请求头设置
- **Acceptance Criteria Addressed**: AC-8
- **Test Requirements**:
  - `programmatic` TR-4.1: 用户访问系统时应能正常加载分类列表，无403错误
  - `programmatic` TR-4.2: 分类数据应正确返回
- **Notes**: 可能是权限验证问题

## [x] Task 5: 实现邮箱脱敏处理功能
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 在后端实现邮箱脱敏处理工具类
  - 修改登录接口，对返回的用户信息中的邮箱进行脱敏处理
  - 修改个人资料页，实现邮箱的脱敏展示
- **Acceptance Criteria Addressed**: AC-9, AC-11
- **Test Requirements**:
  - `programmatic` TR-5.1: 登录接口返回的用户信息中邮箱应为脱敏格式
  - `human-judgment` TR-5.2: 个人资料页显示的邮箱应为脱敏格式
- **Notes**: 邮箱脱敏规则：保留前两位和域名，中间用****替换

## [x] Task 6: 实现个人资料页邮箱修改功能
- **Priority**: P1
- **Depends On**: Task 5
- **Description**:
  - 在前端个人资料页添加邮箱修改功能
  - 在后端实现邮箱修改接口，校验邮箱格式，如为脱敏格式则不更新
- **Acceptance Criteria Addressed**: AC-10
- **Test Requirements**:
  - `programmatic` TR-6.1: 用户可以修改邮箱地址
  - `programmatic` TR-6.2: 输入脱敏格式邮箱时，系统应拒绝更新
  - `programmatic` TR-6.3: 输入有效邮箱时，系统应成功更新
- **Notes**: 邮箱格式校验应包括脱敏格式的检测

## [x] Task 7: 实现失效token校验功能
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 在后端实现token缓存机制，存储已退出的token
  - 修改JWT认证过滤器，增加对失效token的校验
  - 修改退出登录接口，将token加入失效缓存
- **Acceptance Criteria Addressed**: AC-12
- **Test Requirements**:
  - `programmatic` TR-7.1: 用户退出登录后，使用该token访问系统应被拒绝
  - `programmatic` TR-7.2: 系统应强制跳转到登录页
- **Notes**: 需要确定token的有效期

## [x] Task 8: 优化消息页面用户搜索功能
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 在前端消息页面添加用户搜索框
  - 实现根据用户名搜索用户列表的功能
  - 实现上滑翻页功能
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-8.1: 输入用户名后搜索应显示匹配的用户列表
  - `programmatic` TR-8.2: 上滑时应加载更多用户
  - `programmatic` TR-8.3: 搜索框为空时应显示所有用户
- **Notes**: 后端需要提供相应的用户搜索接口

## [x] Task 9: 优化消息页面用户列表展示
- **Priority**: P1
- **Depends On**: Task 8
- **Description**:
  - 修改前端消息页面，使用户列表包含用户头像、昵称(用户名)、个性签名
  - 优化排版，使页面美观
  - 实现同一组联系人只出现一个消息卡片的功能
- **Acceptance Criteria Addressed**: AC-3, AC-5
- **Test Requirements**:
  - `human-judgment` TR-9.1: 用户列表应包含头像、昵称、个性签名
  - `human-judgment` TR-9.2: 页面排版应美观
  - `programmatic` TR-9.3: 同一组联系人应只出现一个消息卡片
- **Notes**: 需要后端提供用户头像和个性签名数据

## [x] Task 10: 实现点击用户卡片打开聊天窗口功能
- **Priority**: P1
- **Depends On**: Task 9
- **Description**:
  - 修改前端消息页面，实现点击用户卡片自动打开双人聊天窗口的功能
  - 参考微信聊天功能的设计
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-10.1: 点击用户卡片应自动打开与该用户的聊天窗口
  - `human-judgment` TR-10.2: 聊天窗口设计应参考微信聊天功能
- **Notes**: 需要后端提供聊天消息的接口