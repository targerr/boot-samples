


Feign拦截器:
* 注意: 值得注意的是：该拦截器所在的线程不是controller层接收到请求的线程，因为feign是利用多个线程池来发送请求的，所以，主线程中的ThreadLocal对象在这里失效了


* [Spring Cloud Alibaba组件介绍](https://blog.csdn.net/m0_67402125/article/details/123684210)
* [Sentinel](https://xie.infoq.cn/article/f026552e0ccd71f3d9279b518)
* [Apple Mac M1 docker环境下nacos无法启动](https://github.com/alibaba/nacos/issues/6340)