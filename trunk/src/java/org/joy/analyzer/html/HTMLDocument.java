/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.html;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import org.cyberneko.html.parsers.DOMParser;
import org.joy.analyzer.Document;
import org.joy.analyzer.Paragraph;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * HTML文档类
 * @author Lamfeeling
 */
public class HTMLDocument extends Document {

    private org.w3c.dom.Document doc;
    private List<Anchor> anchors;
    private List<Paragraph> paragraphs;
    private String url;

    /**
     * 利用指定的，符合HTML语法规则的字符串中构造一个HTML文档
     * @param str 构造
     * @return 由指定字符串够早的HTML文档
     */
    public static HTMLDocument createHTMLDocument(String URL, String str) throws ParseException {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource(new StringReader(str)));
        } catch (SAXException ex) {
            //如果解析错误，要抛出异常
            Logger.getLogger(HTMLDocument.class.getName()).log(Level.SEVERE, null, ex);
            throw new ParseException(ex.getMessage());
        } catch (IOException ex) {
            // never reach here
            Logger.getLogger(HTMLDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new HTMLDocument(URL, str, parser.getDocument());
    }

    /**
     * 受保护的方法，不可直接构造。
     * @param URL 网页的URL地址
     * @param content 描述文档的HTML字符串
     * @param doc 由HTML字符串解析出的Dom文档。
     */
    protected HTMLDocument(String url, String content, org.w3c.dom.Document doc) {
        super(content);
        this.doc = doc;
        this.url = url;
        parse();

    }

    private void parse() {
        //TODO: 利用此类中的Document变量分析HTML，分析代码写这里。方法之後，扄1�7有的私有变量都被赋予合�1�7�的初始值�1�7�1�7
        Parser p = new Parser(url, doc);
        p.parse();
        anchors = p.getAnchors();
        paragraphs = p.getParagraphs();
    }

    /**
     * 获取文本中的正文段落
     * @return 返回文档中的正文段落的集合
     */
    @Override
    public List<Paragraph> getParagraphs() {
        return paragraphs;
        //throw new UnsupportedOperationException();
    }

    /**
     * 返回HTML文档的链接集合
     * @return HTML文档的链接集合
     */
    public List<Anchor> getAnchors() {
        return anchors;
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
     * @return HTML构建的DOM树
     */
    public org.w3c.dom.Document getDoc() {
        return doc;
    }
}