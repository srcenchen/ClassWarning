# 班级违纪 管理系统

## To-do List
- [x] 完整的用户体系
 - [x] 登录功能
 - [x] 注册功能
 - [x] 切换账户

## 测试用户
- 由于注册功能已经开发完毕，所以测试用户不再提供！

## 目前已完成的工作
- 录入违纪信息
- 查询所有违纪
- 查询某次违纪
- 登录功能仅完成外壳
- 登录功能中密码部分使用Hash-SHA-224加密上传至服务器，保障安全
- 功能实现：将用户输入的密码在本地进行Hash-SHA-224加密处理，之后与服务器端对比加密后的文本，如果加密后与服务器存储注册时加密的一致时，允许登录

# 版本更新
## 0.0.5 更新 2020/6/20
本次更新主要目的：新增用户注册功能
### 功能新增
- 新增注册功能
至此，用户体系已经建立！

## 0.0.4 更新 2020/6/19
本次更新主要目的：安全优化
### 功能新增
- 新增自动登录功能，免去了总是输入密码的窘境

### 功能优化
#### 安全方面
- 重写了服务端代码，更加安全
- 新增LinkID帮助区分用户与加密，使得个人信息不会轻易泄露
- LinkID：用户名(SHA-224)加密+4位数字随机码
- 安全！

## 0.0.3 更新
### 功能新增
- 增加了违纪上报日期
PS：没有实现注册功能!

### 功能优化
- 查询速度优化，已经变得十分快速！降低百分之50的加载时间
@sanyinchen提供优化方案

### 如何实现如此快速的速度？
- 使用NodeJS作为中转Service
- 由于不用查询一次就重新连接一次，大大提高使用体验

## 0.0.2 更新
### 功能新增
- 登录功能，仅仅可以登录，暂未实现每个人一个违纪账户
 - 注册功能尚未实现

### 功能优化
- 搜索，查看所有违纪信息，在列表中优先输出近期违纪
 - 比如，一个2月的违纪，一个3月的违纪，将3月违纪放置前列

## 0.0.1 发布
- 交接任务

## 开源执照（MIT）
MIT License

Copyright (c) 2020 PenSeeYou

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
