## springboot整合 validator

### 第一步：spring-boot-validate

~~~xml

<dependencies>

</dependencies>
~~~

### 第二步：创建application.yml文件



### 第三步： 配置类


### 第四步： validate 分组

#### 1. AddGroup 

```java
public interface AddGroup {
}

```

#### 2. EditGroup
```java

/**
 * 校验分组 edit
 */
public interface EditGroup {
}

```
#### 3. QueryGroup
```java
/**
 * 校验分组 query
 */
public interface QueryGroup {
}

```

### 第五步：创建ValidatorUtils

~~~java
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorUtils {
    private static final Validator VALID = SpringUtil.getBean(Validator.class);

    public static <T> void validate(T object,Class<?>... groups) {
        Set<ConstraintViolation<T>> validate = VALID.validate(object,groups);

        if (!validate.isEmpty()){
            throw new ConstraintViolationException("参数校验异常", validate);
        }
    }
}


~~~

### 第六步：创建测试

#### 1. bo类

```java
@Data
public class TestDemoBo {
    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long deptId;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long userId;

    /**
     * 排序号
     */
    @NotNull(message = "排序号不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer orderNum;

    /**
     * key键
     */
    @NotBlank(message = "key键不能为空", groups = {AddGroup.class, EditGroup.class})
    private String testKey;

    /**
     * 值
     */
    @NotBlank(message = "值不能为空", groups = {AddGroup.class, EditGroup.class})
    private String value;

}


```

#### 2.  controller

```java
@RestController
@RequestMapping("/validator")
public class ValidatorController {

    @PostMapping("/add")
    public Dict add(@RequestBody TestDemoBo bo) {
        // 使用校验工具校验 @Validated(AddGroup.class) 注解
        // 用于非 controller 的地方校验对象
        ValidatorUtils.validate(bo, AddGroup.class);
        return Dict.create().set("msg", "success");
    }

    @PutMapping("/edit")
    public Dict edit(@Validated(EditGroup.class) @RequestBody TestDemoBo bo){
        return Dict.create().set("msg", "success");
    }
}

```

[优雅进行字段校验](https://mp.weixin.qq.com/s/WAXnevAlT4p8CkCOAG4FZA)

[参数校验](https://mp.weixin.qq.com/s/1FFkwZo_-yHNvjsRJSQkZA)

[@Validated 和 @Valid 的区别](https://mp.weixin.qq.com/s/Uvl1b_cxubnhROilxytaRQ)

