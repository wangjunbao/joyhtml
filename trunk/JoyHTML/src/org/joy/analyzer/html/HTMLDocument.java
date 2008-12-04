/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.html;

import java.util.List;
import org.cyberneko.html.parsers.DOMParser;
import org.joy.analyzer.Document;
import org.joy.analyzer.Paragraph;

/**
 * HTML文本类。
 * @author Lamfeeling
 */
public class HTMLDocument extends Document {

    private org.w3c.dom.Document doc;

    /**
     * 从指定的字符串中构造一个HTMLDocument
     * @param str 所制定的字符串
     * @return 由指定的字符串够早的文档类
     */
    public HTMLDocument createHTMLDocument(String str) {
        DOMParser parser = new DOMParser();
        Document doc = (Document) parser.getDocument();
        //TODO: 在这里加入解析HTML的代码
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 受保护的构造方法，不可以直接构造
     * @param content 用于构造文档的字符串
     * @param doc 由上文分析器分析出的Document类
     */
    protected HTMLDocument(String content, org.w3c.dom.Document doc) {
        super(content);
        this.doc = doc;
    }

    /**
     * 获取文本中的正文段落
     * @return 返回文档中的正文段落的集合
     */
    @Override
    public List<Paragraph> getParagraphs() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 返回HTML文档的连接集合
     * @return HTML文档的连接集合
     */
    public List<Anchor> getAnchors() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 返回HTML文本的标题
     * @return HTML文本的标题
     */
    @Override
    public String getTitle() {
        return doc.getElementsByTagName("TITLE").item(0).getTextContent();
    }

    /**
     * 返回文本构建的的DOM树
     * @return Dom树文档对象
     */
    public org.w3c.dom.Document getDoc() {
        return doc;
    }
}
