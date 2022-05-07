### 获取照片的Exif信息

##### 示例

第一步：创建starter工程spring-boot-exif并配置pom.xml文件

~~~xml

<dependencies>
    <!--获取相机exif信息-->
    <dependency>
        <groupId>com.drewnoakes</groupId>
        <artifactId>metadata-extractor</artifactId>
        <version>2.18.0</version>
    </dependency>
    <!--工具类-->
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
    </dependency>

</dependencies>

~~~

第二步：创建application.yml文件

~~~yaml
server:
  port: 8080
hello:
  name: xiaoming
  address: beijing
~~~

第三步：创建服务类 GpsExifServiceImpl

~~~java

package com.example.service;

import com.drew.imaging.ImageProcessingException;

import java.io.IOException;

/**
 * @Author: wgs
 * @Date 2022/5/6 17:39
 * @Classname GpsExifService
 * @Description
 */
public interface GpsExifService {

    /**
     * 打印图片exif信息
     *
     * @param imgPath img路径
     */
    public abstract void printPicExifInfo(String imgPath);

    /**
     * 打印pic exif文件
     *
     * @param imgDir 图片文件夹
     */
    public abstract void printPicExifFile(String imgDir);
}

~~~

第四步：创建HelloController

~~~java

@RestController
@RequestMapping("/exif")
public class GpsExifController {
    @Autowired
    private GpsExifService gpsExifService;

    @GetMapping("/imgPath")
    public String printInfo(String path) {
        gpsExifService.printPicExifInfo(path);
        return "ok";
    }

    @GetMapping("/imgDir")
    public String printInfoList(String path) {
        gpsExifService.printPicExifFile(path);
        return "ok";
    }
}
~~~

第五步：创建启动类SpringBootExifApplication

~~~java
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootExifApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootExifApplication.class, args);
    }

}

~~~

执行启动类main方法，访问地址127.0.0.1:8080/exif/imgPath?path=/Users/2022-05/2091651887544_.pic_hd.jpg

~~~
压缩格式  Compression : JPEG (old-style)
图像宽度  Image Width : 3008 pixels
图像高度  Image Height : 4012 pixels
拍摄手机  Make : Xiaomi
型号  Model : Mi 10
经度  GPS Latitude : 30° 12' 40.36", °转dec: 30.211211111111112
纬度  GPS Longitude : 120° 13' 15.41", °转dec: 120.22094722222222
高度  GPS Altitude : 0 metres
UTC时间戳  GPS Time-Stamp : 01:38:54.000 UTC
gps日期  GPS Date Stamp : 2022:05:07
iso速率  ISO Speed Ratings : 76
曝光时间  Exposure Time : 0.01 sec
曝光模式  Exposure Mode : Auto exposure
光圈值  F-Number : f/1.7
焦距  Focal Length 35 : 24 mm
图像色彩空间  Color Space : sRGB
场景类型  Scene Type : Directly photographed image
{"infocode":"10000","regeocode":{"formatted_address":"浙江省杭州市滨江区西兴街道江南大道","roadinters":[{"second_id":"0571H51F022002982","first_id":"0571H51F022002673587","distance":"69.8893","second_name":"江南大道","location":"120.220220,30.211230","first_name":"建设河绿道","direction":"东"}],"aois":[],"roads":[{"distance":"19.0597","name":"江南大道","location":"120.221,30.2114","id":"0571H51F022002982","direction":"东南"},{"distance":"39.7519","name":"建设河绿道","location":"120.221,30.2115","id":"0571H51F022002673587","direction":"东南"},{"distance":"51.0022","name":"阡陌路","location":"120.22,30.211","id":"0571H51F02200210992","direction":"东北"}],"pois":[{"poiweight":"0.22635","businessarea":"西兴","address":"江南大道与阡陌路交叉口南100米","distance":"76.6593","name":"国家物联网产业示范基地","location":"120.220367,30.210738","tel":[],"id":"B0FFKSHBCL","type":"商务住宅;楼宇;商务写字楼|商务住宅;产业园区;产业园区","direction":"西南"}],"addressComponent":{"businessAreas":[{"name":"西兴","location":"120.221721,30.194076","id":"330108"}],"country":"中国","province":"浙江省","citycode":"0571","city":"杭州市","adcode":"330108","streetNumber":{"number":"57号","distance":"58.3578","street":"江南大道","location":"120.221183,30.210728","direction":"东南"},"towncode":"330108001000","district":"滨江区","neighborhood":{"name":[],"type":[]},"township":"西兴街道","building":{"name":[],"type":[]}}},"status":"1","info":"OK"}
拍摄地点：浙江省杭州市滨江区西兴街道江南大道
2022-05-07 10:17:23.508  INFO 72724 --- [extShutdownHook] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'

~~~

### [参考](https://blog.csdn.net/qq_40985985/article/details/118605888)