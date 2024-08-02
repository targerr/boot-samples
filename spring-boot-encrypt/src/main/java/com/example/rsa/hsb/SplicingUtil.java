package com.example.rsa.hsb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class SplicingUtil {

    public static String splicingSign(String json){
        List<JSONObject> objList = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(json);
        SortedMap<String,Object> jsonMap = new TreeMap<>();
        String rstStr = "";
        for(Map.Entry<String,Object> entry:jsonObject.entrySet()){
            String key = (String)entry.getKey();
            Object obj = jsonObject.get(key);
            if(obj instanceof JSONArray){
                JSONArray jsonArray = (JSONArray) obj;
                List<String> strLst = new ArrayList<>();
                for(int i=0,len=jsonArray.size();i<len;i++){
                    rstStr = splicingSign(((JSONObject)jsonArray.get(i)).toJSONString());
                    strLst.add(rstStr);
                }
                jsonMap.put(key,strLst);
            }else if(obj instanceof JSONObject){
                rstStr = splicingSign(((JSONObject)obj).toJSONString());
                List<String> strLst = new ArrayList<>();
                strLst.add(rstStr);
                jsonMap.put(key,rstStr);
            }else{
                if(StringUtils.isNotBlank(obj.toString()))
                    jsonMap.put(key,obj.toString());
            }
        }
        StringBuffer sbuffer = new StringBuffer();
        for(Map.Entry<String,Object> entry:jsonMap.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            if(!StringUtils.equalsIgnoreCase("SIGN_INF",key)&&
                    !StringUtils.equalsIgnoreCase("Svc_Rsp_St",key)&&
                    !StringUtils.equalsIgnoreCase("Svc_Rsp_Cd",key)&&
                    !StringUtils.equalsIgnoreCase("Rsp_Inf",key)
            ){
                if(value instanceof ArrayList){
                    for (Object v:(ArrayList)value){
                        sbuffer.append(v);
                    }
                }else{
                    sbuffer.append(key+"="+value+"&");
                }
            }
        }
        return sbuffer.toString();
    }

    public static String createSign(String json){
        String jsonStr = splicingSign((json));
        System.out.println(json);
        jsonStr = jsonStr.substring(0,jsonStr.length()-1);
        return jsonStr;
    }

}
