package com.example.share.service.impl;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.feignclient.UserCenterFeignClient;
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
    @Autowired
    private UserCenterFeignClient client;

    @Override
    public ShareDTO findById(String id) {
        Share share = getById(id);
        String userId = share.getUserId();

        // restTemplate
        //UserDTO userDTO = buildUserV1(userId);
        // 使用Feign负载均衡
        UserDTO userDTO = client.findById(userId);

        // 装配ShareDTO对象
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());
        return shareDTO;
    }

    @Override
    public ShareDTO detail(String id) {
        Share share = getById(id);
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        return shareDTO;
    }

    @SentinelResource(value = "sayHello",blockHandler = "exceptionHandler",fallback = "")
    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }

    public String exceptionHandler(String name,Throwable e){
        e.printStackTrace();
        log.error("【sentienl】 异常处理!");
        return "异常，请稍后";
    }

    public String exceptionHandler(BlockException e){
        e.printStackTrace();
        log.error("【sentienl】 流控!");
        return "系统繁忙，请稍后";
    }


    private UserDTO buildUserV1(String userId) {
        List<ServiceInstance> instanceList = discoveryClient.getInstances("user-center");
        String targetUri = instanceList.stream()
                // 数据转换
                .map(instance -> instance.getUri().toString() + "/user/{id}").findFirst()
                // 如果实例为空者报错
                .orElseThrow(() -> new IllegalArgumentException("url不存在!"));

        log.info("【请求地址】 --> {}", targetUri);
        // 获取发布人姓名
        UserDTO userDTO = restTemplate.getForObject(targetUri, UserDTO.class, userId);
        return userDTO;
    }
}
