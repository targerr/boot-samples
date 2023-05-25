package com.example.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.account.AddressInfo;
import com.example.common.TableId;
import com.example.dao.EcommerceAddressDao;
import com.example.entity.EcommerceAddress;
import com.example.filter.AccessContext;
import com.example.service.AddressService;
import com.example.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: wgs
 * @Date 2022/11/15 15:07
 * @Classname AddressServiceImpl
 * @Description 用户地址相关服务接口实现
 */
@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final EcommerceAddressDao addressDao;

    public AddressServiceImpl(EcommerceAddressDao addressDao) {
        this.addressDao = addressDao;
    }

    /**
     * 存储多个地址信息
     *
     * @param addressInfo
     * @return
     */
    @Override
    public TableId createAddressInfo(AddressInfo addressInfo) {

        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        // 参数转化为 EcommerceAddress 实体类
        List<EcommerceAddress> ecommerceAddresses = addressInfo.getAddressItems().stream()
                .map(a -> EcommerceAddress.to(loginUserInfo.getId(), a)).collect(Collectors.toList());

        // 批量保存返回 id
        final List<EcommerceAddress> addressList = addressDao.saveAll(ecommerceAddresses);
        List<TableId.Id> ids = addressList.stream().map(ecommerceAddress -> new TableId.Id(ecommerceAddress.getId())).collect(Collectors.toList());
        log.info("批量插入地址信息: {}", JSONObject.toJSONString(addressList));

        return new TableId(ids);
    }

    @Override
    public AddressInfo getCurrentAddressInfo() {
        // 当前用户
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        // 查找用户地址
        final List<EcommerceAddress> addresses = addressDao.findAllByUserId(loginUserInfo.getId());
        if (CollectionUtil.isEmpty(addresses)) {
            return new AddressInfo();
        }

        // 转换
        List<AddressInfo.AddressItem> addressItems = addresses.stream()
                .map(EcommerceAddress::toAddressItem).collect(Collectors.toList());

        return new AddressInfo(loginUserInfo.getId(), addressItems);
    }

    @Override
    public AddressInfo getAddressInfoById(Long id) {

        EcommerceAddress ecommerceAddress = addressDao.findById(id).orElse(null);
        if (ecommerceAddress == null) {
            throw new RuntimeException("地址不存在!");
        }

        return new AddressInfo(
                ecommerceAddress.getUserId(),
                Collections.singletonList(ecommerceAddress.toAddressItem())
        );
    }

    @Override
    public AddressInfo getAddressInfoByTableId(TableId tableId) {
        List<Long> ids = tableId.getIds().stream()
                .map(TableId.Id::getId).collect(Collectors.toList());
        log.info("批量查询用户地址: {}", JSONObject.toJSONString(ids));

        List<EcommerceAddress> ecommerceAddress = addressDao.findAllById(ids);

        // 转换
        List<AddressInfo.AddressItem> addressItems = ecommerceAddress.stream()
                .map(EcommerceAddress::toAddressItem).collect(Collectors.toList());

        return new AddressInfo(
                ecommerceAddress.get(0).getUserId(), addressItems
        );
    }
}
