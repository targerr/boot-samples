package com.example.webstater.base.exception;

import com.example.webstater.base.enums.IErrorCode;
import com.example.webstater.base.enums.ResultErrorCode;

/**
 * description: 登录过期异常
 *
 */
public class TokenExpireException extends BaseUncheckedException {

    public TokenExpireException(Throwable cause) {
        super(cause);
    }

    public TokenExpireException(String errorMsg) {
        super(ResultErrorCode.LOGIN_EXPIRE.getErrorCode(), errorMsg);
    }

    public TokenExpireException(IErrorCode errorCode) {
        super(errorCode.getErrorCode(), errorCode.getErrorMsg());
    }

    @Override
    public String toString() {
        return "TokenExpireException [message=" + getMessage() + ", code=" + getErrorCode() + "]";
    }


}
