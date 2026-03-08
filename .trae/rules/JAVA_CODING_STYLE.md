---
alwaysApply: false
globs: 
  - '**/backend/**'
---
# Java 编码风格规范

## 1. 代码格式
- **缩进**：4个空格，禁止Tab
- **行宽**：单行不超过120字符
- **空格**：运算符两侧、逗号后加空格；方法名与括号间无空格
- **大括号**：左大括号不换行，右大括号单独换行；空代码块使用 `{}`

## 2. 命名规则
|元素|规则|示例|
|---|---|---|
|包名|全小写，反向域名|com.trae.project.user|
|类/接口|大驼峰，名词|UserService|
|方法/变量|小驼峰，动词|getUserById|
|常量|全大写，下划线分隔|MAX_PAGE_SIZE|
|枚举值|全大写，下划线分隔|STATUS_SUCCESS|
|抽象类|前缀Abstract|AbstractBaseService|
|异常类|后缀Exception|BusinessException|

## 3. 注释规范
- **类/接口**：必须添加注释，包含作者、创建时间、功能描述
- **方法**：public/protected方法必须添加注释，说明入参、返回值、异常
- **单行**：使用`//`，注释内容与代码隔一个空格

## 4. 类型规范
- **控制器入参**：使用专用DTO类，添加参数校验注解，避免`Map<String, Object>`
- **控制器返回**：使用`ResponseEntity`包装具体DTO/VO类型，避免`Map<String, Object>`
- **服务层**：使用具体DTO/VO类型或基本类型，避免`Map<String, Object>`