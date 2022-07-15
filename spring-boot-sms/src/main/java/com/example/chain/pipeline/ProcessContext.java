package com.example.chain.pipeline;

import com.example.chain.vo.BasicResultVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author: wgs
 * @Date 2022/7/14 16:14
 * @Classname ProcessContext
 * @Description 责任链上下文
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class ProcessContext<T extends ProcessModel> {
    /**
     * 标识责任链的code
     */
    private String code;

    /**
     * 存储责任链上下文数据的模型AssembleAction
     */
    private T processModel;


    /**
     * 责任链中断的标识
     */
    private Boolean needBreak;

    /**
     * 流程处理的结果
     */
    RespData respData;

    /**
     * 流程处理的结果
     */
    BasicResultVO response;
}
