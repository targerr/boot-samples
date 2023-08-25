## 整合OSS

### 示例

#### 第一步：创建starter工程spring-boot-upload-oss并配置pom.xml文件

~~~xml

<dependencies>
    <!-- 阿里云 oss存储包-->
    <dependency>
        <groupId>com.aliyun.oss</groupId>
        <artifactId>aliyun-sdk-oss</artifactId>
        <version>3.10.2</version>
        <scope>compile</scope>
    </dependency>
    <!--七牛云依赖-->
    <dependency>
        <groupId>com.qiniu</groupId>
        <artifactId>qiniu-java-sdk</artifactId>
        <version>[7.7.0, 7.10.99]</version>
    </dependency>
    <!--工具类-->
    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
    </dependency>
</dependencies>
~~~

#### 第二步：配置文件

```yaml
# 系统业务参数
isys:

  oss:
    file-root-path: /Users/develop/upload #存储根路径 ( 无需以‘/’结尾 )
    file-public-path: ${isys.oss.file-root-path}/public #公共读取块  ( 一般配合root-path参数进行设置，需以‘/’ 开头, 无需以‘/’结尾 )
    file-private-path: ${isys.oss.file-root-path}/private #私有化本地访问，不允许url方式公共读取 ( 一般配合root-path参数进行设置，需以‘/’ 开头, 无需以‘/’结尾  )

    # [local]: 本地存储，所有的文件将存在放本地，可通过nfs, rsync工具实现多机共享；
    # [aliyun-oss]: 将文件统一上传到阿里云oss服务器;
    # [qiniu-oss]: 将文件统一上传到七牛云oss服务器;
    service-type: qiniu-oss

    # 阿里云OSS服务配置信息
    aliyun-oss:
      endpoint: oss-us-west-1.aliyuncs.com  #endpoint  如： oss-cn-beijing.aliyuncs.com
      public-bucket-name: bucket #公共读 桶名称
      private-bucket-name: bucket  #私有 桶名称
      access-key-id: LTAI5tApjwUBpNV3Ug26AT   #AccessKeyId
      access-key-secret: S9n9q7bNI2bt01dk0Epcy2wvEFBxxx  #AccessKeySecret
    qiniu-oss:
      ak: k78TmLqWbytGbOY1xpyEbguXOkAgjaeOVLoJ4FlD # ak
      sk: sXrxQItLMGZZ9qOpii2YmrEw5hRQucHW-p7k-xxx # sk
      bucket: xxx # 桶名称

```

#### 第三步: 配置类

##### 3.1 OssSavePlaceEnum(权限)

```java
public enum OssSavePlaceEnum {
    /**
     * 公共读取
     */
    PUBLIC,
    /**
     * 私有存储
     */
    PRIVATE;
}

```

##### 3.2 定义文件上传的配置信息

```java

@Data
@AllArgsConstructor
public class OssFileConfig {

    /**
     * 用户头像
     */
    interface BIZ_TYPE {
        /**
         * 用户头像
         */
        String AVATAR = "avatar";
        /**
         * 接口类型卡片背景图片
         */
        String IF_BG = "ifBG";
        /**
         * 接口参数
         */
        String CERT = "cert";
    }

    /**
     * 图片类型后缀格式
     */
    public static final Set<String> IMG_SUFFIX = new HashSet<>(Arrays.asList("jpg", "png", "jpeg", "gif"));

    /**
     * 全部后缀格式的文件标识符
     */
    public static final String ALL_SUFFIX_FLAG = "*";

    /**
     * 不校验文件大小标识符
     */
    public static final Long ALL_MAX_SIZE = -1L;

    /**
     * 允许上传的最大文件大小的默认值
     */
    public static final Long DEFAULT_MAX_SIZE = 5 * 1024 * 1024L;

    private static final Map<String, OssFileConfig> ALL_BIZ_TYPE_MAP = new HashMap<>();

    static {
        ALL_BIZ_TYPE_MAP.put(BIZ_TYPE.AVATAR, new OssFileConfig(OssSavePlaceEnum.PUBLIC, IMG_SUFFIX, DEFAULT_MAX_SIZE));
        ALL_BIZ_TYPE_MAP.put(BIZ_TYPE.IF_BG, new OssFileConfig(OssSavePlaceEnum.PUBLIC, IMG_SUFFIX, DEFAULT_MAX_SIZE));
        ALL_BIZ_TYPE_MAP.put(BIZ_TYPE.CERT, new OssFileConfig(OssSavePlaceEnum.PRIVATE, new HashSet<>(Arrays.asList(ALL_SUFFIX_FLAG)), DEFAULT_MAX_SIZE));
    }

    /**
     * 存储位置
     */
    private OssSavePlaceEnum ossSavePlaceEnum;

    /**
     * 允许的文件后缀, 默认全部类型
     */
    private Set<String> allowFileSuffix;

    /**
     * 允许的文件大小, 单位： Byte
     */
    private Long maxSize = DEFAULT_MAX_SIZE;


    /**
     * 是否在允许的文件类型后缀内
     * @param fixSuffix
     * @return
     */
    public boolean isAllowFileSuffix(String fixSuffix) {

        //允许全部
        if (this.allowFileSuffix.contains(ALL_SUFFIX_FLAG)) {
            return true;
        }
        fixSuffix = StrUtil.blankToDefault(fixSuffix, "");
        return this.allowFileSuffix.contains(fixSuffix.toLowerCase());
    }

    /**
     * 是否在允许的大小范围内
     * @param fileSize
     * @return
     */
    public boolean isMaxSizeLimit(Long fileSize) {

        // 允许全部大小
        if (ALL_MAX_SIZE.equals(this.maxSize)) {
            return true;
        }

        return this.maxSize >= (fileSize == null ? 0L : fileSize);
    }


    public static OssFileConfig getOssFileConfigByBizType(String bizType) {
        return ALL_BIZ_TYPE_MAP.get(bizType);
    }

}
```

##### 3.3 系统Yml配置参数定义Bean

```java

@Data
@Configuration
@ConfigurationProperties(prefix = "isys")
public class OssYmlConfig {

    //指定该属性为嵌套值, 否则默认为简单值导致对象为空（外部类不存在该问题， 内部static需明确指定）
    @NestedConfigurationProperty
    private Oss oss;

    /** 系统oss配置信息 **/
    @Data
    public static class Oss {

        /** 存储根路径 **/
        private String fileRootPath;

        /** 公共读取块 **/
        private String filePublicPath;

        /** 私有读取块 **/
        private String filePrivatePath;

        /** oss类型 **/
        private String serviceType;

    }
}
```

##### 3.4 阿里云配置参数

```java

@Data
@Configuration
@ConfigurationProperties(prefix = "isys.oss.aliyun-oss")
public class AliyunOssYmlConfig {
    private String endpoint;
    private String publicBucketName;
    private String privateBucketName;
    private String accessKeyId;
    private String accessKeySecret;
}
```

#### 3.5 七牛云参数配置

```java

@Data
@Configuration
@ConfigurationProperties(prefix = "isys.oss.qiniu-oss")
public class QiniuOssYmlConfig {
    private String ak;
    private String sk;
    private String bucket;
}
```

#### 第四步: service

##### 4.1 接口定义

```java

/**
 * OSSService 接口
 */
public interface IOssService {

    /**
     * 上传文件 & 生成下载/预览URL
     *
     * @param ossSavePlaceEnum
     * @param multipartFile
     * @param saveDirAndFileName
     * @return
     */
    String upload2PreviewUrl(OssSavePlaceEnum ossSavePlaceEnum, MultipartFile multipartFile, String saveDirAndFileName);

    /**
     * 将文件下载到本地
     * 返回是否 写入成功
     * false: 写入失败， 或者文件不存在
     **/
    boolean downloadFile(OssSavePlaceEnum ossSavePlaceEnum, String source, String target);

}

```

##### 4.2 本地存储
```java
@Service
@Slf4j
@ConditionalOnProperty(name = "isys.oss.service-type", havingValue = "local")
public class LocalFileService implements IOssService {
    @Autowired
    private OssYmlConfig ossYmlConfig;

    /**
     * 将上传的文件进行保存
     * @param ossSavePlaceEnum
     * @param multipartFile
     * @param saveDirAndFileName
     * @return
     */
    @Override
    public String upload2PreviewUrl(OssSavePlaceEnum ossSavePlaceEnum, MultipartFile multipartFile, String saveDirAndFileName) {
        String path = null;
        try {

            String savePath = ossSavePlaceEnum == OssSavePlaceEnum.PUBLIC ?
                    ossYmlConfig.getOss().getFilePublicPath() : ossYmlConfig.getOss().getFilePrivatePath();
            path = savePath + File.separator + saveDirAndFileName;
            File saveFile = new File(path);

            //如果文件夹不存在则创建文件夹
            File dir = saveFile.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            multipartFile.transferTo(saveFile);

        } catch (Exception e) {
            log.error("", e);
        }

        // 私有文件 不返回预览文件地址
        if (ossSavePlaceEnum == OssSavePlaceEnum.PRIVATE) {
            return saveDirAndFileName;
        }

        return path;
    }

    @Override
    public boolean downloadFile(OssSavePlaceEnum ossSavePlaceEnum, String source, String target) {
        return false;
    }
}

```

##### 4.3 阿里云存储
```java
@Service
@Slf4j
@ConditionalOnProperty(name = "isys.oss.service-type", havingValue = "aliyun-oss")
public class AliyunOssService implements IOssService {
    @Autowired
    private AliyunOssYmlConfig aliyunOssYmlConfig;

    // ossClient初始化
    OSS ossClient = null;

    @PostConstruct
    public void init() {
        ossClient = new OSSClientBuilder().build(aliyunOssYmlConfig.getEndpoint(), aliyunOssYmlConfig.getAccessKeyId(), aliyunOssYmlConfig.getAccessKeySecret());

    }

    @Override
    public String upload2PreviewUrl(OssSavePlaceEnum ossSavePlaceEnum, MultipartFile multipartFile, String saveDirAndFileName) {
        try {
            this.ossClient.putObject(ossSavePlaceEnum == OssSavePlaceEnum.PUBLIC ? aliyunOssYmlConfig.getPublicBucketName() : aliyunOssYmlConfig.getPrivateBucketName()
                    , saveDirAndFileName, multipartFile.getInputStream());

            if (ossSavePlaceEnum == OssSavePlaceEnum.PUBLIC) {
                // 文档：https://www.alibabacloud.com/help/zh/doc-detail/39607.htm  example: https://BucketName.Endpoint/ObjectName
                return "https://" + aliyunOssYmlConfig.getPublicBucketName() + "." + aliyunOssYmlConfig.getEndpoint() + "/" + saveDirAndFileName;
            }

            return saveDirAndFileName;
        } catch (Exception e) {
            log.error("error", e);
            return null;
        }
    }

    @Override
    public boolean downloadFile(OssSavePlaceEnum ossSavePlaceEnum, String source, String target) {
        try {
            String bucket = ossSavePlaceEnum == OssSavePlaceEnum.PRIVATE ? aliyunOssYmlConfig.getPrivateBucketName() : aliyunOssYmlConfig.getPublicBucketName();
            this.ossClient.getObject(new GetObjectRequest(bucket, source), new File(target));

            return true;
        } catch (Exception e) {
            log.error("error", e);
            return false;
        }
    }
}

```

##### 4.4 七牛云云存储
```java
@Service
@Slf4j
@ConditionalOnProperty(name = "isys.oss.service-type", havingValue = "qiniu-oss")
public class QiniuOssService implements IOssService {

    @Autowired
    private QiniuOssYmlConfig qiqiuProperties;

    private UploadManager uploadManager;
    private String token;

    @PostConstruct
    public void init() {
        //构造一个带指定 Region 对象的配置类
        uploadManager = new UploadManager(new Configuration(Region.regionNa0()));
        token = Auth.create(qiqiuProperties.getAk(), qiqiuProperties.getSk()).
                uploadToken(qiqiuProperties.getBucket());
    }

    @Override
    public String upload2PreviewUrl(OssSavePlaceEnum ossSavePlaceEnum, MultipartFile multipartFile, String saveDirAndFileName) {
        Response response = null;
        try {
            response = uploadManager.put(multipartFile.getBytes(), saveDirAndFileName, token);
            //解析上传成功的结果
            log.info("【七牛云】 上传响应,data{}", JSONObject.parseObject(response.bodyString()));
            DefaultPutRet putRet = JSONObject.parseObject(response.bodyString(), DefaultPutRet.class);


            if (ossSavePlaceEnum == OssSavePlaceEnum.PUBLIC) {
                // 文档：https://developer.qiniu.com/kodo/1239/java#upload-flow
                // 七牛绑定的域名
                return "绑定域名" + "/" + saveDirAndFileName;
            }

            return saveDirAndFileName;
        } catch (Exception e) {
            log.error("error", e);
            return null;
        }


    }

    @Override
    public boolean downloadFile(OssSavePlaceEnum ossSavePlaceEnum, String source, String target) {
        return false;
    }
}

```


#### 第五步: controller

```java
@RestController
@RequestMapping("/api/ossFiles")
public class OssFileController {

    @Autowired
    private IOssService ossService;

    /**
     * 上传文件 （单文件上传）
     * @param file
     * @param bizType
     * @return
     */
    @PostMapping("/{bizType}")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable("bizType") String bizType) {
        if (file == null) {
            return "选择文件不存在";
        }

        OssFileConfig ossFileConfig = OssFileConfig.getOssFileConfigByBizType(bizType);

        //1. 判断bizType 是否可用
        if (ossFileConfig == null) {
            return "类型有误";
        }

        // 2. 判断文件是否支持
        String fileSuffix = FileUtil.extName(file.getOriginalFilename());
        if (!ossFileConfig.isAllowFileSuffix(fileSuffix)) {
            return "上传文件格式不支持";
        }

        // 3. 判断文件大小是否超限
        if (!ossFileConfig.isMaxSizeLimit(file.getSize())) {
            return "上传大小请限制在[\" + ossFileConfig.getMaxSize() / 1024 / 1024 + \"M]以内！";
        }

        // 新文件地址 (xxx/xxx.jpg 格式)
        String saveDirAndFileName = bizType + "/" + UUID.fastUUID() + "." + fileSuffix;
        return ossService.upload2PreviewUrl(ossFileConfig.getOssSavePlaceEnum(), file, saveDirAndFileName);

    }

}

```

### 六、阿里云对象存储(oss)之服务端签名后直传
> 基于Post Policy的使用规则在服务端通过各种语言代码完成签名，然后通过表单直传数据到OSS。由于服务端签名直传无需将AccessKey暴露在前端页面，相比JavaScript客户端签名直传具有更高的安全性。
代码:
```java

@Slf4j
@RestController
@RequestMapping("/")
class AliTokenController {

    private static final String DEFAULT_DIR = "default";

    @Resource
    private OSS oss;

    @PostMapping("/get-token")
    public JSONObject getToken() {

        // 过期时间默认5分钟
        Date expiration = DateUtil.offsetMinute(new Date(),5);


        try {
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem("bucket", "bucket值");
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1024 * 1024 * 1024);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, "dir文件夹");
            String postPolicy = oss.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = oss.calculatePostSignature(postPolicy);

        }catch (Exception e) {
            log.error("生成临时签名异常", e);
            throw new RuntimeException( "生成临时签名异常");
        }


        return new JSONObject();
    }

}

```

### [参考](https://blog.csdn.net/HaoGeCSDN2002/article/details/129471064)

```java
 
@SuppressWarnings("all")
@RestController
public class OssController {
    @Autowired
    OSS ossClient;
 
    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endoint;
 
    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;
 
    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;
 
    @RequestMapping("oss/policy")
    public R Policy() {
        // 填写Host地址，格式为https://bucketname.endpoint。
        String host = "https://" + bucket + "." + endoint;
        // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
        String format = new SimpleDateFormat("yyyy:MM:hh").format(new Date());
        String dir = format + "/";
        Map<String, String> respMap = null;
        try {
            long expireTime = 6000;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
 
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
 
            respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
 
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return R.ok().put("data", respMap);
    }
}
```
