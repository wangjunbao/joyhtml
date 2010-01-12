/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.scoring;

import java.util.List;
import java.util.Set;

import org.joy.analyzer.Paragraph;

/**
 * Scorer, scores all the terms using specific criteria.
 * 
 * @author Song Liu(lamfeeling2@gmail.com)
 */
public abstract class Scorer {

    protected List<Paragraph> paragraphs;
    protected Set<String> termSet;
    protected String fulltext = "";

    public Scorer() {
    }

    /**
     * Set terms which are being scored
     * 
     * @param termSet
     *            Term Set
     */
    public void setTermSet(Set<String> termSet) {
	this.termSet = termSet;
    }

    /**
     * Set Paragraphs used in scoring
     * 
     * @param paragraphs
     *            Paragraph List
     */
    public void setParagraphs(List<Paragraph> paragraphs) {
	this.paragraphs = paragraphs;
	StringBuffer sb = new StringBuffer();
	for (Paragraph p : paragraphs) {
	    sb.append(p.getText());
	}
	fulltext = sb.toString();
    }

    /**
     * Get score for specified term
     * 
     * @param term
     *            term
     * @return score of the term you queried
     */
    public abstract double getScore(String term);

    /**
     * Get the score for specified term and given positions of this term in
     * document
     * 
     * @param term
     *            score of which you want to get
     * @param pos
     *            term
     * @return score of the term you queried
     */
    public abstract double getScore(String term, List<Integer> pos);
}
