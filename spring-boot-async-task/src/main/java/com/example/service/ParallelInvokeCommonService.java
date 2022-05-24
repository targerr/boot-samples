package com.example.service;

import com.example.dto.BaseRspDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author: wgs
 * @Date 2022/5/24 10:53
 * @Classname ExecuteTask
 * @Description
 */
@Service
public class ParallelInvokeCommonService {
    public List<BaseRspDTO<Object>> executeTask(List<Callable<BaseRspDTO<Object>>> taskList,ExecutorService executor) {

        List<BaseRspDTO<Object>> resultList = new ArrayList<>();
        //校验参数
        if (taskList == null || taskList.size() == 0) {
            return resultList;
        }

        CompletionService<BaseRspDTO<Object>> baseDTOCompletionService = new ExecutorCompletionService<BaseRspDTO<Object>>(executor);
        //提交任务
        for (Callable<BaseRspDTO<Object>> task : taskList) {
            baseDTOCompletionService.submit(task);
        }

        try {
            //遍历获取结果
            for (int i = 0; i < taskList.size(); i++) {
                Future<BaseRspDTO<Object>> baseRspDTOFuture = baseDTOCompletionService.poll(10, TimeUnit.SECONDS);
                resultList.add(baseRspDTOFuture.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
