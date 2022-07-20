package seckill.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wgs
 * @since 2022-07-20
 */
@TableName("t_order")
public class TOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    private String orderNo;

    private Integer orderStatus;

    private String userid;

    private String recvName;

    private String recvAddress;

    private String recvMobile;

    private Float postage;

    private Float amout;

    private LocalDateTime createTime;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getRecvName() {
        return recvName;
    }

    public void setRecvName(String recvName) {
        this.recvName = recvName;
    }
    public String getRecvAddress() {
        return recvAddress;
    }

    public void setRecvAddress(String recvAddress) {
        this.recvAddress = recvAddress;
    }
    public String getRecvMobile() {
        return recvMobile;
    }

    public void setRecvMobile(String recvMobile) {
        this.recvMobile = recvMobile;
    }
    public Float getPostage() {
        return postage;
    }

    public void setPostage(Float postage) {
        this.postage = postage;
    }
    public Float getAmout() {
        return amout;
    }

    public void setAmout(Float amout) {
        this.amout = amout;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "TOrder{" +
            "orderId=" + orderId +
            ", orderNo=" + orderNo +
            ", orderStatus=" + orderStatus +
            ", userid=" + userid +
            ", recvName=" + recvName +
            ", recvAddress=" + recvAddress +
            ", recvMobile=" + recvMobile +
            ", postage=" + postage +
            ", amout=" + amout +
            ", createTime=" + createTime +
        "}";
    }
}
