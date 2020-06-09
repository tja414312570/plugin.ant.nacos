# plugin.ant.nacos
plugin.ant使用Alibaba的nacos作为服务中心和配置中心的实现
Ant.yc配置
```xml
Ant:{ 
	##	基础配置
	bufferType:"heap",	## 内存类型  heap 或 direct
	timeout:30000,  ## 超时
	process:10,	##处理消息时的线程数
	checkTime:1000,	##每次消息检查时间间隔 毫秒
	bufferSize:1024	,	## buffer大小
	bufferMaxSize:20480 		##最大buffer大小
	##	作为服务提供者必须填写
	port:4280,##	对位服务端口
	host:127.0.0.1,	## 对外服务ip
	name:"queue", ##对外服务名
	
}
nacos:{
	host:"127.0.0.1",
	port:"8858",
	group:"queue",
	name:"queue",
	namespace:"",
}
```
测试代码:
```java
    PlugsFactory.getInstance().addPlugs(AntMeessageSerialHandler.class);//消息序列化
		PlugsFactory.getInstance().addPlugs(DefaultAntClientServiceImpl.class);
		AntNacosRuntime antNacosRuntime = new AntNacosRuntime("classpath:Ant.yc");
		//启动Ant
		AntContext antContext = AntFactory.build("classpath:Ant.yc");
		antContext.init();
		antContext.start();
```
