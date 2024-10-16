package com.example.rsa;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


public class GsonUtil {


  static enum GsonSerializerFeature {
    SortField
  }

  private static final String DEFAULT_FORMAT = "yyyyMMddHHmmss";

  private static Gson gson = null;

  static {

    if (gson == null) {

      gson = new GsonBuilder().setDateFormat(DEFAULT_FORMAT).create();

    }
  }


  /**
   * 
   * @param object
   * @return
   */
  public static String toJson(Object object) {
    return gson.toJson(object);
  }

  /**
   * 指定排序规则排序
   * @param object
   * @param serializerFeature
   * @return
   */
  public static String toJson(Object object, GsonSerializerFeature serializerFeature) {

    if (serializerFeature == null) {
      return toJson(object);
    }
    return _sort(object, serializerFeature);
  }

  /**
   * 默认Key 排序
   * @param object
   * @return
   */
  public static String toJsonSort(Object object) {
    return _sort(object, GsonSerializerFeature.SortField);
  }

  /**
   * 
   * @param <T>
   * @param content
   * @param clazz
   * @return
   */
  public static <T> T parseObject(String content, Class<T> clazz) {
    try {
      return gson.fromJson(content, clazz);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static <T> T parseObject(String content, Type type) {
    try {
      return gson.fromJson(content, type);
    } catch (Exception e) {
      // e.printStackTrace();
    }
    return null;
  }


  /**
   * 
   * @param <T>
   * @param content
   * @return
   */
  public static <T> List<Map<String, T>> parseListMaps(String content) {
    try {
      return gson.fromJson(content, new TypeToken<List<Map<String, T>>>() {}.getType());
    } catch (Exception e) {
    }
    return null;
  }


  /**
   * 
   * @param <T>
   * @param gsonString
   * @return
   */
  public static <T> Map<String, T> parseMaps(String gsonString) {
    Map<String, T> map = null;
    if (gson != null) {
      map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {}.getType());
    }
    return map;
  }

  /**
   * 新增
   * @param obj
   * @return
   */
  public static JsonElement toJsonElement(Object obj) {
    return gson.toJsonTree(obj);
  }

  /**
   * 排序主方法
   * @param e
   */
  private static void _sortJsonElement(JsonElement e, GsonSerializerFeature sf) {
    if (e == null || e.isJsonNull()) {
      return;
    }
    if (e.isJsonPrimitive()) {
      return;
    }

    if (e.isJsonArray()) {
      JsonArray a = e.getAsJsonArray();
      for (Iterator<JsonElement> it = a.iterator(); it.hasNext();) {
        _sortJsonElement(it.next(), sf);
      }
      return;
    }

    if (e.isJsonObject()) {

      Map<String, JsonElement> tm = null;
      switch (sf) {
        case SortField:
        default:
          // 按照首字母升序
          tm = new TreeMap<String, JsonElement>((o1, o2) -> o1.compareTo(o2));
          break;
      }
      for (Entry<String, JsonElement> en : e.getAsJsonObject().entrySet()) {
        tm.put(en.getKey(), en.getValue());
      }

      for (Entry<String, JsonElement> en : tm.entrySet()) {
        e.getAsJsonObject().remove(en.getKey());
        e.getAsJsonObject().add(en.getKey(), en.getValue());
        _sortJsonElement(en.getValue(), sf);
      }
      return;
    }
  }

  private static String _sort(Object object, GsonSerializerFeature serializerFeature) {
    JsonElement e = gson.toJsonTree(object);
    _sortJsonElement(e, serializerFeature);
    return gson.toJson(e);
  }

}
