package com.example.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.example.constant.GoodsConstant;
import com.example.dao.EcommerceGoodsDao;
import com.example.common.TableId;
import com.example.entity.EcommerceGoods;
import com.example.goods.DeductGoodsInventory;
import com.example.goods.GoodsInfo;
import com.example.goods.SimpleGoodsInfo;
import com.example.service.GoodsService;
import com.example.vo.PageSimpleGoodsInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: wgs
 * @Date 2022/11/18 11:05
 * @Classname GoodsServiceImpl
 * @Description 商品服务接口实现
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class GoodsServiceImpl implements GoodsService {

    private final StringRedisTemplate redisTemplate;
    private final EcommerceGoodsDao ecommerceGoodsDao;

    public GoodsServiceImpl(StringRedisTemplate redisTemplate,
                            EcommerceGoodsDao ecommerceGoodsDao) {
        this.redisTemplate = redisTemplate;
        this.ecommerceGoodsDao = ecommerceGoodsDao;
    }

    @Override
    public List<GoodsInfo> getGoodsInfoByTableId(TableId tableId) {

        // 详细商品信息，不能从 redis cache 中获取
        List<Long> ids = tableId.getIds().stream().
                map(TableId.Id::getId).collect(Collectors.toList());
        log.info("获取商品id集合:{}", JSONObject.toJSONString(ids));

        List<EcommerceGoods> ecommerceGoods = IterableUtils.toList(ecommerceGoodsDao.findAllById(ids));

        return ecommerceGoods.stream()
                .map(EcommerceGoods::toGoodsInfo).collect(Collectors.toList());
    }

    @Override
    public PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int page) {

        if (page <= 1) {
            page = 1;
        }

        // 自定义分页规则
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        Page<EcommerceGoods> goodsPage = ecommerceGoodsDao.findAll(pageable);

        // 是否还有更多页: 总页数是否大于当前给定的页
        boolean hasMore = goodsPage.getTotalPages() > page;

        return PageSimpleGoodsInfo.builder()
                .simpleGoodsInfos(goodsPage.getContent().stream()
                        .map(EcommerceGoods::toSimple).collect(Collectors.toList()))
                .hasMore(hasMore)
                .build();
    }

    @Override
    public List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId) {

        // 先从 redis 中获取，如果没有从 db 查询，然后同步到 redis
        List<Object> goodIds = tableId.getIds().stream()
                .map(i -> i.getId().toString()).collect(Collectors.toList());

        // FIXME 如果 cache 中查不到 goodsId 对应的数据, 返回的是 null, [null, null]
        List<Object> cachedSimpleGoodsInfos = redisTemplate.opsForHash()
                .multiGet(GoodsConstant.ECOMMERCE_GOODS_DICT_KEY, goodIds)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 如果 Redis 中查不到数据
        if (CollectionUtil.isEmpty(cachedSimpleGoodsInfos)) {
            return queryGoodsFromDBAndCacheToRedis(tableId);
        }

        // 如果 Redis 中查到了商品，分为两种情况操作。
        // 1. 缓存中查询出所有的数据
        if (cachedSimpleGoodsInfos.size() == goodIds.size()) {
            log.info("根据 ids 获取缓存数据: {}", JSONObject.toJSONString(cachedSimpleGoodsInfos));
            return parseCacheGoodsInfo(cachedSimpleGoodsInfos);
        }
        // 2. 一半数据库中获取（right）;一半从 cache redis 中获取(left)
        List<SimpleGoodsInfo> left = parseCacheGoodsInfo(cachedSimpleGoodsInfos);

        // 取差集： 传递的参数 - 缓存查出 = 缓存没有的（数据库查询）
        List<Long> subtractIds = CollectionUtil.subtractToList(
                goodIds.stream().map(Convert::toLong).collect(Collectors.toList()),
                left.stream().map(SimpleGoodsInfo::getId).collect(Collectors.toList())
        );

        // 缓存中没有的, 查询数据表并缓存
        List<SimpleGoodsInfo> right = queryGoodsFromDBAndCacheToRedis(
                new TableId(subtractIds.stream().map(TableId.Id::new)
                        .collect(Collectors.toList()))
        );
        log.info("获取简单商品并且缓存到 redis data:{}", JSONObject.toJSONString(right));

        // 合并返回
        return new ArrayList<>(CollectionUtil.unionAll(right, left));
    }


    @Override
    public Boolean deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories) {

        // 校验参数
        deductGoodsInventories.forEach(d -> {
            if (d.getCount() <= 0) {
                throw new RuntimeException("商品扣减个数大于0");
            }
        });

        // 查询商品
        List<EcommerceGoods> ecommerceGoods = IterableUtils.toList(ecommerceGoodsDao.findAllById(
                deductGoodsInventories.stream().map(DeductGoodsInventory::getGoodsId).collect(Collectors.toList())
        ));
        // 根据传递 goodIds 查询不到商品。抛异常
        if (CollectionUtil.isEmpty(ecommerceGoods)) {
            throw new RuntimeException("扣减商品不存在!");
        }

        // 根据传递参数查询出来的数量不一致。抛异常
        if (ecommerceGoods.size() != deductGoodsInventories.size()) {
            throw new RuntimeException("查询商品数量,与传递参数不一致!");
        }

        // goodId -> DeductGoodsInventory
        Map<Long, DeductGoodsInventory> goods2Inventory = deductGoodsInventories.stream()
                .collect(Collectors.toMap(DeductGoodsInventory::getGoodsId, Function.identity()));

        // 检查是否可以扣减，再扣除
        ecommerceGoods.forEach(g -> {
            Long currentInventory = g.getInventory();
            Integer needDeductInventory = goods2Inventory.get(g.getId()).getCount();
            if (currentInventory < needDeductInventory) {
                log.error("商品库存不正确 库存:{},需要扣除数量:{}", currentInventory, needDeductInventory);
                throw new RuntimeException("商品库存数量不够");
            }
            // 设置扣减库存
            g.setInventory(currentInventory - needDeductInventory);
            log.info("商品id:{},商品数据:{},商品库存:{}", g.getId(), currentInventory, g.getInventory());
        });

        ecommerceGoodsDao.saveAll(ecommerceGoods);
        log.info("扣减商品库存完成");

        return true;
    }


    /**
     * 从 DB 从查询，然后同步到 redis
     *
     * @param tableId
     * @return
     */
    private List<SimpleGoodsInfo> queryGoodsFromDBAndCacheToRedis(TableId tableId) {

        List<Long> ids = tableId.getIds().stream().map(TableId.Id::getId).collect(Collectors.toList());
        log.info("数据库要查询的id:{}", JSONObject.toJSONString(ids));

        // 根据 id 查询
        List<SimpleGoodsInfo> simpleGoodsInfos = IterableUtils.toList(ecommerceGoodsDao.findAllById(ids)).stream()
                .map(EcommerceGoods::toSimple).collect(Collectors.toList());
        // 结果缓存，下次直接从缓存获取
        Map<String, String> id2JsonObject = new HashMap<>(simpleGoodsInfos.size());
        simpleGoodsInfos.forEach(e ->
                id2JsonObject.put(e.getId().toString(), JSONObject.toJSONString(e))
        );
        log.info("缓存到 Redis 数据: {}", JSONObject.toJSONString(id2JsonObject));

        // 保存到 Redis 中
        redisTemplate.opsForHash().putAll(
                GoodsConstant.ECOMMERCE_GOODS_DICT_KEY, id2JsonObject);

        return simpleGoodsInfos;
    }

    /**
     * 从缓存中解析成为简单商品
     *
     * @param cacheSimpleGoodsInfo
     * @return
     */
    private List<SimpleGoodsInfo> parseCacheGoodsInfo(List<Object> cacheSimpleGoodsInfo) {

        return cacheSimpleGoodsInfo.stream()
                .map(e -> JSONObject.parseObject(e.toString(), SimpleGoodsInfo.class))
                .collect(Collectors.toList());
    }

}
