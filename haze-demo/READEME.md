### **包`designpattern`为设计模式**
     state包 state design pattern
     
     template包 模板设计模式
         应用场景：
          1 父类实现常用方法和算法，其余可变部分交由字类实现，避免代码重复实现
          2 定义模板方法(增加hook切入点)控制字类扩展
         JDK中应用：
         java.util.Collections#sort()
         java.io.InputStream#skip()
         java.io.InputStream#read()
         java.util.AbstractList#indexOf()