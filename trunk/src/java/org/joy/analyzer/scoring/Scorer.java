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
public abstract class Scorer {

    protected List<Paragraph> paragraphs;

    public Scorer() {
    }

    public void load(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public abstract double getScore(String term);
}
