/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.html;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.joy.analyzer.Document;
import org.joy.analyzer.DocumentCreationException;
import org.joy.analyzer.Paragraph;

/**
 * 描述锚文本实体类。
 * 
 * @author Lamfeeling
 */
public class Anchor extends Document {
    private final static int OFFSET_RANDOM_BOUNDARY = 32000; 
    private final static double ANCHOR_WEIGHT = 1.0;
    
    /**
     * 构造函数
     * 
     * @param text
     *            锚文本
     * @param url
     *            锚文本指向的链接
     */
    public Anchor(String text, String url) {
	setUrl(url);
	setData(text);
	Random r = new Random();
	//random the offset of links
	paragraphs.add(new Paragraph(text, ANCHOR_WEIGHT, -r.nextInt(OFFSET_RANDOM_BOUNDARY)));
    }

    @Override
    public void createFromInputStream(InputStream in, String URL)
	    throws IOException, DocumentCreationException {
	// TODO Auto-generated method stub

    }

    @Override
    public String getMineType() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getSuffix() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getTitle() {
	// TODO Auto-generated method stub
	return null;
    }
}
