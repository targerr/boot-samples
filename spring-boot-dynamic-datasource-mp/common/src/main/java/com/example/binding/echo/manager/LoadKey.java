package com.example.binding.echo.manager;

import com.example.binding.EchoField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/**
 * description: 封装 EchoField 注解中解析出来的参数
 * <p>
 * 必须重写该类的 equals() 和 hashCode() 便于Map操作
 * </p>
 *
 * @author zhouxinlei
 * @since 2022-10-14 16:27:51
 */
@Data
@NoArgsConstructor
@ToString
public class LoadKey {

    /**
     * 执行查询任务的类
     * <p/>
     * api()  和 feign() 任选其一,  使用 api时，请填写实现类， 使用feign时，填写接口即可 如： @Echo(api="userServiceImpl") 等价于 @Echo(feign=UserService.class) 如：
     *
     * @EchoField(api="userController") 等价于 @Echo(feign=UserApi.class)
     */
    private String api;

    public LoadKey(EchoField echoField) {
        this.api = echoField.api();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoadKey that = (LoadKey) o;
        return Objects.equals(api, that.api);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(api);
    }
}
