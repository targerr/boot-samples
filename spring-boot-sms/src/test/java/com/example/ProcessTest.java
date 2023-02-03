package com.example;

import cn.hutool.core.collection.ListUtil;
import com.example.chain.domain.MessageParam;
import com.example.chain.domain.SendTaskModel;
import com.example.chain.domain.TaskInfo;
import com.example.chain.pipeline.ProcessContext;
import com.example.chain.pipeline.ProcessModel;
import com.example.chain.process.ProcessController;
import com.example.chain.vo.BasicResultVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * @Author: wgs
 * @Date 2022/7/15 10:59
 * @Classname ProcessTest
 * @Description
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProcessTest {
    @Autowired
    private ProcessController processController;

    @Test
    public void send() {
        MessageParam param = MessageParam.builder().build();

        List<TaskInfo> taskInfo = new ArrayList<>();

        Set<String> set = new HashSet<>();
        set.add("18806513887");
        taskInfo.add(TaskInfo.builder()
                .sendChannel(1)
                .receiver(set)
                .build()
        );


        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(2L)
                .messageParamList(Arrays.asList(param))
                .taskInfo(taskInfo)
                .build();

        ProcessContext<ProcessModel> context = ProcessContext.builder()
                .code("send")
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResultVO.success()).build();

        ProcessContext<ProcessModel> process = processController.process(context);

        System.out.println(process);
    }
}
