/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.scoring;

/**
 *
 * @author Administrator
 */
public class ZeroScorer extends Scorer {

    public ZeroScorer() {
        super(null);
    }

    @Override
    public double getScore(String term) {
        return .0;
    }
}
