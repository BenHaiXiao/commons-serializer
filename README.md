#  commons-serializer

序列化工具，是java网络编程，IO编程中常用的工具，本项目主要集成各种序列化工具，方便使用
当前集成工具：
1. Kryo 
2. Fst
3. Jvm

将来会有更多的工具集成，敬请关注

## 使用方法

对外外接口
```
public interface Serializater {
	
	public byte[] serialize(final Serializable obj);

	public <T> T deserialize(final byte[] objectData);
	
}
```

## 性能报告

运行PerformanceTest 类中Test方法，即可得出当前机器的报告。
下面是在I5,3.3GHz机器上的报告：
1. 序列化 
![](http://empfs.bs2dl.yy.com/bWQtMTUyMDIxNTUwMDg3ODZfMTUyMDIxNTUwMDg3OQ.png)
2. 序列化 
![](http://empfs.bs2dl.yy.com/bWQtMTUyMDIxNTYxMDA0NzdfMTUyMDIxNTYxMDA0Nw.png)


