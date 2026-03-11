# Loafer Blog - 添加删除时间字段和修改唯一约束 Verification Checklist

## 数据库表结构检查
- [ ] 所有表都添加了 `delete_time` 字段
- [ ] `delete_time` 字段类型为 TIMESTAMP，默认为 NULL
- [ ] 所有表的 `delete_time` 字段都有正确的注释
- [ ] 具有唯一约束的表的约束条件都包含了 `deleted` 和 `delete_time` 字段

## MyBatis Plus 配置检查
- [ ] `MyBatisPlusConfig.java` 中添加了 `delete_time` 字段的自动填充逻辑
- [ ] 自动填充逻辑能够在逻辑删除时正确触发
- [ ] 自动填充的时间格式正确

## 实体类检查
- [ ] 所有实体类都添加了 `deleteTime` 字段
- [ ] `deleteTime` 字段类型为 `LocalDateTime`
- [ ] 字段有正确的 `@TableField` 注解，指定了字段名和自动填充策略

## 功能测试检查
- [ ] 执行逻辑删除操作后，`delete_time` 字段被自动填充为当前时间
- [ ] 逻辑删除一条记录后，创建相同的记录成功，不会触发唯一约束冲突
- [ ] 所有具有唯一约束的表都通过了测试

## 兼容性检查
- [ ] 所有修改保持向后兼容，不影响现有数据
- [ ] 数据库迁移安全，确保数据不丢失
- [ ] 代码修改遵循现有的编码规范