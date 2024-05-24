package com.example.springword.word;

import com.github.houbb.sensitive.word.api.IWordDeny;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wgs
 * @Date 2024/4/24 15:26
 * @Classname MyDdWordDeny
 * @Description
 */
@Service
public class MyDdWordDeny implements IWordDeny {
    @Override
    public List<String> deny() {
        List<String> deny = new ArrayList<String>();
        deny.add("帅");
        return deny;
    }
}
