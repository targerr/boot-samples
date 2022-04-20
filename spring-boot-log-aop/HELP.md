## spring-boot-log-aop

> 演示用户操作日志,具体代码见 demo。

### 注解

- BusinessLog

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BusinessLog {

    /**
     * 业务名称
     *
     * @return
     */
    String value() default "";

    /**
     * 用户行为
     *
     * @return
     */
    CommonEnum.LoggerTypeEnum behavior();

}

```

### 切面
- LoggerAspect

```java
@Aspect
@Component
@Slf4j
public class LoggerAspect {

    /**
     * 切点
     *
     * @param operation
     */
    @Pointcut(value = "@annotation(operation)")
    public void pointcut(BusinessLog operation) {
    }


    /**
     * 环绕操作
     *
     * @param point 切入点
     * @return 原方法返回值
     * @throws Throwable 异常信息
     */
    @Around(value = "pointcut(operation)", argNames = "point,operation")
    public Object aroundLog(ProceedingJoinPoint point, BusinessLog operation) throws Throwable {

        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        // 打印请求相关参数
        long startTime = System.currentTimeMillis();
        Object result = point.proceed();
        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgentUtil.parse(header);

        Log l = buildLog(point, request, startTime, result, header, userAgent);

        log.info("Request Log Info : {}\n operation:{}", JSONUtil.toJsonStr(l), operation.behavior().getLogDesc());

        return result;
    }

    /**
     * 构建日志
     *
     */
    private Log buildLog(ProceedingJoinPoint point, HttpServletRequest request, long startTime, Object result, String header, UserAgent userAgent) {
        final Log l = Log.builder()
                .threadId(Convert.toStr(Thread.currentThread().getId()))
                .threadName(Thread.currentThread().getName())
                .ip(ServletUtil.getClientIP(request))
                .url(request.getRequestURL().toString())
                .classMethod(String.format("%s.%s", point.getSignature().getDeclaringTypeName(),
                        point.getSignature().getName()))
                .httpMethod(request.getMethod())
                .requestParams(getNameAndValue(point))
                .result(result)
                .timeCost(System.currentTimeMillis() - startTime)
                .userAgent(header)
                .browser(userAgent.getBrowser().toString())
                .os(userAgent.getOs().getName())
                .build();
        return l;
    }


    /**
     * 获取名称和值
     *
     * @param joinPoint 连接点
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    private Map<String, Object> getNameAndValue(ProceedingJoinPoint joinPoint) {

        final Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        final String[] names = methodSignature.getParameterNames();
        final Object[] args = joinPoint.getArgs();

        if (ArrayUtil.isEmpty(names) || ArrayUtil.isEmpty(args)) {
            return Collections.emptyMap();
        }
        if (names.length != args.length) {
            log.warn("{}方法参数名和参数值数量不一致", methodSignature.getName());
            return Collections.emptyMap();
        }
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], args[i]);
        }
        return map;
    }

}

```

### TestController

```java
@RestController
@RequestMapping("/aop")
public class TestController {
    @GetMapping("/test")
    @BusinessLog(behavior = CommonEnum.LoggerTypeEnum.SELECT)
    public String test(String result) {
        return result;
    }


    @PostMapping("/testJson")
    @BusinessLog(behavior = CommonEnum.LoggerTypeEnum.SELECT)
    public Dict testJson(@RequestBody Map<String, Object> map) {
        final String jsonStr = JSONUtil.toJsonStr(map);
        Console.error(jsonStr);
        return Dict.create().set("json", map);
    }
}

```
