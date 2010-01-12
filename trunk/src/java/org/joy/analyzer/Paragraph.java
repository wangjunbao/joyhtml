package org.joy.analyzer;

/**
 * Paragraph is defined as a topic-independent block in a document. Each
 * paragraph has a weight for its degree of approximation to main topic in
 * document
 * 
 * @author Song Liu(lamfeeling2@gmail.com)
 */
public class Paragraph {

    /**
     * paragraph text
     */
    private String text;
    /**
     * paragraph weight
     */
    private double weight;
    /**
     * paragraph offset
     */
    private int offset;

    public Paragraph() {
    }

    /**
     * construct a paragraph using text, weight, and offset
     * 
     * @param text
     *            paragraph text
     * @param weight
     *            weight of this paragraphs. indicates how close it is to the
     *            main topic of a document.
     * @param offset
     *            offset for a paragraph in a document
     */
    public Paragraph(String text, double weight, int offset) {
	this.text = text;
	this.weight = weight;
	this.offset = offset;
    }

    public void setText(String text) {
	this.text = text;
    }

    public void setWeight(double weight) {
	this.weight = weight;
    }

    public void setOffset(int offset) {
	this.offset = offset;
    }

    /**
     * get paragraph text
     * 
     * @return paragraph text
     */
    public String getText() {
	return text;
    }

    /**
     * get paragraph weight
     * 
     * @return paragraph weight
     */
    public double getWeight() {
	return weight;
    }

    /**
     * get paragraph offset
     * 
     * @return paragraph offset
     */
    public int getOffset() {
	return offset;
    }
}
