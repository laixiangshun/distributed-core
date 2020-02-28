##简介
该项目一款基于Redis + Zookeeper的分布式应用组件集合，包含分布式锁。
它采用 AOP框架进行切面架构，提供注解调用方式，也提供API调用方式

## 简介
- 分布式应用组件集合
  - Nepxion Aquarius Lock 分布式锁(支持Redis、Zookeeper、ReentrantLock本地锁)
- 支持Spring Boot集成
  - 提供Start模式，在类头部加注解@EnableLock()，同时结合配置文件lock.enabled=true/false进行相关Aop功能的关闭和开启
  - 支持Aop异常的中断业务方法调用和忽略中断两种选择
- 支持Spring Cloud集成
- 支持组件扩展适配，再次编程
  - RedissonAdapter，扩展实现可默认覆盖原生组件

## 兼容
- 默认支持Spring 5.x.x和Spring Boot 2.x.x，也兼容Spring 4.x.x和Spring Boot 1.x.x

## 依赖
```xml
分布式锁
<dependency>
    <groupId>com.htwz</groupId>
    <artifactId>lock-core-starter</artifactId>
    <version>${lock.version}</version>
</dependency>
```

## Nepxion Aquarius Lock
基于Redisson(Redis)、Curator(Zookeeper)分布式锁和本地锁，构建于AOP framework，你可以在这三个锁组件中选择一个移植入你的应用中

### 提示
- 注解的Key支持Java8下的SPEL语义拼装。但SPEL语义对于接口代理的方式，需要打开编译参数项
- 参照说明，需要在IDE和Maven里设置"-parameters"的Compiler Argument。具体参考如下：
  - Eclipse加"-parameters"参数：https://www.concretepage.com/java/jdk-8/java-8-reflection-access-to-parameter-names-of-method-and-constructor-with-maven-gradle-and-eclipse-using-parameters-compiler-argument
  - Idea加"-parameters"参数：http://blog.csdn.net/royal_lr/article/details/52279993

### 介绍
- 锁既支持Redisson(基于Redis)和Curator(基于Zookeeper)机制的分布式锁，也支持ReentrantLock机制的本地锁
- 锁既支持普通可重入锁，也支持读/写可重入锁
  - 普通可重入锁都是互斥的
  - 读/写可重入锁必须配对使用，规则如下：
    - 当写操作时，其他分布式进程/线程无法读取或写入数据；当读操作时，其他分布式进程/线程无法写入数据，但可以读取数据
    - 允许同时有多个读锁，但是最多只能有一个写锁。多个读锁不互斥，读锁与写锁互斥
- 锁既支持公平锁，也支持非公平锁
- 锁既支持同步执行方式，也支持异步执行方式(异步拿锁，同步阻塞)
- 锁既支持持锁时间后丢弃，也支持持锁超时等待时间
- 锁注解既可以加在接口上，也可以加在实现类上，也可以加在没有接口只有类的情形下
  - 注解说明
    - 注解Lock，普通可重入锁
    - 注解ReadLock，读可重入锁
    - 注解WriteLock，写可重入锁
  - 参数说明
    - name 锁的名字
    - key 锁的Key。锁Key的完整路径是prefix + "_" + name + "_" + key，prefix为config.propertie里的namespace值
    - leaseTime 持锁时间，持锁超过此时间则自动丢弃锁(Redisson支持，Curator和本地锁不支持)
    - waitTime 没有获取到锁时，等待时间
    - async 是否采用锁的异步执行方式(默认都支持同步执行方式，Redisson三种锁都支持异步，Curator三种锁都不支持异步，本地锁三种锁都不支持异步)
    - fair 是否采用公平锁(默认都支持非公平锁，Redisson三种锁只有普通可重入锁支持公平锁，Curator三种锁都不支持公平锁，本地锁三种锁都支持公平锁)
- 锁由于是可重入锁，支持缓存和重用机制
- 锁组件采用通过改变Pom中对锁中间件类型的引用，达到快速切换分布式锁的目的
  - 实现对redisson支持若干种部署方式(例如单机，集群，哨兵模式)，并支持json和yaml(默认)两种配置方式，要切换部署方式，只需要修改相应的redisson.yaml文件即可。具体参考如下：
    https://github.com/redisson/redisson/wiki/2.-%E9%85%8D%E7%BD%AE%E6%96%B9%E6%B3%95
    同时也支持redisson开头的配置方法（只支持单机，哨兵模式）
  - 实现对Curator的多种重试机制(例如exponentialBackoffRetry, boundedExponentialBackoffRetry, retryNTimes, retryForever, retryUntilElapsed)，可在配置文件里面切换
- 锁支持两种调用方式，注解方式和直接调用方式

### 示例
使用分布式锁示例如下

普通分布式锁的使用

注解方式
```java
public interface MyService1 {
    @Lock(name = "lock", key = "#id1 + \"-\" + #id2", leaseTime = 5000L, waitTime = 60000L, async = false, fair = false)
    String doA(String id1, String id2);

    String doB(String id1, String id2);
}
```

```java

@Service("myService2Impl")
public class MyService2Impl {
    private static final Logger LOG = LoggerFactory.getLogger(MyService2Impl.class);

    @Lock(name = "lock", key = "#id1 + \"-\" + #id2", leaseTime = 5000L, waitTime = 60000L, async = false, fair = false)
    public String doC(String id1, String id2) {
        try {
            TimeUnit.MILLISECONDS.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOG.info("doC - lock is got");

        return "C";
    }

    public String doD(String id1, String id2) {
        LOG.info("doD");

        return "D";
    }
}
```

```java

@SpringBootApplication
@EnableLock
@ComponentScan(basePackages = { "com.htwz.service" })
public class LockAopApplication {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(LockAopApplication.class, args);

        // 执行效果是doA和doC无序打印，即谁拿到锁谁先运行
        MyService1 myService1 = applicationContext.getBean(MyService1.class);
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    myService1.doA("X", "Y");
                }
            }).start();
        }

        MyService2Impl myService2 = applicationContext.getBean(MyService2Impl.class);
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    myService2.doC("X", "Y");
                }
            }).start();
        }
    }
}
```

直接调用方式
```java
@SpringBootApplication
@EnableLock
@ComponentScan(basePackages = { "com.htwz.service" })
public class LockApplication {
    private static final Logger LOG = LoggerFactory.getLogger(LockApplication.class);

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(LockApplication.class, args);

        LockExecutor<Object> lockExecutor = applicationContext.getBean(LockExecutor.class);
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Object lock = null;
                    try {
                        lock = lockExecutor.tryLock(LockType.LOCK, "lock", "X-Y", 5000L, 60000L, false, false);
                        if (lock != null) {
                            try {
                                TimeUnit.MILLISECONDS.sleep(2000L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            LOG.info("doA - lock is got");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            lockExecutor.unlock(lock);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Object lock = null;
                    try {
                        lock = lockExecutor.tryLock(LockType.LOCK, "lock", "X-Y", 5000L, 60000L, false, false);
                        if (lock != null) {
                            try {
                                TimeUnit.MILLISECONDS.sleep(2000L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            LOG.info("doC - lock is got");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            lockExecutor.unlock(lock);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
}
```

读/写分布式锁的使用

注解方式
```java
public interface MyService3 {
    @ReadLock(name = "lock", key = "#id1 + \"-\" + #id2", leaseTime = 5000L, waitTime = 60000L, async = false, fair = false)
    String doR(String id1, String id2);
}
```

```java

@Service("myService4Impl")
public class MyService4Impl {
    private static final Logger LOG = LoggerFactory.getLogger(MyService4Impl.class);

    @WriteLock(name = "lock", key = "#id1 + \"-\" + #id2", leaseTime = 15000L, waitTime = 60000L, async = false, fair = false)
    public String doW(String id1, String id2) {
        try {
            TimeUnit.MILLISECONDS.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOG.info("doW - write lock is got");

        return "W";
    }
}
```

```java
@SpringBootApplication
@EnableLock
@ComponentScan(basePackages = { "com.htwz.service" })
public class ReadWriteLockAopApplication {
    private static final Logger LOG = LoggerFactory.getLogger(ReadWriteLockAopApplication.class);

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ReadWriteLockAopApplication.class, args);

        // 执行效果是先打印doW，即拿到写锁，再打印若干个doR，即可以同时拿到若干个读锁
        MyService4Impl myService4 = applicationContext.getBean(MyService4Impl.class);
        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                LOG.info("Start to get write lock...");
                // 写锁逻辑，最高持锁15秒，睡眠10秒，10秒后释放读锁
                myService4.doW("X", "Y");
            }
        }, 0L, 600000L);

        MyService3 myService3 = applicationContext.getBean(MyService3.class);
        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                LOG.info("Start to get read lock...");
                for (int i = 0; i < 3; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 读锁逻辑，最高持锁5秒，睡眠2秒，2秒后释放读锁
                            myService3.doR("X", "Y");
                        }
                    }).start();
                }
            }
        }, 2000L, 2000L);
    }
}
```

直接调用方式
```java
@SpringBootApplication
@EnableLock
@ComponentScan(basePackages = { "com.htwz.service" })
public class ReadWriteLockApplication {
    private static final Logger LOG = LoggerFactory.getLogger(ReadWriteLockApplication.class);

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ReadWriteLockApplication.class, args);

        LockExecutor<Object> lockExecutor = applicationContext.getBean(LockExecutor.class);
        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                LOG.info("Start to get write lock...");
                // 写锁逻辑，最高持锁15秒，睡眠10秒，10秒后释放读锁
                Object lock = null;
                try {
                    lock = lockExecutor.tryLock(LockType.WRITE_LOCK, "lock", "X-Y", 15000L, 60000L, false, false);
                    if (lock != null) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(10000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        LOG.info("doW - write lock is got");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        lockExecutor.unlock(lock);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0L, 600000L);

        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                LOG.info("Start to get read lock...");
                for (int i = 0; i < 3; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 读锁逻辑，最高持锁5秒，睡眠2秒，2秒后释放读锁
                            Object lock = null;
                            try {
                                lock = lockExecutor.tryLock(LockType.READ_LOCK, "lock", "X-Y", 5000L, 60000L, false, false);
                                if (lock != null) {
                                    try {
                                        TimeUnit.MILLISECONDS.sleep(2000L);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    LOG.info("doR - read lock is got");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    lockExecutor.unlock(lock);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
            }
        }, 2000L, 2000L);
    }
}
```

