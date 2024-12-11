package com.example.webstater.base.exception;

import com.example.webstater.base.enums.IErrorCode;
import com.example.webstater.base.enums.ResultErrorCode;
import lombok.Getter;

/**
 * description: 业务异常类
 *
 */
@Getter
public class BizException extends BaseUncheckedException {

    private static final long serialVersionUID = -3238517855583910821L;


    public BizException(IErrorCode errorCode) {
        super(errorCode.getErrorCode(), errorCode.getErrorMsg());
    }

    public BizException(IErrorCode errorCode, String errorMsg) {
        super(errorCode.getErrorCode(), errorMsg);
    }

    public BizException(String code, String message) {
        super(code, message);
    }

    public BizException(String message) {
        super(ResultErrorCode.FAILURE.getErrorCode(), message);
    }
}
