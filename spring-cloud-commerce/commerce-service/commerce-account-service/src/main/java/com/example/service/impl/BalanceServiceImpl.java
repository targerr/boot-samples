package com.example.service.impl;

import com.example.account.BalanceInfo;
import com.example.dao.EcommerceBalanceDao;
import com.example.entity.EcommerceBalance;
import com.example.filter.AccessContext;
import com.example.service.BalanceService;
import com.example.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: wgs
 * @Date 2022/11/15 11:57
 * @Classname BalanceServiceImpl
 * @Description
 */
@Service
@Slf4j
public class BalanceServiceImpl implements BalanceService {
    private final EcommerceBalanceDao ecommerceBalanceDao;

    public BalanceServiceImpl(EcommerceBalanceDao ecommerceBalanceDao) {
        this.ecommerceBalanceDao = ecommerceBalanceDao;
    }

    @Override
    public BalanceInfo getCurrentUserBalanceInfo() {

        // 当前用户信息
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        BalanceInfo balanceInfo = new BalanceInfo(loginUserInfo.getId(), 0L);

        EcommerceBalance ecommerceBalance = ecommerceBalanceDao.findByUserId(loginUserInfo.getId());
        if (ecommerceBalance != null) {
            balanceInfo.setBalance(ecommerceBalance.getBalance());
        } else {
            // 没有余额记录，就创建出来
            createBalance(loginUserInfo);
        }

        return balanceInfo;
    }


    @Override
    public BalanceInfo deductBalance(BalanceInfo balanceInfo) {

        // 当前用户信息
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        EcommerceBalance ecommerceBalance = ecommerceBalanceDao.findByUserId(loginUserInfo.getId());
        // 原则：扣减余额小于用户余额
        if (ecommerceBalance == null || ecommerceBalance.getBalance() - balanceInfo.getBalance() < 0) {
            throw new RuntimeException("余额不够!");
        }

        Long sourceBalance = ecommerceBalance.getBalance();
        ecommerceBalance.setBalance(ecommerceBalance.getBalance() - balanceInfo.getBalance());
        ecommerceBalanceDao.save(ecommerceBalance);
        log.info("用户扣减余额用户Id:{},原始余额:{},扣减后余额:{}", loginUserInfo.getId(), sourceBalance, ecommerceBalance.getBalance());

        return new BalanceInfo(loginUserInfo.getId(), ecommerceBalance.getBalance());
    }


    private void createBalance(LoginUserInfo loginUserInfo) {
        EcommerceBalance newBalance = new EcommerceBalance();
        newBalance.setUserId(loginUserInfo.getId());
        newBalance.setBalance(0L);

        ecommerceBalanceDao.save(newBalance);
        log.info("新增用户余额记录: [{}]", newBalance.getId());
    }
}
