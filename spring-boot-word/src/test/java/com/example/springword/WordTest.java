package com.example.springword;

import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.api.IWordReplace;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import com.github.houbb.sensitive.word.support.result.WordResultHandlers;
import com.github.houbb.sensitive.word.support.result.WordTagsDto;
import com.github.houbb.sensitive.word.support.resultcondition.WordResultConditions;
import com.github.houbb.sensitive.word.support.tag.WordTags;

import java.util.Collections;
import java.util.List;

/**
 * @Author: wgs
 * @Date 2024/4/24 14:55
 * @Classname WordTest
 * @Description
 */
public class WordTest {
    public static void main(String[] args) {
        final String text = "五星红旗迎风飘扬，毛主席的画像屹立在天安门前。";
//        base(text);

        System.out.println("----------------");
        IWordReplace replace = new MyWordReplace();
        String result = SensitiveWordHelper.replace(text, replace);
        System.out.println(result);

        wordBs();

    }

    public static void wordBs() {
        final String text = "五星红旗迎风飘扬，毛主席的画像屹立在天安门前。";

      // 默认敏感词标签为空
        List<WordTagsDto> wordList1 = SensitiveWordHelper.findAll(text, WordResultHandlers.wordTags());
        System.out.println(wordList1.toString());

        final String text1 = "I have a nice day。";

        List<String> wordList = SensitiveWordBs.newInstance()
                .wordDeny(new IWordDeny() {
                    @Override
                    public List<String> deny() {
                        return Collections.singletonList("。");
                    }
                })
                .wordResultCondition(WordResultConditions.alwaysTrue())
                .init()
                .findAll(text1);

        System.out.println(wordList.toString());


        SensitiveWordBs wordBs = SensitiveWordBs.newInstance()
                .wordDeny(WordDenys.defaults())
                .wordAllow(WordAllows.defaults())
                .init();

        final String text2 = "五星红旗迎风飘扬，毛主席的画像屹立在天安门前。";
        System.out.println("默认: " + wordBs.contains(text2));
    }

    private static void base(String text) {
        System.out.println(SensitiveWordHelper.contains(text));

        // 返回第一个敏感词
        String word = SensitiveWordHelper.findFirst(text);
        System.out.println(word);

        // 返回所有敏感词
        List<String> wordList = SensitiveWordHelper.findAll(text);
        System.out.println(wordList);

        // 默认的替换策略
        String result = SensitiveWordHelper.replace(text);
        System.out.println(result);

        //指定替换的内容
        String specify = SensitiveWordHelper.replace(text, '0');
        System.out.println(specify);
    }
}
