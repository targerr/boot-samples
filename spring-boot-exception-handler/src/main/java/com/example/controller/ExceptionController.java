package com.example.controller;

import com.example.enums.ResultEnum;
import com.example.excetpion.ServiceException;
import com.example.vo.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2022/4/19 10:32
 * @Classname ControllerExceptionHandler
 * @Description
 */
@RestController
@RequestMapping("/handler")
public class ExceptionController {
    @GetMapping("/{id:\\d+}")
    public ResultVO getDetail(@PathVariable String id) {
        throw new ServiceException(ResultEnum.PARAM_ERROR);
    }
}
