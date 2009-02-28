/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.scoring;

import java.util.List;

/**
 * 频率打分器，完全按照每个term的出现频率打分
 * @note 需要的打分参数：paragraph
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

	@Override
	public double getScore(String term, List<Integer> pos) {
		// TODO Auto-generated method stub
		return pos.size();
	}


}
