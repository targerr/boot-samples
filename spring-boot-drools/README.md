## 规则引擎

### 理解规则引擎

```java

//此处为伪代码

//根据用户输入信息确定信用卡额度
public Integer determineCreditCardLimit(User user){
    //如果申请人有房有车，或者月收入在20000以上，那么发放的信用卡额度为15000
    if((user.getHouse() != null && user.getcar() != null) 
       || user.getSalary() > 20000){
        return 15000;
    }
    //如果申请人没房没车，并且月收入在10000到20000之间，那么发放的信用卡额度为6000
    else if(user.getHouse() == null 
       && user.getcar() == null
       && user.getSalary() > 10000 
       && user.getSalary() < 20000){
        return 6000;
    }
    //如果申请人没房没车，并且月收入在10000以下，那么发放的信用卡额度为3000
    else if(user.getHouse() == null 
       && user.getcar() == null
       && user.getSalary() < 10000){
        return 3000;
    }
    //如果申请人有房没车或者没房但有车，并且月收入在10000以下，那么发放的信用卡额度为5000
    else if((((user.getHouse() != null && user.getcar() == null) || (user.getHouse() == null && user.getcar() != null))
       && user.getSalary() < 10000){
        return 5000;
    }
    //如果申请人有房没车或者没房但有车，并且月收入在10000到20000之间，那么发放的信用卡额度为8000
    else if((((user.getHouse() != null && user.getcar() == null) || (user.getHouse() == null && user.getcar() != null))
       && (user.getSalary() > 10000 && user.getSalary() < 20000)){
        return 8000;
    }
}
```
##### 通过上面的伪代码我们可以看到，我们的业务规则是通过Java代码的方式实现的。这种实现方式存在如下问题：
- 硬编码实现业务规则难以维护

- 硬编码实现业务规则难以应对变化

- 业务规则发生变化需要修改代码，重启服务后才能生效

> 规则引擎的主要思想是将应用程序中的业务决策部分分离出来，并使用预定义的语义模块编写业务决策（业务规则），由用户或开发者在需要时进行配置、管理。
#### 系统中引入规则引擎后，业务规则不再以程序代码的形式驻留在系统中，取而代之的是处理规则的规则引擎，业务规则存储在规则库中，完全独立于程序。业务人员可以像管理数据一样对业务规则进行管理，比如查询、添加、更新、统计、提交业务规则等。业务规则被加载到规则引擎中供应用系统调用。
脚本化

```

import com.example.springbootdrools.controller.OrderRequest;
import com.example.springbootdrools.model.CustomerType;
global com.example.springbootdrools.model.OrderDiscount orderDiscount;

dialect "mvel"

rule "Age based discount"
    when
        OrderRequest(age < 20 || age > 50)
    then
        System.out.println("==========Adding 10% discount for Kids/ senior customer=============");
        orderDiscount.setDiscount(orderDiscount.getDiscount() + 10);
end

rule "Customer type based discount - Loyal customer"
    when
        OrderRequest(customerType.getValue == "LOYAL")
    then
        System.out.println("==========Adding 5% discount for LOYAL customer=============");
        orderDiscount.setDiscount(orderDiscount.getDiscount() + 5);
end

rule "Customer type based discount - others"
    when
        OrderRequest(customerType.getValue != "LOYAL")
    then
        System.out.println("==========Adding 3% discount for NEW or DISSATISFIED customer=============");
        orderDiscount.setDiscount(orderDiscount.getDiscount() + 3);
end

rule "Amount based discount"
    when
        OrderRequest(amount > 1000L)
    then
        System.out.println("==========Adding 5% discount for amount more than 1000$=============");
        orderDiscount.setDiscount(orderDiscount.getDiscount() + 5);
end
```

### 使用规则引擎的优势
- 业务规则与系统代码分离，实现业务规则的集中管理

- 在不重启服务的情况下可随时对业务规则进行扩展和维护

- 可以动态修改业务规则，从而快速响应需求变更

- 规则引擎是相对独立的，只关心业务规则，使得业务分析人员也可以参与编辑、维护系统的业务规则

- 减少了硬编码业务规则的成本和风险

- 使用规则引擎提供的规则编辑工具，使复杂的业务规则实现变得的简单规则引擎应用场景

#### 规则引擎应用场景
- 对于一些存在比较复杂的业务规则并且业务规则会频繁变动的系统比较适合使用规则引擎，如下：

- 风险控制系统----风险贷款、风险评估

- 反欺诈项目----银行贷款、征信验证

- 决策平台系统----财务计算

- 促销平台系统----满减、打折、加价购

### 工作流与规则引擎有什么不同？

![image.png](https://upload-images.jianshu.io/upload_images/4994935-c8f6af5817a18885.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 二者关系：
1. 共同目标：工作流引擎和规则引擎都致力于提高业务效率，减少人工操作和错误，以及确保业务流程的一致性。它们的共同目标是通过自动化来降低成本和提高生产力。
2. 非编码用户友好：两者都旨在让非技术人员能够管理和改进业务流程，而无需深入了解编程。这使得企业内部的各种团队和部门能够参与流程改进，从而实现更广泛的自动化 
3. 相互整合：在实际应用中，工作流引擎和规则引擎通常一起使用。工作流引擎可以根据事先定义的流程指导任务的执行，而规则引擎可以用于制定决策规则，以便在工作流程中执行。这种协同工作可以实现更复杂的流程自动化，同时确保规则的合规性。
### 不同点：
1. 功能目的：工作流引擎旨在自动化和标准化多步骤任务的执行，这些任务通常以有序的方式完成以完成一个特定的任务或过程。这意味着它们更适合处理连续的、多阶段的工作流程，如审批流程或项目管理。规则引擎则专注于自动化基于规则的决策，这些规则可以涉及到复杂的条件、计算和数据分析。
2. 适用范围：工作流引擎通常用于通用和相对简单的流程，其中每个步骤通常是预定义的，可以按照既定的流程图执行。规则引擎更适合于复杂的、分层次的过程，通常涉及到大量的变量和条件。规则引擎的主要目标是根据规则的评估做出决策，而工作流引擎更关注执行任务和任务之间的协调。
3. 代码分离：规则引擎的代码通常是独立于工作流引擎的代码的。这使得规则引擎可以被应用到多个不同的工作流程中，而无需改变工作流引擎的结构。有时，它们甚至可以是不同的产品，可以独立部署和维护。
4. 规则应用：工作流引擎使用规则来指导流程中的任务执行，但规则引擎更专注于处理和应用规则。规则引擎能够处理更复杂的规则，包括基于数据分析的决策、动态生成的规则和计算。这使得规则引擎非常适合需要更高级决策制定的场景。
5. 发现位置：工作流引擎通常作为工作流软件的一部分，而规则引擎通常作为企业应用程序的一部分。规则引擎被集成到现有的业务应用程序中，以帮助这些应用程序更智能地处理数据和决策。 
