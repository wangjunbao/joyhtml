/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.html;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
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

    //用网页中的元素生成链接
    private Anchor generateLink(Element e) {
        try {
            URL u = new URL(new URL(context), e.getAttribute("href"));
            String url = u.toString().toLowerCase();
            url = url.replaceAll("\\.\\/", "");
            //避免主页重定向
            if (url.endsWith("index.htm") || url.endsWith("index.html") ||
                    url.endsWith("index.asp") || url.endsWith("default.aspx") ||
                    url.endsWith("index.php") || url.endsWith("index.jsp")) {
                url = url.substring(0, url.lastIndexOf("/") + 1);
            } else if (u.getPath().indexOf(".") == -1 && !url.endsWith("/")) {
                //一律写成**/的形式
                url = url + "/";
            }
            System.out.println(url);
            return new Anchor(e.getTextContent(), url);
        } catch (MalformedURLException ex) {
            //System.out.println("");
            ex.printStackTrace();
        }
        return null;
    }

    //从网页DOM树中提取A元素
    private void extractLinks() {
        NodeList nl = doc.getElementsByTagName("A");
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            anchors.add(generateLink(e));
        }
    }

    /**
     * 解析网页文本
     */
    public void parse() {
        extractLinks();
    }

    public Parser(String URL, org.w3c.dom.Document doc) {
        this.doc = doc;
        this.context = URL;
    }

    /**
     * 获取网页中所有链接。
     * @return 网页中所有链接
     */
    public Vector<Anchor> getAnchors() {
        return anchors;
    }
}
