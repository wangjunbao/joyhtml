/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.html;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cyberneko.html.parsers.DOMParser;
import org.joy.analyzer.Document;
import org.joy.analyzer.DocumentCreationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * HTML Document class
 * 
 * @author Song Liu(lamfeeling2@gmail.com)
 */
public class HTMLDocument extends Document {

    private org.w3c.dom.Document doc;
    private List<Anchor> anchors;
    private String body;

    /**
     * construct a HTMLDocument object using specified URL and content text
     * 
     * @param str
     *            HTML data
     * @param URL
     *            URL for this document
     * @return HTMLDocument constructed by these parameters
     * @throws ParseException
     * 
     */
    public static HTMLDocument createHTMLDocument(String URL, String str)
	    throws ParseException {
	DOMParser parser = new DOMParser();
	try {
	    parser.parse(new InputSource(new StringReader(str.trim())));
	} catch (SAXException ex) {
	    // if error occurs
	    Logger.getLogger(HTMLDocument.class.getName()).log(Level.SEVERE,
		    null, ex);
	    throw new ParseException(ex.getMessage());
	} catch (IOException ex) {
	    // never reach here
	    Logger.getLogger(HTMLDocument.class.getName()).log(Level.SEVERE,
		    null, ex);
	}
	return new HTMLDocument(URL, str, parser.getDocument());
    }

    public HTMLDocument() {
    }

    /**
     * protected constructor, using createHTMLDocument method
     * 
     * @param url
     *            URL used for creating this document
     * @param data
     *            HTML data
     * @param doc
     *            dom tree analyzed from HTML data
     * @throws ParseException
     */
    protected HTMLDocument(String url, String data, org.w3c.dom.Document doc)
	    throws ParseException {
	super(url, data);
	this.doc = doc;
	parse();
    }

    // /**
    // * 测试dom树递归深度的算法。如果深度超过可以接受的范围，就返回false
    // *
    // * @param node
    // * 根节点
    // * @param level
    // * 当前递归层
    // * @return 如果递归超过规定层数，那么就返回true，如果没有，返回false
    // */
    // private boolean testRecursiveDepth(Node node, int level) {
    // if (level == MAX_DEPTH)
    // return false;
    // NodeList children = node.getChildNodes();
    // for (int i = 0; i < children.getLength(); i++) {
    // if (!testRecursiveDepth(children.item(i), level + 1)) {
    // return false;
    // }
    // }
    // return true;
    // }

    private void parse() throws ParseException {
	// 测试递归深度是否超出接受范围
	// if (!testRecursiveDepth(doc, 0)) {
	// throw new ParseException("超过最大递归深度！");
	// }

	// parse the HTML data
	Parser p = new Parser(getUrl(), doc);
	p.parse();
	anchors = p.getAnchors();
	paragraphs = p.getParagraphs();
	body = p.getMainBody();
    }

    /**
     * returns the HTML anchors
     * 
     * @return anchors in this HTML document
     */
    public List<Anchor> getAnchors() {
	return anchors;
    }

    /**
     * returns the HTML title
     * 
     * @return HTML title
     */
    @Override
    public String getTitle() {
	return TagWindow.getInnerText(
		doc.getElementsByTagName("TITLE").item(0), false);
    }

    /**
     * DOM tree that constructed
     * 
     * @return DOM tree
     */
    public org.w3c.dom.Document getDoc() {
	return doc;
    }

    /**
     * body text in the current document
     * 
     * @return body in current document
     */
    public String getBody() {
	return body;
    }

    @Override
    public String getMineType() {
	return "text/html";
    }

    @Override
    public String getSuffix() {
	return ".html;.htm;.shtml";
    }

    @Override
    public void createFromInputStream(InputStream in, String URL)
	    throws IOException, DocumentCreationException {
	String str = Utility.getWebContentFromInputStream(in);
	DOMParser parser = new DOMParser();
	try {
	    parser.parse(new InputSource(new StringReader(str.trim())));
	} catch (SAXException ex) {
	    Logger.getLogger(HTMLDocument.class.getName()).log(Level.SEVERE,
		    null, ex);
	    throw new DocumentCreationException(ex.getMessage());
	} catch (IOException ex) {
	    // never reach here
	    Logger.getLogger(HTMLDocument.class.getName()).log(Level.SEVERE,
		    null, ex);
	}

	// set fields
	setData(str);
	setUrl(URL);
	doc = parser.getDocument();
	try {
	    parse();
	} catch (ParseException ex) {
	    ex.printStackTrace();
	    throw new DocumentCreationException(ex.getMessage());
	}
    }
}
