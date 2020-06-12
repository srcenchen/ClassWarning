# 班级违纪 管理系统

## To-do List
- [ ] 完整的用户体系
 - [x] 登录功能
 - [ ] 注册功能
 - [ ] 切换账户 

## 目前已完成的工作
- 录入违纪信息
- 查询所有违纪
- 查询某次违纪
- 登录功能仅完成外壳
 - 登录功能中密码部分使用Base64加密上传至服务器，保障安全
 - 功能实现：将用户输入的密码在本地进行Base64加密处理，之后与服务器端对比加密后的文本，如果加密后与服务器存储注册时加密的一致时，允许登录

## 版本更新
### 0.0.1 发布
- 交接任务

### 0.0.2 更新
#### 功能新增
- 登录功能，仅仅可以登录，暂未实现每个人一个违纪账户
 - 注册功能尚未实现
 - 测试用户：sanenchen, sanenchen

#### 功能优化
- 搜索，查看所有违纪信息，在列表中优先输出近期违纪
 - 比如，一个2月的违纪，一个3月的违纪，将3月违纪放置前列

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
