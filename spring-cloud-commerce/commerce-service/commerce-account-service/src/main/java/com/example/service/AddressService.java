package com.example.service;

import com.example.account.AddressInfo;
import com.example.common.TableId;

/**
 * @Author: wgs
 * @Date 2022/11/15 15:06
 * @Classname AddressService
 * @Description 用户地址相关服务接口定义
 */
public interface AddressService {
    /**
     * <h2>创建用户地址信息</h2>
     * */
    TableId createAddressInfo(AddressInfo addressInfo);

    /**
     * <h2>获取当前登录的用户地址信息</h2>
     * */
    AddressInfo getCurrentAddressInfo();

    /**
     * <h2>通过 id 获取用户地址信息, id 是 EcommerceAddress 表的主键</h2>
     * */
    AddressInfo getAddressInfoById(Long id);

    /**
     * <h2>通过 TableId 获取用户地址信息</h2>
     * */
    AddressInfo getAddressInfoByTableId(TableId tableId);
}
