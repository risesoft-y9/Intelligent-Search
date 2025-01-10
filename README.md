<p align="center">
 <img alt="logo" src="https://vue.youshengyun.com/files/img/qrCodeLogo.png">
<p align="center">
 <a href='https://gitee.com/risesoft-y9/y9-datacenter/stargazers'><img src='https://gitee.com/risesoft-y9/y9-datacenter/badge/star.svg?theme=dark' alt='star'></img></a>
    <img src="https://img.shields.io/badge/version-v9.6.6-yellow.svg">
    <img src="https://img.shields.io/badge/Spring%20Boot-2.7-blue.svg">
    <img alt="logo" src="https://img.shields.io/badge/Vue-3.3-red.svg">
    <img alt="" src="https://img.shields.io/badge/JDK-11-green.svg">
    <a href="https://gitee.com/risesoft-y9/y9-core/blob/master/LICENSE">
    <img src="https://img.shields.io/badge/license-GPL3-blue.svg"></a>
</p>

## 简介

智能检索是一款基于Elasticsearch开发的检索软件，为满足大数据高效检索的需求而设计，界面简介友好，操作流畅。该软件深度整合了Elasticsearch的强大搜索功能，可应对多类型海量数据的治理及查询要求，并对查询文件进行相关文件推荐。为满足用户多样化检索需求，提供精准检索与智能检索两种不同的检索方式，且支持多种排序方式、多字段、多条件组合查询并高亮关键词，提供更为极致的检索效果。此外建立后台进行数据监控、数据白名单、检索关键词统计等功能，提高用户体验并高效管理数据。

## 后端技术选型

| 序号 | 依赖              | 版本      | 官网                                                                                                                 |
|----|-----------------|---------|--------------------------------------------------------------------------------------------------------------------|
| 1  | Spring Boot     | 2.7.10  | <a href="https://spring.io/projects/spring-boot" target="_blank">官网</a>                                            |
| 2  | SpringDataJPA   | 2.7.10  | <a href="https://spring.io/projects/spring-data-jpa" target="_blank">官网</a>                                        |
| 3  | nacos           | 2.2.1   | <a href="https://nacos.io/zh-cn/docs/v2/quickstart/quick-start.html" target="_blank">官网</a>                        |
| 4  | druid           | 1.2.16  | <a href="https://github.com/alibaba/druid/wiki/%E9%A6%96%E9%A1%B5" target="_blank">官网</a>                          |
| 5  | Jackson         | 2.13.5  | <a href="https://github.com/FasterXML/jackson-core" target="_blank">官网</a>                                         |
| 6  | javers          | 6.13.0  | <a href="https://github.com/javers/javers" target="_blank">官网</a>                                                  |
| 7  | lombok          | 1.18.26 | <a href="https://projectlombok.org/" target="_blank">官网</a>                                                        |
| 8  | logback         | 1.2.11  | <a href="https://www.docs4dev.com/docs/zh/logback/1.3.0-alpha4/reference/introduction.html" target="_blank">官网</a> |
| 9  | Elaticsearch         | 7.6.2  | <a href="https://www.elastic.co/cn/" target="_blank">官网</a> |

## 前端技术选型

| 序号 | 依赖           | 版本      | 官网                                                                     |
|----|--------------|---------|------------------------------------------------------------------------|
| 1  | vue          | 3.3.2   | <a href="https://cn.vuejs.org/" target="_blank">官网</a>                 |
| 2  | vite2        | 2.9.13  | <a href="https://vitejs.cn/" target="_blank">官网</a>                    |
| 3  | vue-router   | 4.0.13  | <a href="https://router.vuejs.org/zh/" target="_blank">官网</a>          |
| 4  | pinia        | 2.0.11  | <a href="https://pinia.vuejs.org/zh/" target="_blank">官网</a>           |
| 5  | axios        | 0.24.0  | <a href="https://www.axios-http.cn/" target="_blank">官网</a>            |
| 6  | typescript   | 4.5.4   | <a href="https://www.typescriptlang.org/" target="_blank">官网</a>       |
| 7  | core-js      | 3.20.1  | <a href="https://www.npmjs.com/package/core-js" target="_blank">官网</a> |
| 8  | element-plus | 2.2.29  | <a href="https://element-plus.org/zh-CN/" target="_blank">官网</a>       |
| 9  | sass         | 1.58.0  | <a href="https://www.sass.hk/" target="_blank">官网</a>                  |
| 10 | animate.css  | 4.1.1   | <a href="https://animate.style/" target="_blank">官网</a>                |
| 11 | vxe-table    | 4.3.5   | <a href="https://vxetable.cn" target="_blank">官网</a>                   |
| 12 | echarts      | 5.3.2   | <a href="https://echarts.apache.org/zh/" target="_blank">官网</a>        |
| 13 | svgo         | 1.3.2   | <a href="https://github.com/svg/svgo" target="_blank">官网</a>           |
| 14 | lodash       | 4.17.21 | <a href="https://lodash.com/" target="_blank">官网</a>                   |

## 中间件选型

| 序号 | 工具               | 版本   | 官网                                                                        |
|----|------------------|------|---------------------------------------------------------------------------|
| 1  | JDK              | 11   | <a href="https://openjdk.org/" target="_blank">官网</a>                     |
| 2  | Tomcat           | 9.0+ | <a href="https://tomcat.apache.org/" target="_blank">官网</a>               |

## 数据库选型

| 序号 | 工具            | 版本         | 官网                                                                        |
|----|---------------|------------|---------------------------------------------------------------------------|
| 1  | elasticsearch | 7.9+       | <a href="https://www.elastic.co/cn/elasticsearch/" target="_blank">官网</a> |


## 信创兼容适配

| **序号** | 类型   | 对象                 |
|:-------|------|--------------------|
| 1      | 浏览器  | 奇安信、火狐、谷歌、360等     |
| 2      | 插件   | 金山、永中、数科、福昕等       |
| 3      | 中间件  | 东方通、金蝶、宝兰德等        |
| 4      | 数据库  | 人大金仓、达梦、高斯等        |
| 5      | 操作系统 | 统信、麒麟、中科方德等        |
| 6      | 芯片   | ARM体系、MIPS体系、X86体系 |

## 在线体验

### 智能检索

**演示地址**
前台：<https://test.youshengyun.com/y9vue-dataCenter/>
后台：<https://test.youshengyun.com/y9vue-retrievalManager/>

> 演示账号：user 密码：Risesoft@2024
>

## 文档专区

开发文档：https://docs.youshengyun.com/

| 序号 | 名称                                                                                                               |
|:---|------------------------------------------------------------------------------------------------------------------|
| 1  | <a href="https://vue.youshengyun.com/files/内部Java开发规范手册.pdf" target="_blank">内部Java开发规范手册</a>                    |
| 2  | <a href="https://vue.youshengyun.com/files/日志组件使用文档.pdf" target="_blank">日志组件使用文档</a>                            |
| 3  | <a href="https://vue.youshengyun.com/files/文件组件使用文档.pdf" target="_blank">文件组件使用文档</a>                            |
| 4  | <a href="https://vue.youshengyun.com/files/代码生成器使用文档.pdf" target="_blank">代码生成器使用文档</a>                          |
| 5  | <a href="https://vue.youshengyun.com/files/配置文件说明文档.pdf" target="_blank">配置文件说明文档</a>                            |
| 6  | <a href="https://vue.youshengyun.com/files/常用工具类使用示例文档.pdf" target="_blank">常用工具类使用示例文档</a>                      |
| 7  | <a href="https://vue.youshengyun.com/files/有生博大Vue开发手册v1.0.pdf" target="_blank">前端开发手册</a>                       |
| 8  | <a href="https://vue.youshengyun.com/files/开发规范.pdf" target="_blank">前端开发规范</a>                                  |
| 9  | <a href="https://vue.youshengyun.com/files/代码格式化.pdf" target="_blank">前端代码格式化</a>                                |
| 10 | <a href="https://vue.youshengyun.com/files/系统组件.pdf" target="_blank">前端系统组件</a>                                  |
| 11 | <a href="https://vue.youshengyun.com/files/通用方法.pdf" target="_blank">前端通用方法</a>                                  |
| 12 | <a href="https://vue.youshengyun.com/files/国际化.pdf" target="_blank">前端国际化</a>                                    |
| 13 | <a href="https://vue.youshengyun.com/files/Icon图标.pdf" target="_blank">前端Icon图标</a>                              |
| 14 | <a href="https://vue.youshengyun.com/files/单点登录对接文档.pdf" target="_blank">单点登录对接文档</a>                            |


## 依赖开源项目

| 序&nbsp;号 | 项&nbsp;目&nbsp;&nbsp;名&nbsp;称          | 项目介绍           | 地&nbsp;址                                                                                                                                                          |
| ----- | ----------- | ----------------------------------------- |-------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1    | 数字底座 | 数字底座是一款面向大型政府、企业数字化转型，基于身份认证、组织架构、岗位职务、应用系统、资源角色等功能构建的统一且安全的管理支撑平台。数字底座基于三员管理模式，具备微服务、多租户、容器化和国产化，支持用户利用代码生成器快速构建自己的业务应用，同时可关联诸多成熟且好用的内部生态应用      | <a href="https://gitee.com/risesoft-y9/y9-core" target="_blank">码云</a> <a href="https://github.com/risesoft-y9/Digital-Infrastructure" target="_blank">GitHub</a> |

## 赞助与支持

### 中关村软件和信息服务产业创新联盟

官网：<a href="https://www.zgcsa.net" target="_blank">https://www.zgcsa.net</a>

### 北京有生博大软件股份有限公司

官网：<a href="https://www.risesoft.net/" target="_blank">https://www.risesoft.net/</a>


### 中国城市发展研究会

官网：<a href="https://www.china-cfh.com/" target="_blank">https://www.china-cfh.com/</a>


## 咨询与合作

联系人：曲经理

微信号：qq349416828

备注：开源咨询-姓名
<div><img style="width: 40%" src="https://vue.youshengyun.com/files/img/开源网站图片上传/曲经理-微信二维码.png"><div/>
联系人：有生博大-咨询热线

座机号：010-86393151
<div><img style="width: 45%" src="https://vue.youshengyun.com/files/img/有生博大-咨询热线.png"><div/>
