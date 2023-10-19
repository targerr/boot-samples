package com.example;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.common.TableId;
import com.example.dao.EcommerceGoodsDao;
import com.example.entity.EcommerceGoods;
import com.example.enums.BrandCategory;
import com.example.enums.GoodsCategory;
import com.example.enums.GoodsStatus;
import com.example.goods.DeductGoodsInventory;
import com.example.goods.GoodsInfo;
import com.example.service.GoodsService;
import com.example.service.async.AsyncTaskManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: wgs
 * @Date 2022/11/18 14:30
 * @Classname GoodsServiceTest
 * @Description 商品微服务测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class GoodsServiceTest {
    @Autowired
    private AsyncTaskManager asyncTaskManager;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private EcommerceGoodsDao ecommerceGoodsDao;
    @Autowired
    private GoodsService goodsService;

    @Test
    public void testSaveGoods() {

        EcommerceGoods goods = new EcommerceGoods();

        goods.setGoodsPic("手机");
        goods.setGoodsName("小米手机");
        goods.setGoodsCategory(GoodsCategory.DIAN_QI);
        goods.setGoodsStatus(GoodsStatus.OFFLINE);
        goods.setGoodsProperty("{}");
        goods.setGoodsDescription("测试");
        goods.setBrandCategory(BrandCategory.BRAND_A);
        goods.setInventory(10L);
        goods.setPrice(20);
        goods.setSupply(30L);

        ecommerceGoodsDao.save(goods);
    }

    @Test
    public void testImportGoods() {

        JSONArray jsonArray = JSONArray.parseArray(getJsonArray());
        List<GoodsInfo> goodsInfos = jsonArray.toJavaList(GoodsInfo.class);
        log.info("goodsInfos:{}", JSON.toJSON(goodsInfos));

        String taskId = asyncTaskManager.submitTask(goodsInfos).getTaskId();

        ThreadUtil.sleep(2000L);
        log.info("异步任务执行结果:{}", JSON.toJSON(asyncTaskManager.getTaskInfo(taskId)));
    }


    @Test
    public void testGetGoodsInfoByTableId() {

        List<Long> ids = Arrays.asList(15L, 2L, 3L);
        List<TableId.Id> tIds = ids.stream()
                .map(TableId.Id::new).collect(Collectors.toList());
        log.info("根据商品id查询:{}",
                JSONObject.toJSONString(goodsService.getGoodsInfoByTableId(new TableId(tIds)), true)
        );

    }


    @Test
    public void testGetSimpleGoodsInfoByPage() {
        log.info("分页查询商品数据: {}",
                JSONObject.toJSONString(goodsService.getSimpleGoodsInfoByPage(1), true)
        );
    }


    @Test
    public void testGetSimpleGoodsInfoByTableId() {

        List<Long> ids = Arrays.asList(14L, 15L);
        List<TableId.Id> tids = ids.stream().map(TableId.Id::new).collect(Collectors.toList());

        log.info("获取简单商品数据: {}",
                JSONObject.toJSONString(goodsService.getSimpleGoodsInfoByTableId(new TableId(tids)))
        );
    }

    @Test
    public void testDeductGoodsInventory() {

        List<DeductGoodsInventory> deductGoodsInventories = Arrays.asList(
                new DeductGoodsInventory(15L, 10),
                new DeductGoodsInventory(16L, 10)
        );

        log.info("扣减库存: {}",
                goodsService.deductGoodsInventory(deductGoodsInventories));

    }

    private String getJsonArray() {
        String jsonArray = "[\n" +
                "    {\n" +
                "        \"goodsCategory\":\"10001\",\n" +
                "        \"brandCategory\":\"20001\",\n" +
                "        \"goodsName\":\"iphone 11\",\n" +
                "        \"goodsPic\":\"\",\n" +
                "        \"goodsDescription\":\"苹果手机\",\n" +
                "        \"price\":100000,\n" +
                "        \"supply\":2000000,\n" +
                "        \"inventory\":\"30\",\n" +
                "        \"goodsProperty\":{\n" +
                "            \"size\":\"12cm * 6.5cm\",\n" +
                "            \"color\":\"绿色\",\n" +
                "            \"material\":\"金属机身\",\n" +
                "            \"pattern\":\"纯色\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"goodsCategory\":\"10001\",\n" +
                "        \"brandCategory\":\"20001\",\n" +
                "        \"goodsName\":\"iphone 12\",\n" +
                "        \"goodsPic\":\"\",\n" +
                "        \"goodsDescription\":\"苹果手机\",\n" +
                "        \"price\":150000,\n" +
                "        \"supply\":2000000,\n" +
                "        \"inventory\":\"30\",\n" +
                "        \"goodsProperty\":{\n" +
                "            \"size\":\"12cm * 6.5cm\",\n" +
                "            \"color\":\"绿色\",\n" +
                "            \"material\":\"金属机身\",\n" +
                "            \"pattern\":\"纯色\"\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"goodsCategory\":\"10001\",\n" +
                "        \"brandCategory\":\"20001\",\n" +
                "        \"goodsName\":\"iphone 13\",\n" +
                "        \"goodsPic\":\"\",\n" +
                "        \"goodsDescription\":\"苹果手机\",\n" +
                "        \"price\":160000,\n" +
                "        \"supply\":2000000,\n" +
                "        \"inventory\":\"30\",\n" +
                "        \"goodsProperty\":{\n" +
                "            \"size\":\"12cm * 6.5cm\",\n" +
                "            \"color\":\"绿色\",\n" +
                "            \"material\":\"金属机身\",\n" +
                "            \"pattern\":\"纯色\"\n" +
                "        }\n" +
                "    }\n" +
                "]";
        return jsonArray;
    }

}
