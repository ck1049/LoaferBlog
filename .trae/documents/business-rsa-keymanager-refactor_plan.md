# Business RSA KeyManager 重构 - 实现计划

## \[x] 任务 1: 重构 BusinessRSAKeyManager 类

* **Priority**: P0

* **Depends On**: None

* **Description**:

  * 修改 BusinessRSAKeyManager 类，使用 RSAUtils 提供的方法来生成和处理密钥对

  * 移除重复的密钥生成和处理逻辑

  * 使用 RSAUtils 的方法来读取和写入 PEM 格式的密钥文件

* **Success Criteria**:

  * BusinessRSAKeyManager 类不再重复实现密钥生成和处理逻辑

  * 所有方法都使用 RSAUtils 提供的方法

  * 代码更加简洁、可维护

* **Test Requirements**:

  * `programmatic` TR-1.1: 验证项目启动时是否能正确生成或读取密钥对

  * `programmatic` TR-1.2: 验证密钥对是否正确添加到全局缓存

  * `programmatic` TR-1.3: 验证加密和解密功能是否正常工作

* **Notes**: 使用 RSAUtils 提供的完整方法，避免重复造轮子

