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

    private String data;
    protected List<Paragraph> paragraphs;

    /**
     * 获取文本中的正文段落
     * @return 返回文档中的正文段落的集合
     */
    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    /**
     * 获取文章的全部文字。
     * @return 文章的全部文字
     */
    public String getContent() {
        StringBuffer sb = new StringBuffer();
        for (Paragraph p : paragraphs) {
            sb.append(p.getText());
        }
        return sb.toString();
    }

    /**
     * 获取文档的标题
     * @return 文档的标题
     */
    public abstract String getTitle();

    /**
     * 构造一个抽象文档
     * @param content 文档的自负表现形式，即未经改变的纯字符串形式
     */
    public Document(String data) {
        this.data = data;
    }

    /**
     * 获取文档的纯字符串形式。
     * @return 要解析的文档字符串形式
     */
    public String getData() {
        return data;
    }
}
