/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.scoring;

import java.util.List;

import org.joy.analyzer.Paragraph;

/**
 * Paragraph Weighted Frequency Scorer，scores using weighted frequency
 * 
 * @author Song Liu(lamfeeling2@gmail.com)
 */
public class PWFScorer extends Scorer {

    public PWFScorer() {
    }

    @Override
    public double getScore(String term) {
	double score = .0;
	for (Paragraph p : paragraphs) {
	    int freqInP = 0, index;
	    index = p.getText().indexOf(term);
	    while (index != -1) {
		freqInP++;
		index = p.getText().indexOf(term, index + 1);
	    }
	    score += freqInP * p.getWeight();
	}
	return score * Math.log(term.length());
    }

    @Override
    public double getScore(String term, List<Integer> pos) {
	double score = .0;
	for (int i : pos) {
	    for (Paragraph p : paragraphs) {
		if (i >= p.getOffset()
			&& i < p.getOffset() + p.getText().length()) {
		    score += p.getWeight();
		    break;
		}
	    }
	}
	return score * Math.log(term.length() + 1);
    }
}
