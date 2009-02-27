package org.joy.analyzer.txt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.joy.analyzer.Document;
import org.joy.analyzer.DocumentCreationException;
import org.joy.analyzer.Paragraph;

/**
 * 一个简单的TXT文档模型示例
 * 
 * @author Andy
 */
public class TXTDocument extends Document {

	public TXTDocument() {
		// TODO Auto-generated constructor stub
	}
	
	protected TXTDocument(String path, String data) {
		super(path,data);
		paragraphs = new ArrayList<Paragraph>();
		paragraphs.add(new Paragraph(data, 1.0, 0));
	}

	/**
	 * TXT文档不存在“标题”的概念
	 */
	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public String getMineType() {
		return "text/plain";
	}

	@Override
	public String getSuffix() {
		return ".txt";
	}

	@Override
	public void createFromInputStream(InputStream in, String URL) 
	throws IOException,DocumentCreationException {
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		String line = r.readLine();
		StringBuffer sb = new StringBuffer();
		while (line != null) {
			sb.append(line);
			line = r.readLine();
		}
		//设置文档属性
		setData(sb.toString());
		setUrl(URL);
		paragraphs = new ArrayList<Paragraph>();
		paragraphs.add(new Paragraph(sb.toString(), 1.0, 0));
	}

}
