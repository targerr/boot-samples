package com.example.chain.process;

import com.example.chain.pipeline.BusinessProcess;
import com.example.chain.pipeline.ProcessContext;
import com.example.chain.pipeline.ProcessModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wgs
 * @Date 2022/7/15 10:33
 * @Classname ProcessController
 * @Description
 */
@Slf4j
@Data
public class ProcessController {
    private Map<String, ProcessTemplate> templateConfig = new HashMap<String, ProcessTemplate>();

    public ProcessContext<ProcessModel> process(ProcessContext context) {
        List<BusinessProcess> businessProcesses = templateConfig.get(context.getCode()).getProcessList();

        for (BusinessProcess business : businessProcesses) {
            business.process(context);
            if (context.getNeedBreak()) {
                break;
            }
        }
        return context;
    }
}
