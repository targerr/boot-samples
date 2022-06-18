package com.example.model;

import cn.hutool.core.util.StrUtil;
import com.example.constant.OssSavePlaceEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

/*
 * 定义文件上传的配置信息
 *
 */
@Data
@AllArgsConstructor
public class OssFileConfig {

    /**
     * 用户头像
     */
    interface BIZ_TYPE {
        /**
         * 用户头像
         */
        String AVATAR = "avatar";
        /**
         * 接口类型卡片背景图片
         */
        String IF_BG = "ifBG";
        /**
         * 接口参数
         */
        String CERT = "cert";
    }

    /**
     * 图片类型后缀格式
     */
    public static final Set IMG_SUFFIX = new HashSet(Arrays.asList("jpg", "png", "jpeg", "gif"));

    /**
     * 全部后缀格式的文件标识符
     */
    public static final String ALL_SUFFIX_FLAG = "*";

    /**
     * 不校验文件大小标识符
     */
    public static final Long ALL_MAX_SIZE = -1L;

    /**
     * 允许上传的最大文件大小的默认值
     */
    public static final Long DEFAULT_MAX_SIZE = 5 * 1024 * 1024L;

    private static final Map<String, OssFileConfig> ALL_BIZ_TYPE_MAP = new HashMap<>();

    static {
        ALL_BIZ_TYPE_MAP.put(BIZ_TYPE.AVATAR, new OssFileConfig(OssSavePlaceEnum.PUBLIC, IMG_SUFFIX, DEFAULT_MAX_SIZE));
        ALL_BIZ_TYPE_MAP.put(BIZ_TYPE.IF_BG, new OssFileConfig(OssSavePlaceEnum.PUBLIC, IMG_SUFFIX, DEFAULT_MAX_SIZE));
        ALL_BIZ_TYPE_MAP.put(BIZ_TYPE.CERT, new OssFileConfig(OssSavePlaceEnum.PRIVATE, new HashSet<>(Arrays.asList(ALL_SUFFIX_FLAG)), DEFAULT_MAX_SIZE));
    }

    /**
     * 存储位置
     */
    private OssSavePlaceEnum ossSavePlaceEnum;

    /**
     * 允许的文件后缀, 默认全部类型
     */
    private Set<String> allowFileSuffix = new HashSet<>(Arrays.asList(ALL_SUFFIX_FLAG));

    /**
     * 允许的文件大小, 单位： Byte
     */
    private Long maxSize = DEFAULT_MAX_SIZE;


    /**
     * 是否在允许的文件类型后缀内
     * @param fixSuffix
     * @return
     */
    public boolean isAllowFileSuffix(String fixSuffix) {

        //允许全部
        if (this.allowFileSuffix.contains(ALL_SUFFIX_FLAG)) {
            return true;
        }
        fixSuffix = StrUtil.blankToDefault(fixSuffix, "");
        return this.allowFileSuffix.contains(fixSuffix.toLowerCase());
    }

    /**
     * 是否在允许的大小范围内
     * @param fileSize
     * @return
     */
    public boolean isMaxSizeLimit(Long fileSize) {

        // 允许全部大小
        if (ALL_MAX_SIZE.equals(this.maxSize)) {
            return true;
        }

        return this.maxSize >= (fileSize == null ? 0L : fileSize);
    }


    public static OssFileConfig getOssFileConfigByBizType(String bizType) {
        return ALL_BIZ_TYPE_MAP.get(bizType);
    }

}
