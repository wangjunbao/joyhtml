/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.terms;

import java.util.HashSet;

import org.joy.nlp.Word;

/**
 * SimpleTermExtractor simply returns the segmented words as indexable terms
 * 
 * @author Song Liu(lamfeeling2@Gmail.com)
 */
public class SimpleTermExtractor extends TermExtractor {

    @Override
    public HashSet<String> getTerms() {
	// return the segmented words
	HashSet<String> termSet = new HashSet<String>();
	for (Word w : words) {
	    // ignore the case
	    termSet.add(w.getText());
	}
	return termSet;
    }
}
