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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * HTML文档类
 * @author Lamfeeling
 */
public class HTMLDocument extends Document {

    private org.w3c.dom.Document doc;
    private List<Anchor> anchors;
    private String url;
    private String body;

    /**
     * 利用指定的，符合HTML语法规则的字符串中构造一个HTML文档
     * @param str 构造
     * @param URL 所需要解析的HTML文档的链接地址是什么
     * @return 由指定字符串够早的HTML文档
     * @throws ParseException
     */
    public static HTMLDocument createHTMLDocument(String URL, String str) throws ParseException {
        DOMParser parser = new DOMParser();
        try {

            //是否允许命名空间
            parser.setFeature("http://xml.org/sax/features/namespaces", false);
            //是否允许增补缺失的标签。如果要以XML方式操作HTML文件，此值必须为真
            parser.setFeature("http://cyberneko.org/html/features/balance-tags", true);
            //是否剥掉<script>元素中的<!-- -->等注释符
            parser.setFeature("http://cyberneko.org/html/features/scanner/script/strip-comment-delims", true);
            parser.setFeature("http://cyberneko.org/html/features/augmentations", true);
            parser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");

            //解析HTML片段时是否作标签增补，此功能不要用在DOMParser上，而要用在DOMFragmentParser上
            //parser.setFeature("http://cyberneko.org/features/document-fragment",true);
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
     * @param url 网页的URL地址
     * @param data 描述文档的HTML字符串
     * @param doc 由HTML字符串解析出的Dom文档。
     */
    protected HTMLDocument(String url, String data, org.w3c.dom.Document doc) {
        super(data);
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
        body = p.getMainBody();
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
        return TagWindow.getInnerText(doc.getElementsByTagName("TITLE").item(0),false);
    }

    /**
     * 返回文本构建的的DOM树
     * @return HTML构建的DOM树
     */
    public org.w3c.dom.Document getDoc() {
        return doc;
    }

    /**
     * 获得的网页当中的主题正文
     * @return 获得的网页当中的主题正文
     */
    public String getBody() {
        return body;
    }

    /**
     * 获得网页的URL
     * @return 获得网页的URL
     */
    public String getUrl() {
        return url;
    }
}
