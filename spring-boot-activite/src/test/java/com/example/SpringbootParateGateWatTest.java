package com.example;

/**
 * Created by jxlhl on 2021/8/18.
 * https://juejin.cn/post/6997652315373043749
 */

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootParateGateWatTest {

    //得到RepositoryService实例
    @Autowired
    private RepositoryService repositoryService;

    //得到RuntimeService实例
    @Autowired
    private RuntimeService runtimeService;

    //得到TaskService实例
    @Autowired
    private TaskService taskService;

    //
    @Autowired
    private HistoryService historyService;

    private static final String definition_key = "gateway-v1";

    //0.流程部署，单个文件部署方式
    @Test
    public void testDeployment(){

        //使用RepositoryService进行部署
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addClasspathResource("process/gateway-v1.bpmn20.xml");
        builder.name(definition_key);
        Deployment deployment = builder.deploy();

        //输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());

//        流程部署id：b7eb2e9c-18b3-11ee-baf5-369ec563b70c
//        流程部署名称：gateway-v1

    }

    //1.流程实例启动
    @Test
    public void testStartProcess(){
        Map<String,Object> param = new HashMap<>();
        param.put("name", "tom");
        //根据流程定义Id启动流程
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(definition_key);

        //输出实例信息
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
        System.out.println("当前活动Id：" + processInstance.getActivityId());
//
//        流程定义id：gateway-v1:1:b7f9867e-18b3-11ee-baf5-369ec563b70c
//        流程实例id：d9fef079-18b3-11ee-8446-c2e19c5cfd0e
//        当前活动Id：null
    }

    //2.任务查询
    //流程启动后，任务的负责人就可以查询自己当前需要处理的任务，查询出来的任务都是该用户的待办任务。
    @Test
    public void testFindPersonalTaskList() {
        //任务负责人
        String assignee = "林";

        //根据流程key 和 任务负责人 查询任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(definition_key)
                .taskAssignee(assignee)
                .list();

        for (Task task : list) {

            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());

        }

//        流程实例id：d9fef079-18b3-11ee-8446-c2e19c5cfd0e
//        任务id：da01617d-18b3-11ee-8446-c2e19c5cfd0e
//        任务负责人：zhangsan
//        任务名称：并行张三请假
    }

    //流程启动后，任务的负责人就可以查询自己当前需要处理的任务，查询出来的任务都是该用户的待办任务。
    @Test
    public void testTaskList() {

        //根据流程key 和 任务负责人 查询任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(definition_key)
                .list();

        for (Task task : list) {

            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());

        }

    }

    //3.完成任务
    @Test
    public void completTask(){

        //任务负责人
        String assignee = "zhangsan";
        //根据流程key和任务的负责人查询任务并选择其中的一个任务处理
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(definition_key) //流程Key
                .taskAssignee(assignee)  //要查询的负责人
                .singleResult();

        //完成任务,参数：任务id
        taskService.complete(task.getId());

    }

    //流程结束，或流程流转过程中的历史信息查询
    @Test
    public void findHistoryInfo(){

        //获取 actinst表的查询对象
        HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
        //查询 actinst表，条件：根据 InstanceId 查询
        instanceQuery.processInstanceId("d9fef079-18b3-11ee-8446-c2e19c5cfd0e");
        //增加排序操作,orderByHistoricActivityInstanceStartTime 根据开始时间排序 asc 升序
        instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        //查询所有内容
        List<HistoricActivityInstance> activityInstanceList = instanceQuery.list();
        //输出结果
        for (HistoricActivityInstance hi : activityInstanceList) {

            System.out.println("");
            System.out.println("===================-===============");
            System.out.println(hi.getStartTime());
            System.out.println(hi.getAssignee());
            System.out.println(hi.getActivityId());
            System.out.println(hi.getActivityName());
            System.out.println(hi.getProcessDefinitionId());
            System.out.println(hi.getProcessInstanceId());
            System.out.println("===================-===============");
            System.out.println("");

        }

    }

    //流程定义信息查询
    @Test
    public void queryProcessDefinition(){

        //得到ProcessDefinitionQuery对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        //查询出当前所有的流程定义
        List<ProcessDefinition> definitionList = processDefinitionQuery.processDefinitionKey(definition_key)
                .orderByProcessDefinitionVersion()
                .desc()
                .list();

        //打印结果
        for (ProcessDefinition processDefinition : definitionList) {
            System.out.println("流程定义 id="+processDefinition.getId());
            System.out.println("流程定义 name="+processDefinition.getName());
            System.out.println("流程定义 key="+processDefinition.getKey());
            System.out.println("流程定义 Version="+processDefinition.getVersion());
            System.out.println("流程部署ID ="+processDefinition.getDeploymentId());
        }

    }

    //删除流程
    @Test
    public void deleteDeployment(){

        String deploymentId = "125098e1-ffd9-11eb-8847-02004c4f4f50";

        //删除流程定义，如果该流程定义已有流程实例启动则删除时出错
        repositoryService.deleteDeployment(deploymentId);

        //设置true 级联删除流程定义，即使该流程有流程实例启动也可以删除，设置为false非级别删除方式，如果流程
        //repositoryService.deleteDeployment(deploymentId, true);

    }

    //1.流程实例启动
    @Test
    public void testQueryProcessStatue(){

        String processInstanceId = "39d83eab-18be-11ee-bbd6-c2e19c5cfd0e";

        //根据流程定义Id启动流程
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (Objects.isNull(processInstance)){
            System.out.println("流程已经结束!");
            return;
        }

        System.out.println("流程进行中~~~~~");

        //输出实例信息
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
        System.out.println("当前活动Id：" + processInstance.getActivityId());
    }

}
