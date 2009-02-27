package org.joy.analyzer.plugins.txt;

import java.util.ArrayList;

import org.joy.analyzer.Document;
import org.joy.analyzer.Paragraph;
/**
 * 一个简单的TXT文档结构示例
 * @author Andy
 *
 */
public class TXTDocument extends Document {
	public static TXTDocument createTXTDocument(String content){
		return new TXTDocument(content);
	}
	
	protected TXTDocument(String data) {
		super(data);
		paragraphs = new ArrayList<Paragraph>();
		paragraphs.add(new Paragraph(data,1.0,0));
	}

	@Override
	public String getTitle() {
		return null;
	}

}
