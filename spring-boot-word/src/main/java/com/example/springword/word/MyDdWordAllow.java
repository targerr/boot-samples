package com.example.springword.word;

import com.github.houbb.sensitive.word.api.IWordAllow;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wgs
 * @Date 2024/4/24 15:23
 * @Classname MyDdWordAllow
 * @Description
 */
@Service
public class MyDdWordAllow implements IWordAllow {
    @Override
    public List<String> allow() {
        List<String> allowed = new ArrayList<String>();
        allowed.add("I");
        return allowed;
    }
}
