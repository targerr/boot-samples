package com.example.service.async;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.convert.impl.MapConverter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.constant.GoodsConstant;
import com.example.dao.EcommerceGoodsDao;
import com.example.entity.EcommerceGoods;
import com.example.goods.GoodsInfo;
import com.example.goods.SimpleGoodsInfo;
import com.example.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @Author: wgs
 * @Date 2022/11/18 11:19
 * @Classname AsyncServiceImpl
 * @Description 异步接口服务实现
 */
@Slf4j
@Service
@Transactional
public class AsyncServiceImpl implements AsyncService {

    private final EcommerceGoodsDao ecommerceGoodsDao;
    private final StringRedisTemplate redisTemplate;

    public AsyncServiceImpl(EcommerceGoodsDao ecommerceGoodsDao,
                            StringRedisTemplate redisTemplate) {
        this.ecommerceGoodsDao = ecommerceGoodsDao;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 异步任务需要加上注解，并指定线程池明
     * 异步任务处理两件事：
     * 1.商品落盘
     * 2.更新缓存
     *
     * @param goodsInfos
     * @param taskId
     */
    @Async("getAsyncExecutor")
    @Override
    public void asyncImportGoods(List<GoodsInfo> goodsInfos, String taskId) {

        log.info("异步线程运行,taskId:{}", taskId);

        // 秒表工具
        StopWatch watch = StopWatch.createStarted();

        // 1. 如果 goodsInfos 存在重复商品，不保存；直接返回，记录日志
        // 标记是否合法
        boolean isIllegal = false;

        // 将商品信息字段 joint 在一起, 用来判断是否存在重复
        Set<String> goodsJointInfos = new HashSet<>(goodsInfos.size());
        // 过滤出来的, 可以入库的商品信息(规则按照自己的业务需求自定义即可)
        List<GoodsInfo> filteredGoodsInfo = new ArrayList<>(goodsInfos.size());

        // 过滤非法参数，请求是否合法
        for (GoodsInfo goods : goodsInfos) {
            if (goods.getPrice() <= 0 || goods.getSupply() <= 0) {
                log.error("商品不合法:{}", JSONObject.toJSONString(goods));
                continue;
            }

            // 组合商品，用于去重
            String joinInfo = String.format("%s,%s.%s",
                    goods.getGoodsCategory(), goods.getBrandCategory(),
                    goods.getGoodsName());

            if (goodsJointInfos.contains(joinInfo)) {
                isIllegal = true;
            }

            // 放入容器
            goodsJointInfos.add(joinInfo);
            filteredGoodsInfo.add(goods);
        }

        // 存在重复商品或者商品为空，打印日志直接返回
        if (isIllegal || CollectionUtil.isEmpty(filteredGoodsInfo)) {
            watch.stop();
            log.warn("导入商品数据不存在: [{}]", JSON.toJSONString(filteredGoodsInfo));
            log.info("校验商品花费时间: [{}ms]",
                    watch.getTime(TimeUnit.MILLISECONDS));
            return;
        }

        List<EcommerceGoods> ecommerceGoods = filteredGoodsInfo.stream().map(
                EcommerceGoods::to
        ).collect(Collectors.toList());

        // 最终要存入的商品数据
        List<EcommerceGoods> targetGoods = new ArrayList<>(ecommerceGoods.size());

        // 2.过滤出重复商品

        ecommerceGoods.forEach(g -> {
            EcommerceGoods dbGoods = ecommerceGoodsDao.findFirst1ByGoodsCategoryAndBrandCategoryAndGoodsName(g.getGoodsCategory(), g.getBrandCategory(), g.getGoodsName()).orElse(null);
            if (dbGoods != null) {
                return;
            }

            targetGoods.add(g);
        });

        log.info("落库商品数据:{}", JSONObject.toJSONString(targetGoods));
        // 保持商品
        List<EcommerceGoods> savedGoods = IterUtil.toList(ecommerceGoodsDao.saveAll(targetGoods));
        // 将入库商品信息同步到 Redis 中
        saveNewGoodsInfoToRedis(savedGoods);

        log.info("商品保存 redis 数量: [{}]", savedGoods.size());

        watch.stop();
        log.info("保存到商品时间: [{}ms]", watch.getTime(TimeUnit.MILLISECONDS));
    }

    /**
     * 将数据持久化到redis
     * dict: key -> <id, SimpleGoodsInfo(json)>
     *
     * @param savedGoods
     */
    private void saveNewGoodsInfoToRedis(List<EcommerceGoods> savedGoods) {

        // 由于 redis 存储在内存，所以存储简单商品信息
        List<SimpleGoodsInfo> simpleGoodsInfos = savedGoods.stream().
                map(EcommerceGoods::toSimple).collect(Collectors.toList());

        Map<String, String> id2JsonObject = new HashMap<>(simpleGoodsInfos.size());
        simpleGoodsInfos.forEach(e -> {
            id2JsonObject.put(e.getId().toString(), JSONObject.toJSONString(e));
        });

        // 保存到 Redis 中
        redisTemplate.opsForHash().putAll(
                GoodsConstant.ECOMMERCE_GOODS_DICT_KEY,
                id2JsonObject
        );
    }
}
