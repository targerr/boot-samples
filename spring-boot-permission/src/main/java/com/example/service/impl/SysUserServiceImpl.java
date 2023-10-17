package com.example.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.UserInfoDTO;
import com.example.entity.SysUser;
import com.example.enums.ResultEnum;
import com.example.exception.PreException;
import com.example.service.SysUserService;
import com.example.mapper.SysUserMapper;
import com.example.service.help.UserPwdEncoder;
import com.example.service.help.UserSessionHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Objects;

/**
 * @author wanggaoshuai
 * @description 针对表【sys_user】的数据库操作Service实现
 * @createDate 2023-10-17 14:47:42
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private UserPwdEncoder userPwdEncoder;
    @Resource
    private UserSessionHelper userSessionHelper;

    @Override
    public UserInfoDTO login(String username, String password) {
        SysUser user = getSysUserByUserName(username);
        if (Objects.isNull(user)) {
            throw new PreException(ResultEnum.USER_NOT_EXISTS);
        }

        if (!userPwdEncoder.match(password, user.getPassword())) {
            throw new PreException(ResultEnum.USER_PWD_ERROR);
        }
        String toke = userSessionHelper.genSession(user.getId());
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setToken(toke);
        return userInfoDTO;
    }

    private SysUser getSysUserByUserName(String username) {
        LambdaQueryWrapper<SysUser> queryUser = Wrappers.lambdaQuery();
        queryUser.eq(SysUser::getUsername, username);
        SysUser user = sysUserMapper.selectOne(queryUser);
        return user;
    }
}




