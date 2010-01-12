/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.scoring;

import java.util.List;

/**
 * FrequencyScorer，scores as the frequency of each word
 * 
 * @author Song Liu (lamfeeling2@gmail.com)
 */
public class FrequencyScorer extends Scorer {

    @Override
    public double getScore(String term) {
	// Score on the basis of frequency
	int freq = 0, index;
	index = fulltext.indexOf(term);
	while (index != -1) {
	    freq += 1;
	    index = fulltext.indexOf(term, index + 1);
	}
	return freq;
    }

    @Override
    public double getScore(String term, List<Integer> pos) {
	return pos.size();
    }

}
