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
     * @param URL 锚文本指向的链接
     */
    public Anchor(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public Anchor() {
    }

    public String getText() {
        return text;
    }

    public String getURL() {
        return url;
    }
}
