package com.example.utils;

import com.example.vo.ResVo;
import lombok.Data;

/**
 * @Author: wgs
 * @Date 2023/10/13 14:43
 * @Classname ResultVOUtil
 * @Description
 */
@Data
public class ResultVoUtil {
    public static ResVo success(Object object) {
        ResVo resultVO = new ResVo();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(object);
        return resultVO;
    }

    public static ResVo success() {
        return success(null);
    }

    public static ResVo error(Integer code, String msg) {
        ResVo resultVO = new ResVo();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }
}
