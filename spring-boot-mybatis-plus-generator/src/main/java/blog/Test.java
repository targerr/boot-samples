package blog;

import common.EntityBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.*;

/**
 * @Author: wgs
 * @Date 2022/6/14 14:46
 * @Classname Test
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`insurance_docking_danger_planted`")
@ApiModel(value="InsuranceDockingDangerPlanted对象", description="保险对接险种")
public class Test extends EntityBase {
    @ApiModelProperty(value = "保险险种id")
    private String dangerPlantedId;

    @ApiModelProperty(value = "请求地址")
    private String address;

    @ApiModelProperty(value = "类型（1：投保接口，10：支付申请，20：退保申请，30：理赔申请，40：发票申请）")
    private Integer type;

    @ApiModelProperty(value = "状态(0:农民工 1：投标 2：履约)")
    private Integer state;

    @ApiModelProperty(value = "发票类型（0：无2：电子 3：纸质）")
    private Integer invoiceType;

    @ApiModelProperty(value = "标签")
    private String label;
}
