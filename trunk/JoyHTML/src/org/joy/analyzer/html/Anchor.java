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
    private String URL;

    /**
     * 构造函数
     * @param text 锚文本
     * @param URL 锚文本指向的链接
     */
    public Anchor(String text, String URL) {
        this.text = text;
        this.URL = URL;
    }

    public Anchor() {
    }

    public String getText() {
        return text;
    }

    public String getURL() {
        return URL;
    }
}
e