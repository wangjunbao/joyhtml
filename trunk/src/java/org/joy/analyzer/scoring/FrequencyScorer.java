/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.scoring;

/**
 *
 * @author Administrator
 */
public class FrequencyScorer extends Scorer {

    @Override
    public double getScore(String term) {
        //直接把频率作为打分依据
        int freq = 0, index;
        index = fulltext.indexOf(term);
        while (index != -1) {
            freq += 1;
            index = fulltext.indexOf(term, index + 1);
        }
        return freq;
    }
}
