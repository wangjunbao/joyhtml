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
 * HTML文本类�1�7�1�7
 * @author Lamfeeling
 */
public class HTMLDocument extends Document {

    private org.w3c.dom.Document doc;
    private List<Anchor> anchors;
    private List<Paragraph> paragraphs;
    private String url;

    /**
     * 从指定的字符串中构�1�7�一个HTMLDocument
     * @param str 扄1�7制定的字符串
     * @return 由指定的字符串够早的文档籄1�7
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
     * 受保护的构�1�7�方法，不可以直接构逄1�7
     * @param URL 网页的URL地址
     * @param content 用于构�1�7�文档的字符丄1�7
     * @param doc 由上文分析器分析出的Document籄1�7
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
     * @return 返回文档中的正文段落的集各1�7
     */
    @Override
    public List<Paragraph> getParagraphs() {
        //return paragraphs;
        throw new UnsupportedOperationException();
    }

    /**
     * 返回HTML文档的连接集各1�7
     * @return HTML文档的连接集各1�7
     */
    public List<Anchor> getAnchors() {
        return anchors;
    }

    /**
     * 返回HTML文本的标预1�7
     * @return HTML文本的标预1�7
     */
    @Override
    public String getTitle() {
        return doc.getElementsByTagName("TITLE").item(0).getTextContent();
    }

    /**
     * 返回文本构建的的DOM栄1�7
     * @return Dom树文档对豄1�7
     */
    public org.w3c.dom.Document getDoc() {
        return doc;
    }
}
