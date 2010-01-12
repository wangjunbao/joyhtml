/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.scoring;

import java.util.List;

/**
 * score all the terms 0
 * 
 * @author Song Liu(lamfeeling2@gmail.com)
 */
public class ZeroScorer extends Scorer {

    public ZeroScorer() {
    }

    @Override
    public double getScore(String term) {
	return .0;
    }

    @Override
    public double getScore(String term, List<Integer> pos) {
	return 0;
    }
}
