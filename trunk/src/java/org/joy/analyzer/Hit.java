package org.joy.analyzer;

import java.text.DecimalFormat;
import java.util.Vector;

/**
 * Hit is a data structure stores all the positions and score for a given term
 * in a document.
 * 
 * @author Song Liu(lamfeeling2@gmail.com)
 */
public class Hit implements Comparable<Hit>, Cloneable {

    private double score;
    private String term;
    private Vector<Integer> pos;

    /**
     * default constructor
     */
    public Hit() {
	pos = new Vector<Integer>();
    }

    /**
     * construct a hit using given term. the positions for this term is empyer
     * 
     * @param term
     *            term for this hit
     */
    public Hit(String term) {
	this.term = term;
	pos = new Vector<Integer>();
    }

    /**
     * get score for this hit
     * 
     * @return hit score
     */
    public double getScore() {
	return score;
    }

    /**
     * set score for this hit
     * 
     * @param score
     *            hit score
     */
    public void setScore(double score) {
	this.score = score;
    }

    /**
     * get positions for this term in a document
     * 
     * @return positions
     */
    public Vector<Integer> getPos() {
	return pos;
    }

    /**
     * add one position for this hit.
     * 
     * @param pos
     */
    public void addPos(int pos) {
	this.pos.add(pos);
    }

    /**
     * set term for this term
     * 
     * @param term
     *            term
     */
    public void setTerm(String term) {
	this.term = term;
    }

    /**
     * get hit term
     * 
     * @return term
     */
    public String getTerm() {
	return term;
    }

    /**
     * string format. for debugging
     * 
     * @return
     */
    @Override
    public String toString() {
	return term + ":" + new DecimalFormat("0.00").format(score);
    }

    public int compareTo(Hit h) {
	return Double.compare(h.getScore(), getScore());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
	Hit h = new Hit();
	h.setTerm(term);
	h.setScore(score);
	for (Integer i : getPos()) {
	    h.addPos(i.intValue());
	}
	return h;
    }
}
