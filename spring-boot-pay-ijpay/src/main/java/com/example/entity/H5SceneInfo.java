package com.example.entity;

import lombok.Data;

/**
 * @Author: wgs
 * @Date 2022/6/24 11:11
 * @Classname H5SceneInfo
 * @Description
 */
@Data
public class H5SceneInfo {
    private H5 h5_info;
    @Data
    public static class H5 {
        private String type;
        private String app_name;
        private String bundle_id;
        private String package_name;
        private String wap_url;
        private String wap_name;
    }
}
