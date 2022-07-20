package com.example.entity;

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
@TableName("t_promotion_seckill")
public class TPromotionSeckill implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ps_id", type = IdType.AUTO)
    private Long psId;

    private Integer goodsId;

    private Integer psCount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    /**
     * 0-未开始 1-进行中  2-已结束
     */
    private Integer status;

    private Float currentPrice;

    public Long getPsId() {
        return psId;
    }

    public void setPsId(Long psId) {
        this.psId = psId;
    }
    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
    public Integer getPsCount() {
        return psCount;
    }

    public void setPsCount(Integer psCount) {
        this.psCount = psCount;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Float getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Float currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public String toString() {
        return "TPromotionSeckill{" +
            "psId=" + psId +
            ", goodsId=" + goodsId +
            ", psCount=" + psCount +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", status=" + status +
            ", currentPrice=" + currentPrice +
        "}";
    }
}
