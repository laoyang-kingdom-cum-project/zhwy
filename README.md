# 智慧物业管理系统

> ⚠️ **免责声明**：这是我在学校随便写的项目，代码质量非常屎，仅供学习参考！

## 项目简介

基于若依(RuoYi)框架二次开发的智慧物业管理系统，前后端分离架构。

- **管理后台**：Vue 2.x + Element UI
- **移动端**：UniApp（小程序/App）
- **后端**：Spring Boot 2.5.15 + MySQL + Redis

## 项目结构

```
zhwy/
├── ruoyi/              # 后端项目
│   ├── ruoyi-admin/    # 主启动模块
│   ├── ruoyi-common/   # 公共模块
│   ├── ruoyi-framework/# 框架核心
│   ├── ruoyi-system/   # 系统模块
│   ├── ruoyi-generator/# 代码生成
│   ├── ruoyi-quartz/   # 定时任务
│   └── ruoyi-ui/       # 管理后台前端（Vue）
├── uni/                # 移动端前端（UniApp）
└── lyfp.sql            # 数据库脚本
```

## 环境要求

- JDK 1.8+
- MySQL 5.7+
- Redis 5.0+
- Node.js 14+

## 如何运行

### 1. 数据库配置

1. 创建数据库 `lyfp`
2. 导入 `lyfp.sql` 文件
3. 修改数据库配置：`ruoyi/ruoyi-admin/src/main/resources/application-druid.yml`

```yaml
spring:
  datasource:
    druid:
      master:
        url: jdbc:mysql://localhost:3306/lyfp?useUnicode=true&characterEncoding=utf8...
        username: root
        password: root
```

### 2. 启动后端

```bash
cd ruoyi/ruoyi-admin/target
java -jar ruoyi-admin.jar
```

后端默认端口：**8080**

### 3. 启动管理后台（Vue）

```bash
cd ruoyi/ruoyi-ui
npm install
npm run dev
```

管理后台默认端口：**80**

### 4. 启动移动端（UniApp）

使用 HBuilderX 打开 `uni` 文件夹，运行到小程序模拟器或真机。

### 5. 访问系统

- 管理后台：http://localhost
- 后端接口：http://localhost:8080
- 默认账号：wyx / 123456
- Druid监控：http://localhost:8080/druid (ruoyi/123456)

## 功能模块

- 用户管理
- 角色权限管理
- 菜单管理
- 字典管理
- 系统监控
- 定时任务
- 代码生成
- ...（还有一些乱七八糟的功能）

## 注意事项

1. 代码写得比较随意，很多地方没有遵循最佳实践
2. 部分功能可能存在bug
3. 安全性未经过严格测试
4. 仅供学习和参考使用

## 技术栈

| 技术 | 版本 |
|------|------|
| Spring Boot | 2.5.15 |
| Spring Security | 5.7.12 |
| MyBatis | 3.5.x |
| Druid | 1.2.23 |
| Redis | - |
| Vue | 2.x |
| Element UI | 2.x |
| UniApp | - |

## 开源地址

本项目已在 GitHub 开源：[https://github.com/laoyang-kingdom-cum-project/zhwy](https://github.com/laoyang-kingdom-cum-project/zhwy)

## 关于

- 基于 [若依(RuoYi)](https://gitee.com/y_project/RuoYi-Vue) 框架开发
- 原框架文档：http://doc.ruoyi.vip

---

**再次提醒：代码很屎，轻喷！**
