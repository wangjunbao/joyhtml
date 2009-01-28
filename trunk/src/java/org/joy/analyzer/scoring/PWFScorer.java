/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.scoring;

import org.joy.analyzer.*;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class PWFScorer extends Scorer {

    public PWFScorer(List<Paragraph> paragraphs) {
        super(paragraphs);
    }

    private void doAnalyze() {
    }

    @Override
    public double getScore(String term) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
