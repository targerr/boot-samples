package com.example.chain.process;

import com.example.chain.pipeline.BusinessProcess;
import com.example.chain.pipeline.ProcessModel;
import lombok.Data;

import java.util.List;

/**
 * @Author: wgs
 * @Date 2022/7/15 10:29
 * @Classname ProcessTemplate
 * @Description 业务执行模板
 */
@Data
public class ProcessTemplate {
    private List<BusinessProcess> processList;
}
