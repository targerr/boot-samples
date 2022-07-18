package com.example.service.getui;

import com.alibaba.fastjson.JSONObject;
import com.example.exception.PushException;
import com.example.param.SendRequest;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.api.UserApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.CommonEnum;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.CidAliasListDTO;
import com.getui.push.v2.sdk.dto.req.message.PushChannel;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.AndroidDTO;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.getui.push.v2.sdk.dto.req.message.android.ThirdNotification;
import com.getui.push.v2.sdk.dto.req.message.android.Ups;
import com.getui.push.v2.sdk.dto.req.message.ios.Alert;
import com.getui.push.v2.sdk.dto.req.message.ios.Aps;
import com.getui.push.v2.sdk.dto.req.message.ios.IosDTO;
import com.getui.push.v2.sdk.dto.res.QueryCidResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: wgs
 * @Date 2022/7/18 14:57
 * @Classname GetuiService
 * @Description
 */
@Slf4j
@Service
public class GetuiService {
    @Autowired
    private PushApi pushApi;
    @Autowired
    private UserApi userApi;

    /**
     * cid 推送
     *
     * @param sendRequest
     */
    protected void pushToSingleByCid(SendRequest sendRequest) {
        PushDTO<Audience> pushDTO = pushDTO(sendRequest);
        fullCid(pushDTO, sendRequest.getCid());
        // 进行cid单推
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
        log.debug("【个推】请求报文:{}", JSONObject.toJSONString(pushDTO, true));
        if (apiResult.isSuccess()) {
            // success
            log.debug("【个推】推送响应参数 data: {}", JSONObject.toJSONString(apiResult, true));
            return;
        }
        // failed
        log.debug("【个推】推送失败响应参数 data: {}", JSONObject.toJSONString(apiResult, true));
        throw new PushException(104, "【个推】推送失败响应" + JSONObject.toJSONString(apiResult));

    }

    /**
     * 别名推送
     *
     * @param sendRequest
     */
    public void pushToSingleByAlias(SendRequest sendRequest) {
        batchUnboundAlias(sendRequest.getCid(), sendRequest.getUserId());
        bindAlias(sendRequest.getCid(), sendRequest.getUserId());

        PushDTO<Audience> pushDTO = pushDTO(sendRequest);
        //fullAlias(pushDTO, sendRequest.getAlias());
        fullAlias(pushDTO, sendRequest.getUserId());
        // 别名单推
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByAlias(pushDTO);
        log.debug("【个推】别名推送 请求参数:{} 响应参数: {}", JSONObject.toJSONString(pushDTO, true), JSONObject.toJSONString(apiResult, true));
        if (apiResult.isSuccess()) {
            // success
        }
    }

    /**
     * 文档
     * https://docs.getui.com/getui/server/rest_v2/common_args/
     *
     * @param sendRequest
     * @return
     */
    private PushDTO<Audience> pushDTO(SendRequest sendRequest) {
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        // 设置推送参数
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        /**** 设置个推通道参数 *****/
        PushMessage pushMessage = new PushMessage();
        pushDTO.setPushMessage(pushMessage);
        GTNotification notification = new GTNotification();
        notification.setTitle(sendRequest.getMessageParam().getTitle());
        notification.setBody(sendRequest.getMessageParam().getBody());
//        notification.setClickType(CommonEnum.ClickTypeEnum.TYPE_STARTAPP.type);
        notification.setClickType(CommonEnum.ClickTypeEnum.TYPE_PAYLOAD.type);

        JSONObject json = new JSONObject();
        json.put("type", "1");
        json.put("title", sendRequest.getMessageParam().getTitle());
        json.put("msgId", "1501468370309742593");
        notification.setPayload(json.toJSONString());
        notification.setUrl("https://www.getui.com");
        notification.setChannelLevel("3");


        pushMessage.setNotification(notification);
        //pushMessage.setTransmission("透传消息测试");


        /**** 设置厂商相关参数 ****/
        PushChannel pushChannel = new PushChannel();
        pushDTO.setPushChannel(pushChannel);
        /*配置安卓厂商参数*/
        buildAndroid(pushChannel, sendRequest);
        /*设置ios厂商参数*/
        buildIos(pushChannel, sendRequest);

        return pushDTO;
    }

    private List<PushDTO<Audience>> pushDTOList(SendRequest sendRequest) {
        List<PushDTO<Audience>> audiences = new ArrayList<>();
        List<String> cids = Arrays.asList(sendRequest.getCid().split(",")).stream().collect(Collectors.toList());
        for (String cid : cids) {
            sendRequest.setCid(cid);
            final PushDTO<Audience> audiencePushDTO = this.pushDTO(sendRequest);
            fullAlias(audiencePushDTO, sendRequest.getUserId());
            audiences.add(audiencePushDTO);

            bindAlias(cid, sendRequest.getUserId());

        }
        return audiences;


    }

    private void buildIos(PushChannel pushChannel, SendRequest sendRequest) {
        IosDTO iosDTO = new IosDTO();
        pushChannel.setIos(iosDTO);
        // 相同的collapseId会覆盖之前的消息
        iosDTO.setApnsCollapseId(System.currentTimeMillis() + "");
        Aps aps = new Aps();
        iosDTO.setAps(aps);
        Alert alert = new Alert();
        aps.setAlert(alert);
        alert.setTitle(sendRequest.getMessageParam().getTitle());
        alert.setBody(sendRequest.getMessageParam().getBody());
    }

    private void buildAndroid(PushChannel pushChannel, SendRequest sendRequest) {
        AndroidDTO androidDTO = new AndroidDTO();
        pushChannel.setAndroid(androidDTO);
        Ups ups = new Ups();
//        ups.setTransmission("透传消息");

        ThirdNotification thirdNotification = new ThirdNotification();
        thirdNotification.setClickType(CommonEnum.ClickTypeEnum.TYPE_STARTAPP.type);
        thirdNotification.setTitle("title-" + System.currentTimeMillis());
        thirdNotification.setBody("content");
        ups.setNotification(thirdNotification);

        androidDTO.setUps(ups);

    }

    /**
     * 绑定别名
     *
     * @param cid
     * @param alias
     */
    public void bindAlias(String cid, String alias) {
        CidAliasListDTO cidAliasListDTO = new CidAliasListDTO();
        cidAliasListDTO.add(new CidAliasListDTO.CidAlias(cid, alias));
        ApiResult<Void> apiResult = userApi.bindAlias(cidAliasListDTO);
        log.debug("【个推】绑定别名 data: {}", JSONObject.toJSONString(apiResult, true));
    }

    /**
     * 根据别名查询cid
     */
    public void queryCidByAlias(String alias) {
        ApiResult<QueryCidResDTO> apiResult = userApi.queryCidByAlias(alias);
        log.debug("【个推】根据别名查询 \n allias:{} data: {}", alias, JSONObject.toJSONString(apiResult, true));
    }

    /**
     * 解绑别名
     *
     * @param cid
     * @param alias
     */
    public void batchUnboundAlias(String cid, String alias) {
        CidAliasListDTO cidAliasListDTO = new CidAliasListDTO();
        cidAliasListDTO.add(new CidAliasListDTO.CidAlias(cid, alias));
        ApiResult<Void> apiResult = userApi.batchUnbindAlias(cidAliasListDTO);
        log.debug("【个推】解绑别名 data: {}", JSONObject.toJSONString(apiResult, true));
    }

    public void bindAlias(List<String> cids, String alias) {
        CidAliasListDTO cidAliasListDTO = new CidAliasListDTO();
        if (cids != null && cids.isEmpty()) {
            cids.forEach(e -> cidAliasListDTO.add(new CidAliasListDTO.CidAlias(e, alias)));
        }
        ApiResult<Void> apiResult = userApi.bindAlias(cidAliasListDTO);
        log.debug("【个推】绑定别名 data: {}", JSONObject.toJSONString(apiResult, true));
    }

    /**
     * 解绑别名
     *
     * @param alias
     */
    public void batchUnboundAlias(String alias) {
        ApiResult<Void> unbindAllAlias = userApi.unbindAllAlias(alias);

        log.debug("【个推】解绑别名 data: {}", unbindAllAlias);
    }

    private void fullCid(PushDTO<Audience> pushDTO, String cid) {
        Audience audience = new Audience();
        audience.addCid(cid);
        pushDTO.setAudience(audience);
    }

    private void fullAlias(PushDTO<Audience> pushDTO, String alias) {
        Audience audience = new Audience();
        audience.addAlias(alias);
        pushDTO.setAudience(audience);
    }

}
