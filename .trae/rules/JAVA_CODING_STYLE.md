---
alwaysApply: false
globs: 
  - '**/backend/**'
---
# Java 编码风格规范

## 1. 代码格式

### 1.1 缩进
- 统一使用 **4个空格**，禁止使用Tab键。

### 1.2 行宽
- 单行代码不超过120字符，超过时合理换行。

### 1.3 空格
- 运算符两侧、逗号后加空格；方法名与括号间无空格。

```Java 
// 正确
sum = a + b;
// 错误
sum=a+b;
```

### 1.4 大括号
- 左大括号不换行，右大括号单独换行；空代码块使用 `{}`。

## 2. 命名规则

|元素类型|规则|示例| 
|---|---|---| 
|包名|全小写，反向域名|com.trae.project.user| 
|类/接口|大驼峰，名词|UserService| 
|方法/变量|小驼峰，动词|getUserById| 
|常量|全大写，下划线分隔|MAX_PAGE_SIZE| 
|枚举值|全大写，下划线分隔|STATUS_SUCCESS| 
|抽象类|前缀Abstract|AbstractBaseService| 
|异常类|后缀Exception|BusinessException| 

## 3. 注释规范

### 3.1 类/接口注释
- 必须添加类注释，包含作者、创建时间、功能描述。

### 3.2 方法注释
- public/protected方法必须添加注释，说明入参、返回值、异常。

### 3.3 单行注释
- 使用`//`，注释内容与代码隔一个空格。