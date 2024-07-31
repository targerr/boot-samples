package com.example.rsa.hutool;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: wgs
 * @Date 2022/3/4 09:14
 * @Classname SplicingUtil
 * @Description
 */
public class SplicingUtil {
    /**
     * map转字符串
     *
     * @param map   集合
     * @param param 追加参数
     * @return
     */
    public static void mapToString(TreeMap<String, Object> map, StringBuffer param) {
        //循环集合
        for (String key : map.keySet()) {
            //值
            Object obj = map.get(key);
            //判断不同类型，执行不同参数转换
            if (obj instanceof List) {
                // 转list
                List<TreeMap<String, Object>> list = Convert.convert(List.class, obj);
                // 递归遍历
                for (Object m : list) {
                    mapToString(Convert.convert(TreeMap.class, m), param);
                }
            } else if (obj instanceof Map) {
                // 递归遍历
                mapToString(Convert.convert(TreeMap.class, obj), param);
            } else {
                //判断是否为空
                if (StrUtil.isNotBlank(Convert.toStr(obj))) {
                    //附值
                    param.append(Convert.toStr(obj));
                }
            }
        }
    }

    /**
     * Jackson
     * @param map
     * @param param
     */
    public static void mapToStringByJackson(TreeMap<String, Object> map, StringBuffer param) {
        //循环集合
        for (String key : map.keySet()) {
            //值
            Object obj = map.get(key);
            //判断不同类型，执行不同参数转换
            if (obj instanceof List) {
                // 转list
                List<TreeMap<String, Object>> list = JSONObject.parseObject(JSON.toJSONString(obj),List.class);
                // 递归遍历
                for (Object m : list) {
                    mapToStringByJackson(JSONObject.parseObject(JSON.toJSONString(m),TreeMap.class),param);
                }
            } else if (obj instanceof Map) {
                // 递归遍历
                mapToStringByJackson(JSONObject.parseObject(JSON.toJSONString(obj),TreeMap.class),param);
            } else if (obj instanceof String) {
                String str = (String) obj;
                if (StrUtil.isNotBlank(str)) {
                    //附值
                    param.append(str);
                }
            }
        }
    }
}
