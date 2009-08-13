/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.html;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;
import org.joy.analyzer.Paragraph;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 网页解析类用来解析网页中的正文文本和链接
 * @author Lamfeeling
 */
public class Parser {

    private Vector<Anchor> anchors = new Vector<Anchor>();
    private org.w3c.dom.Document doc;
    private String context;
    private TextExtractor textExtractor;
    private String mainBody;

    //用网页中的元素生成链接
    private Anchor generateLink(Element e) {
        try {
            URL u = new URL(new URL(context), e.getAttribute("href"));
            String url = u.toString();
            String suffix = u.getFile();
            url = url.replaceAll("\\.\\/", "");
            //避免主页重定向
            if (suffix.endsWith("index.htm") || suffix.endsWith("index.html") ||
            		suffix.endsWith("index.asp") || suffix.endsWith("default.aspx") ||
            		suffix.endsWith("index.php") || suffix.endsWith("index.jsp")) {
                url = url.substring(0, url.lastIndexOf("/") + 1);
            } else if (u.getPath().trim().equals("") && !url.endsWith("/")) {
                //一律写成**/的形式
                url = url + "/";
            }
            return new Anchor(TagWindow.getInnerText(e,false), url);
        } catch (MalformedURLException ex) {
            //ex.printStackTrace();
            //System.out.println("链接生成错误 " + ex.getMessage());
        }
        return null;
    }

    //从网页DOM树中提取A元素
    private void extractLinks() {
        NodeList nl = doc.getElementsByTagName("A");
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            Anchor a = generateLink(e);
            if (a != null) {
                anchors.add(a);
            }
        }
    }

    /**
     * 解析网页文本
     * @throws ParseException 
     */
    public void parse() throws ParseException {
        extractLinks();
        mainBody = textExtractor.extract();
    }

    public Parser(String URL, org.w3c.dom.Document doc) {
        this.doc = doc;
        this.context = URL;
        textExtractor = new TextExtractor(doc);
    }

    /**
     * 获取网页中所有链接。
     * @return 网页中所有链接
     */
    public Vector<Anchor> getAnchors() {
        return anchors;
    }

    public List<Paragraph> getParagraphs() {
        return textExtractor.getParagraphList();
    }

    public String getMainBody() {
        return mainBody;
    }
}
