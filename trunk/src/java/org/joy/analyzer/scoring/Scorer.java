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
    protected String fulltext="";

    public Scorer() {
    }

    public void load(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
        for (Paragraph p : paragraphs) {
            fulltext += p.getText();
        }
    }

    public abstract double getScore(String term);
}
