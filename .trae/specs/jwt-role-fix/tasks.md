# JWT 角色认证安全修复 - 实现计划

## [ ] Task 1: 修改 JwtUtils 类，支持生成包含角色列表的 JWT
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 修改 JwtUtils 类，添加生成包含角色列表的 JWT 的方法
  - 确保角色列表正确写入 JWT payload
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: 验证生成的 JWT 包含角色列表
- **Notes**: 需要添加新的 generateToken 方法，支持传入角色列表

## [ ] Task 2: 修改 AuthServiceImpl 类，使用新的 JWT 生成方法
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 修改 AuthServiceImpl 类的 login 方法，使用新的 JWT 生成方法
  - 确保生成 JWT 时传入用户的角色列表
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-2.1: 验证登录时生成的 JWT 包含角色列表
- **Notes**: 需要确保角色列表格式正确

## [ ] Task 3: 修改 JwtUtils 类，添加从 JWT 中提取角色列表的方法
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 修改 JwtUtils 类，添加从 JWT payload 中提取角色列表的方法
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-3.1: 验证能从 JWT 中提取角色列表
- **Notes**: 需要处理角色列表可能不存在的情况

## [ ] Task 4: 修改 JwtAuthenticationFilter 类，从 JWT 中提取角色列表
- **Priority**: P0
- **Depends On**: Task 3
- **Description**: 
  - 修改 JwtAuthenticationFilter 类，从 JWT payload 中提取用户 ID 和角色列表
  - 不再硬编码角色列表
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-4.1: 验证 JwtAuthenticationFilter 能从 JWT 中提取角色列表
  - `programmatic` TR-4.2: 验证不再硬编码角色列表
- **Notes**: 需要确保角色列表正确转换为 authorities

## [ ] Task 5: 测试角色权限验证
- **Priority**: P1
- **Depends On**: Task 4
- **Description**: 
  - 测试不同角色的用户访问权限
  - 确保用户只能访问其角色允许的资源
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `human-judgment` TR-5.1: 验证普通用户不能访问管理员资源
  - `human-judgment` TR-5.2: 验证管理员能访问所有资源
- **Notes**: 需要测试不同角色的权限

## [ ] Task 6: 整体测试和优化
- **Priority**: P1
- **Depends On**: Task 5
- **Description**: 
  - 测试整个认证流程
  - 确保修复后的认证系统正常工作
  - 优化性能和安全性
- **Acceptance Criteria Addressed**: AC-1, AC-2, AC-3
- **Test Requirements**:
  - `human-judgment` TR-6.1: 验证整个认证流程正常
  - `human-judgment` TR-6.2: 验证系统安全性得到提升
- **Notes**: 需要在不同场景下测试