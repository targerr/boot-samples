## spring-boot-exception-handler

> 演示统一异常处理,具体代码见 demo。

### 自定义异常

- ServiceException

```java

@Getter
public class ServiceException extends RuntimeException {
    private Integer code;

    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();

    }
}

```

### 异常处理类

```java

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public ResultVO handlerException(ServiceException e) {
        return ResultVO.error(e.getCode(), e.getMessage());
    }
}
```

### 运行结果

- http://127.0.0.1:8080/handler/1

```json
{
  code: 1,
  msg: "参数不正确",
  data: null
}
```
