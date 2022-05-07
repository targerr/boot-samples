package com.example.service;

import com.drew.imaging.ImageProcessingException;

import java.io.IOException;

/**
 * @Author: wgs
 * @Date 2022/5/6 17:39
 * @Classname GpsExifService
 * @Description
 */
public interface GpsExifService {

    /**
     * 打印图片exif信息
     *
     * @param imgPath img路径
     */
    public abstract void printPicExifInfo(String imgPath);

    /**
     * 打印pic exif文件
     *
     * @param imgDir 图片文件夹
     */
    public abstract void printPicExifFile(String imgDir) ;
}
