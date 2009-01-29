/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.nlp;

/**
 * 分词系统的接口，任何其他的分词系统必须实现此接口。
 * @author Administrator
 */
public interface WordSpliter {

    /**
     * 关闭分词接口，释放分词资源
     */
    void close();

    /**
     * 分词，把一段文本分解成一段string字符串
     * @param text 要分词的文本
     * @return 分完词的字符串数组
     */
    String[] split(String text);

    /**
     * 分词，把一段文本分解成一个字符串，当中用空格隔开
     * @param text 要分解的字符串
     * @param isTagged 是否需要标注POS TAG
     * @return 返回分过词的字符串
     */
    String split(String text, boolean isTagged);

    /**
     * 吧一段文本分解成一个Word数组
     * @param text 要分解的字符串
     * @return 返回分解好的Word数组
     */
    Word[] splitToWords(String text);
}
