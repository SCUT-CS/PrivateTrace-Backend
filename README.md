# 项目架构

## 模块设置

计划封装三个模块。

1. 核心算法模块，不开源。
2. 服务器后端逻辑部分，开源。
3. 前端界面开发部分，开源。

交作业时提供混淆后和运行的Jar/War包和开源模块的源代码。

## 工具

- Git/GitHub/GitHub Desktop
- Intellij （Ultimate）
  - CS61B (Code Style)
  - Copilot
- WebStorm

注释格式

```java
/** 
 * [Description here（return what）]
 * @param [Parameters and Description](optional)
 * @param [Parameters and Description](optional)
 * @author [Your name]
 * @source (optional)
 */

/**
 * encrypt a image and return true if success.
 * @param img the image to be encrypted.
 * @author Cui Yuxin
 */
private boolean encrypt(Bitmap img) {
        
}
```

# TODO

- 核心算法模块

  - [x] 加密数据结构的序列化

  - [x] 关键流程封装

  - [x] 使用Proprieties代替Constant
  - [x] 封装接口