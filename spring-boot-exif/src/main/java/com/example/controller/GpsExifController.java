package com.example.controller;

import com.example.service.GpsExifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wgs
 * @Date 2022/5/7 09:59
 * @Classname GpsExifController
 * @Description
 */
@RestController
@RequestMapping("/exif")
public class GpsExifController {
    @Autowired
    private GpsExifService gpsExifService;

    @GetMapping("/imgPath")
    public String printInfo(String path) {
        gpsExifService.printPicExifInfo(path);
        return "ok";
    }

    @GetMapping("/imgDir")
    public String printInfoList(String path) {
        gpsExifService.printPicExifFile(path);
        return "ok";
    }
}
