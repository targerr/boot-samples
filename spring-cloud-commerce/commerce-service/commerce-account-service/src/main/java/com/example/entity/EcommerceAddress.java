package com.example.entity;

import cn.hutool.core.bean.BeanUtil;
import com.example.account.AddressInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: wgs
 * @Date 2022/11/15 10:58
 * @Classname EcommerceBalance
 * @Description 用户地址表实体类定义
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_ecommerce_address")
public class EcommerceAddress {
    /** 自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 用户 id */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** 用户名 */
    @Column(name = "username", nullable = false)
    private String username;

    /** 电话 */
    @Column(name = "phone", nullable = false)
    private String phone;

    /** 省 */
    @Column(name = "province", nullable = false)
    private String province;

    /** 市 */
    @Column(name = "city", nullable = false)
    private String city;

    /** 详细地址 */
    @Column(name = "address_detail", nullable = true)
    private String addressDetail;

    /** 创建时间 */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    /** 更新时间 */
    @LastModifiedDate
    @Column(name = "update_time", nullable = false)
    private Date updateTime;

    public static EcommerceAddress to(Long userId, AddressInfo.AddressItem addressItem) {

        EcommerceAddress address = new EcommerceAddress();
        BeanUtil.copyProperties(addressItem, address);
        address.setUserId(userId);
        address.setId(null);

        return address;
    }


    /**
     * <h2>将 EcommerceAddress 对象转成 AddressInfo</h2>
     * */
    public AddressInfo.AddressItem toAddressItem() {

        AddressInfo.AddressItem addressItem = new AddressInfo.AddressItem();

        addressItem.setId(this.id);
        addressItem.setUsername(this.username);
        addressItem.setPhone(this.phone);
        addressItem.setProvince(this.province);
        addressItem.setCity(this.city);
        addressItem.setAddressDetail(this.addressDetail);
        addressItem.setCreateTime(this.createTime);
        addressItem.setUpdateTime(this.updateTime);

        return addressItem;
    }
}
