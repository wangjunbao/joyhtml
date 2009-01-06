/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.html;

/**
 * 描述锚文本实体类。
 * @author Lamfeeling
 */
public class Anchor {

    private String text;
    private String url;

    /**
     * 构造函数
     * @param text 锚文本
     * @param url 锚文本指向的链接
     */
    public Anchor(String text, String url) {
        this.text = text;
        this.url = url;
    }

    /**
     * bean 方法，默认构造函数
     */
    public Anchor() {
    }

    /**
     * 獲取超鏈接锚文本
     * @return 返回超鏈接錨文本
     */
    public String getText() {
        return text;
    }

    /**
     * 获取超链接的URL地址
     * @return
     */
    public String getURL() {
        return url;
    }
}
