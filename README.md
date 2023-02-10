# IotHub

#### 介绍
基于Java语言开发的，跨平台标准化终端设备适配服务，解决多种外设适配问题，方便集成开发接入。适用于打印机、读写器等通过串口或网络连接的设备，支持HTTP、MQTT消息协议，统一接口报文规范。
目前适配的设备类型：斑马打印机（ZT230、ZT4xx等）、博思得打印机、各类桌面读写器、RFID相关设备。

* 运行平台：适用于windows操作系统
* 源于某固定资产管理系统方案，适用于各类管理系统对接windows外接终端，兼容公有云/私有云架构方式
* 此方案思路可供参考，如需深入了解可邮件联系作者hou610433155@163.com
#### 软件架构
基于Springboot框架开发，对外提供HTTP、MQTT消息接入方式(后续将按需增加新的消息协议)。采用适配器开发模式，统一多种设备对接方式。
![输入图片说明](docs/IOTHub-Server.png)
![输入图片说明](docs/HTTP&MQTT%E8%AE%BE%E5%A4%87%E4%BA%92%E8%81%94%E8%AF%B7%E6%B1%82_%E6%97%B6%E5%BA%8F.jpg)

#### 二次开发

1.  MQTT  p2p 部分逻辑
2.  各类硬件集成开发方式
3.  介绍如何打包
4.  使用install4j打exe应用服务  （打包配置仓库）
5.  RFID相关
#### 使用说明

1.  服务部署至windows
2.  配置文件说明
3.  服务接口对接

