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
 * Anchor represents the anchor tag(<A>) in HTML document
 * 
 * @author Song Liu (lamfeeling2@gmail.com)
 */
public class Anchor extends Document {
    private final static int OFFSET_RANDOM_BOUNDARY = 1024*32;
    private final static double ANCHOR_WEIGHT = 1.0;

    /**
     * Construct method
     * 
     * @param text
     *            text of this anchor
     * @param url
     *            the href attribute of this anchor
     */
    public Anchor(String text, String url) {
	setUrl(url);
	setData(text);
	Random r = new Random();
	// get random negative offset of anchor, used for index
	paragraphs.add(new Paragraph(text, ANCHOR_WEIGHT, -r
		.nextInt(OFFSET_RANDOM_BOUNDARY)));
    }

    @Override
    public void createFromInputStream(InputStream in, String URL)
	    throws IOException, DocumentCreationException {

    }

    @Override
    public String getMineType() {
	return null;
    }

    @Override
    public String getSuffix() {
	return null;
    }

    @Override
    public String getTitle() {
	return null;
    }
}
