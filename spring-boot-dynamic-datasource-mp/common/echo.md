### 字典回显

#### 使用

1. 注入拦截器

```java
@SpringBootApplication
@MapperScan("com.example.mapper")
public class SpringBootMybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisPlusApplication.class, args);
    }

    // 字典回显
    @Bean
    public EchoDataInterceptor echoResultInterceptor(ApplicationContext applicationContext, EchoProperties echoProperties) {
        return new EchoDataInterceptor(applicationContext, echoProperties);
    }
    
}

```

2. 实现LoadService

```java
@Service
public class VersionService implements LoadService {
    @Override
    public Map<Serializable, Object> findByIds(Set<Serializable> ids) {

        Map<Serializable, Object> result = new HashMap<Serializable, Object>();
        result.put("version###1","版本号1.0");

        return result;
    }

    @Override
    public Map<Serializable, Object> findByIdsAndFilter(Set<Serializable> ids) {
        return null;
    }
}

```

3. 实体类添加注解 EchoField

```java
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "city")
public class City implements Serializable {
    /**
     * 序列
     */
    @TableId
    private String id;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 市代码
     */
    private String cityCode;

    /**
     * 说明
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createDateTime;

    /**
     * 创建人
     */
    private String createName;

    /**
     * 修改时间
     */
    private Date modifyDateTime;

    /**
     * 修改人
     */
    private String modifyName;

    /**
     * 删除状态（0：未删除，1：删除）
     */
    private Boolean isDelete;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 状态
     */
    @EchoField(ref = "stateName", dictType = "state", api = "cityTypeService")
    private Integer state;

    @TableField(exist = false)
    private String stateName;

    /**
     * 标签
     */
    private String label;


    /**
     * 排序
     */
    private Integer sorting;

    /**
     * 版本号
     */
    @EchoField(ref = "versionName", dictType = "version", api = "versionService")
    private Integer version;

    @TableField(exist = false)
    private String versionName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}
```
