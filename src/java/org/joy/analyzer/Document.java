/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.UnsupportedAddressTypeException;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象文档类,用来描述一个的属性，比如一个HTML文档，或者一个Doc文档。
 * 
 * @author Lamfeeling
 */
public abstract class Document {

	private String data;
	private String url;
	protected List<Paragraph> paragraphs;

	/**
	 * 从输入流创建文档对象
	 * 
	 * @param in
	 */
	public void createFromInputStream(InputStream in, String URL)
			throws IOException,DocumentCreationException{
	    throw new UnsupportedOperationException();
	}

	/**
	 * 构造一个缺省文档
	 */
	public Document() {
		// TODO Auto-generated constructor stub
		paragraphs = new ArrayList<Paragraph>();
	}
	/**
	 * 构造一个抽象文档
	 * 
	 * @param content
	 *            文档的自负表现形式，即未经改变的纯字符串形式
	 */
	public Document(String URL, String data) {
		this.data = data;
		this.url = URL;
	}
	
	/**
	 * 获取文本中的正文段落
	 * 
	 * @return 返回文档中的正文段落的集合
	 */
	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

	/**
	 * 获取文章的全部文字。
	 * 
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
	 * 
	 * @return 文档的标题
	 */
	public abstract String getTitle();

	/**
	 * 设置这个文档的数据文字
	 * @param data 需要传输的数据文字
	 */
	protected void setData(String data){
		this.data = data;
	}

	/**
	 * 获取文档的纯字符串形式。
	 * 
	 * @return 要解析的文档字符串形式
	 */
	public String getData() {
		return data;
	}

	/**
	 * 设置文档的URL
	 */
	protected void setUrl(String URL){
		this.url = URL;
	}
	/**
	 * 获取文件的URL
	 * @return 文件URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 获取该文档的MIME的类型
	 * 
	 * @return 该文档的MIME的类型
	 */
	public abstract String getMineType();

	/**
	 * 获取该文档的文件名后缀
	 * 
	 * @return 文档的文件名后缀
	 */
	public abstract String getSuffix();
}
