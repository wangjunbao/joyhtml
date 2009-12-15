/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.nlp;

import java.util.Vector;

/**
 * 分词系统的接口，任何其他的分词系统必须实现此接口。
 * 
 * @author Administrator
 */
public abstract class WordSpliter {

    /**
     * 关闭分词接口，释放分词资源
     */
    public abstract void close();

    /**
     * 分词，把一段文本分解成一段string字符串
     * 
     * @param text
     *            要分词的文本
     * @return 分完词的字符串数组
     */
    public abstract String[] split(String text);

    /**
     * 分词，把一段文本分解成一个字符串，当中用空格隔开
     * 
     * @param text
     *            要分解的字符串
     * @param isTagged
     *            是否需要标注POS TAG
     * @return 返回分过词的字符串
     */
    public abstract String split(String text, boolean isTagged);

    /**
     * 吧一段文本分解成一个Word数组
     * 
     * @param text
     *            要分解的字符串
     * @return 返回分解好的Word数组
     */
    public Word[] splitToWords(String text) {
	String[] res = split(text);
	Vector<Word> words = new Vector<Word>();
	for (String t : res) {
	    words.add(new Word(t + "/x"));
	}
	return words.toArray(new Word[0]);
    }
}
