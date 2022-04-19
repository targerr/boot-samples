package com.example.vo;

import lombok.Data;

/**
 * @Author: wgs
 * @Date 2022/4/19 10:37
 * @Classname ResultVO
 * @Description * http请求返回最外层对象
 */
@Data
public class ResultVO<T> {
    //错误码
    private Integer code;
    // 错误信息
    private String msg;
    // 返回数据
    private T data;

    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(object);
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
