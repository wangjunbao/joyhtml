/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.joy.analyzer;

import java.util.List;

/**
 * 抽象文档类,用来描述一个的属性，比如一个HTML文档，或者一个Doc文档。
 * @author Lamfeeling
 */
public abstract class Document {
    private String content;

    public abstract List<Paragraph> getParagraphs();
    public abstract String getTitle();
   

    /**
     * 构造一个抽象文档
     * @param content 文档的自负表现形式，即未经改变的纯字符串形式
     */
    public Document(String content) {
        this.content = content;
    }

    /**
     * 获取文档的纯字符串形式。
     * @return 要解析的文档字符串形式
     */
    public String getContent() {
        return content;
    }
}
