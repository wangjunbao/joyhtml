package org.joy.analyzer.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joy.analyzer.Paragraph;

/**
 * Split Semi-HTML data by line breaker, and build the paragraphs, and set
 * weight for each paragraph Semi-HTML is a plain text that contains some
 * important visual HTML tags, such as the <H> and <Title>.
 * 
 * @author Lamfeeling
 */
public class ParagraphSplitter {

    private String body, whole;
    private int offset = 0;

    /**
     * Constructor
     * 
     * @param body
     *            Semi-Html of Body Text
     * @param whole
     *            semi-html of whole text
     */
    public ParagraphSplitter(String body, String whole) {
	this.body = body;
	this.whole = whole;
    }

    /**
     * split all the paragraphs from given text and add them to the paragraph
     * list
     * 
     * @return paragraphs after split
     */
    public List<Paragraph> split() {
	// reset the offset
	offset = 0;
	try {
	    ArrayList<Paragraph> paragraphList = new ArrayList<Paragraph>();
	    // seperate the main body with other parts of the whold text
	    int start = whole.indexOf(body);
	    int end = start + body.length();

	    split(whole.substring(0, start), paragraphList, .1);
	    // now, add the main body
	    split(body, paragraphList, .3);

	    split(whole.substring(end), paragraphList, .1);
	    return paragraphList;
	} catch (IOException ex) {
	    Logger.getLogger(ParagraphSplitter.class.getName()).log(
		    Level.SEVERE, null, ex);
	}
	return null;
    }

    /**
     * text line by line, and translate the remaining visual HTML tags into
     * score and add it "topic score". Finlly stores it into the paragraphs
     * list.
     * 
     * @param str
     *            Semi-HTML text you want to analyse
     * @param paraList
     *            paragraph list you want to store in
     * @param base
     *            base score
     * @throws java.io.IOException
     */
    private void split(String str, ArrayList<Paragraph> paraList, double base)
	    throws IOException {
	BufferedReader r = new BufferedReader(new StringReader(str));
	String line = r.readLine();

	double lastWeight = 0;
	while (line != null) {
	    double weight = base;
	    if (line.startsWith("<H")) {
		// add score for visual tags, for example, H tags.
		weight += .3;
	    }
	    if (line.startsWith("<TITLE")) {
		weight += .7;
	    }
	    String t = line.replaceAll("(</*H[1-9]>)|(</*TITLE>)", "");
	    if (!t.trim().equals("")) {
		// whether this paragraph has the same weight with the previous
		// paragraph
		if (weight == lastWeight) {
		    // if we have two paragraphs that is next to each other,
		    // then merge them.
		    String p = paraList.get(paraList.size() - 1).getText()
			    + "\r\n" + t;
		    paraList.get(paraList.size() - 1).setText(p);
		    // get the offset for next paragraph, 2 for \r\n
		    offset += t.length() + 2;
		} else {
		    // if not, start a new paragraph
		    paraList.add(new Paragraph(t, weight, offset));
		    lastWeight = weight;
		    // get the offset for next paragraph
		    offset += t.length();
		}
	    }
	    line = r.readLine();
	}
    }
}
