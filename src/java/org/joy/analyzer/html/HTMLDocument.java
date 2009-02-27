/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import org.cyberneko.html.parsers.DOMParser;
import org.joy.analyzer.Document;
import org.joy.analyzer.DocumentCreationException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * HTML文档类
 * 
 * @author Lamfeeling
 */
public class HTMLDocument extends Document {

	private org.w3c.dom.Document doc;
	private List<Anchor> anchors;
	private String body;
	private static final int MAX_DEPTH = 512;

	/**
	 * 利用指定的，符合HTML语法规则的字符串中构造一个HTML文档
	 * 
	 * @param str
	 *            构造
	 * @param URL
	 *            所需要解析的HTML文档的链接地址是什么
	 * @return 由指定字符串够早的HTML文档
	 * @throws ParseException
	 *             如果超过最大递归深度的文档会抛出解析错误
	 */
	public static HTMLDocument createHTMLDocument(String URL, String str)
			throws ParseException {
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(new StringReader(str.trim())));
		} catch (SAXException ex) {
			// 如果解析错误，要抛出异常
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
		// TODO Auto-generated constructor stub
	}
	/**
	 * 受保护的方法，不可直接构造。
	 * 
	 * @param url
	 *            网页的URL地址
	 * @param data
	 *            描述文档的HTML字符串
	 * @param doc
	 *            由HTML字符串解析出的Dom文档。
	 * @throws ParseException
	 */
	protected HTMLDocument(String url, String data, org.w3c.dom.Document doc)
			throws ParseException {
		super(url, data);
		this.doc = doc;
		parse();
	}

	/**
	 * 测试dom树递归深度的算法。如果深度超过可以接受的范围，就返回false
	 * 
	 * @param node
	 *            根节点
	 * @param level
	 *            当前递归层
	 * @return 如果递归超过规定层数，那么就返回true，如果没有，返回false
	 */
	private boolean testRecursiveDepth(Node node, int level) {
		if (level == MAX_DEPTH)
			return false;
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (!testRecursiveDepth(children.item(i), level + 1)) {
				return false;
			}
		}
		return true;
	}

	private void parse() throws ParseException {
		// 测试递归深度是否超出接受范围
		if (!testRecursiveDepth(doc, 0)) {
			throw new ParseException("超过最大递归深度！");
		}
		// TODO:
		// 利用此类中的Document变量分析HTML，分析代码写这里。方法之後，扄1�7有的私有变量都被赋予合�1�7�的初始值�1�7�1�7
		Parser p = new Parser(getUrl(), doc);
		p.parse();
		anchors = p.getAnchors();
		paragraphs = p.getParagraphs();
		body = p.getMainBody();
	}

	/**
	 * 返回HTML文档的链接集合
	 * 
	 * @return HTML文档的链接集合
	 */
	public List<Anchor> getAnchors() {
		return anchors;
	}

	/**
	 * 返回HTML文本的标题
	 * 
	 * @return HTML文本的标题
	 */
	@Override
	public String getTitle() {
		return TagWindow.getInnerText(
				doc.getElementsByTagName("TITLE").item(0), false);
	}

	/**
	 * 返回文本构建的的DOM树
	 * 
	 * @return HTML构建的DOM树
	 */
	public org.w3c.dom.Document getDoc() {
		return doc;
	}

	/**
	 * 获得的网页当中的主题正文
	 * 
	 * @return 获得的网页当中的主题正文
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
			throws IOException, DocumentCreationException
	{
		String str = Utility.getWebContentFromInputStream(in);
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(new StringReader(str.trim())));
		} catch (SAXException ex) {
			// 如果解析错误，要抛出异常
			Logger.getLogger(HTMLDocument.class.getName()).log(Level.SEVERE,
					null, ex);
			throw new DocumentCreationException(ex.getMessage());
		} catch (IOException ex) {
			// never reach here
			Logger.getLogger(HTMLDocument.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		// 设置文档属性
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
