package com.example.rsa.sm4;

import cn.hutool.crypto.SmUtil;

/**
 * @Author: wgs
 * @Date 2024/8/3 11:37
 * @Classname SM3Test
 * @Description
 *  原文链接：https://blog.csdn.net/thinkpet/article/details/131316040*
 * 需要注意的是SM3 算法是不可逆的，只能从原文得到摘要签名，不能从摘要签名 反向得到原文，*
 */
public class SM3Test {
    /**
     * sm3 摘要签名算法
     * @param args
     */
    public static void main(String[] args) {
        String str = "hello2023";
        /**
         * str重复执行sm3签名后的值 是相同的
         */
        String s = SmUtil.sm3(str);
        System.out.println(s);
    }


}
