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
 * Parses the Anchors and the Body Text in HTML
 * 
 * @author Song Liu (lamfeeling2@Gmail.com)
 */
public class Parser {

    private Vector<Anchor> anchors = new Vector<Anchor>();
    private org.w3c.dom.Document doc;
    private String context;
    private TextExtractor textExtractor;
    private String mainBody;

    // gengerate the anchor using the A element
    private Anchor generateLink(Element e) {
	try {
	    URL u = new URL(new URL(context), e.getAttribute("href"));
	    String url = u.toString();
	    url = url.replaceAll("\\/\\.\\/", "\\/");
	    url = url.replaceAll("\\/[^\\/]+\\/\\.\\.\\/", "\\/");
	    // avoid redirect
	    // if (suffix.endsWith("index.htm")
	    // || suffix.endsWith("index.html")
	    // || suffix.endsWith("index.asp")
	    // || suffix.endsWith("default.aspx")
	    // || suffix.endsWith("index.php")
	    // || suffix.endsWith("index.jsp")) {
	    // url = url.substring(0, url.lastIndexOf("/") + 1);
	    // } else
	    if (u.getFile().trim().equals("") && !url.endsWith("/")) {
		// add the / to the end
		url = url + "/";
	    }

	    return new Anchor(TagWindow.getInnerText(e, false), url);
	} catch (MalformedURLException ex) {
	    // ex.printStackTrace();
	    // System.out.println("链接生成错误 " + ex.getMessage());
	}
	return null;
    }

    // Extract A element from DOM tree
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
     * do the parse action
     * 
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
