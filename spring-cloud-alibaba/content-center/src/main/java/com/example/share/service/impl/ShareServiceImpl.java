package com.example.share.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.share.dto.share.ShareDTO;
import com.example.share.dto.user.UserDTO;
import com.example.share.entity.Share;
import com.example.share.mapper.ShareMapper;
import com.example.share.service.IShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wgs
 * @since 2022-08-19
 */
@Service
@Slf4j
public class ShareServiceImpl extends ServiceImpl<ShareMapper, Share> implements IShareService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public ShareDTO findById(String id) {
        Share share = getById(id);
        String userId = share.getUserId();

        UserDTO userDTO = buildUserV1(userId);

        // 装配ShareDTO对象
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share,shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO;
    }

    private UserDTO buildUserV1(String userId) {
        List<ServiceInstance> instanceList = discoveryClient.getInstances("user-center");
        String targetUri = instanceList.stream()
                // 数据转换
                .map(instance -> instance.getUri().toString()+"/user/{id}").findFirst()
                // 如果实例为空者报错
                .orElseThrow(() -> new IllegalArgumentException("url不存在!"));

        log.info("【请求地址】 --> {}",targetUri);
        // 获取发布人姓名
        UserDTO userDTO = restTemplate.getForObject(targetUri, UserDTO.class, userId);
        return userDTO;
    }
}
