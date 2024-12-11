package com.example.webstater.base.exception;

import com.example.webstater.base.enums.ResultErrorCode;
import lombok.Getter;

/**
 * description: jwt解析异常类
 *
 */
@Getter
public class JwtParseException extends BaseUncheckedException {

    private static final long serialVersionUID = 6898087804057803400L;

    public JwtParseException() {
        super(ResultErrorCode.JSON_TRANSFORM_ERROR);
    }

    public JwtParseException(String errorMsg) {
        super(ResultErrorCode.JSON_TRANSFORM_ERROR.getErrorCode(), errorMsg);
    }


    public JwtParseException(String message,
            Throwable cause) {
        super(ResultErrorCode.JSON_TRANSFORM_ERROR.getErrorCode(), message, cause);
    }
}
