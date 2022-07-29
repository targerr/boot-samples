package com.example.param;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 钉钉群 自定义机器人返回的结果
 * 正常的返回：{"errcode":0,"errmsg":"ok"}
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public class DingDingRobotResult {
    /**
     * errcode
     */
    @JSONField(name = "errcode")
    private Integer errCode;

    /**
     * errmsg
     */
    @JSONField(name = "errmsg")
    private String errMsg;
}
