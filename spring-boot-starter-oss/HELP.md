### springboot整合 阿里云 OSS 对象存储

#### 使用步骤

第一步：添加依赖

~~~xml

<dependency>
    <groupId>com.example</groupId>
    <artifactId>spring-boot-starter-oss</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
~~~

第二步：创建application.yml文件

~~~yaml

aliyun:
  oss:
    endpoint: oss-cn-hangzhou.aliyuncs.com
    accessKeyId: LTAI4GL4U2cfxxxxw
    accessKeySecret: zIVArxxxxxxxxxxxxx

~~~

第三步：使用

~~~java

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootPushApplication.class)
public class UploadTest {
    @Autowired
    private OSSClient ossClient;

    @Test
    public void upload() throws FileNotFoundException {

        String file = "/Users/Downloads/20220518141025&1526806782837227522.pdf";
        String name = "pdf/tainan/bid/report/20220528813143433.pdf";

        InputStream inputStream = new FileInputStream(file);
        ossClient.putObject("gongbaojinji", name, inputStream);
        ossClient.shutdown();

    }
}

~~~
