###一、项目名称：
#####基于Windows平台开发的命令行文件检索工具
###二、项目背景：
     用户有时候需要打开某些已知文件名或者扩展名的一系列文件，在Windows系统中，都需要打开磁盘再逐一打开文件夹寻找文件，
    速度很慢，而这个项目就解决了这个问题，检索速度增快，并且使用方便。
    
###三、功能
+ 索引文件信息
+ 检索文件信息
     


###四、技术
+ JavaSE
+ Java多线程，线程池
+ JDBC编程
+ 嵌入式数据库H2
+ Apache Commons IO库
+ 接口编程、设计模式
+ 插件Lombok

###五、实现
+ 从上到下分层结构
+ 客户端---->统一调度器---->索引、检索、监控、拦截器---->数据库访问---->数据库


###六、使用
+ 程序发布包
+ lib（依赖的库）
+ jar包
+ 配置文件
+ 解压程序发布包
+ 启动程序
java -jar everything_g2-1.0.0.jar
