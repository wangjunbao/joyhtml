/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joy.analyzer.terms;

import java.util.HashSet;
import org.joy.nlp.Word;

/**
 *
 * @author Administrator
 */
public abstract class TermExtractor {

    protected Word[] words;

    public TermExtractor() {
    }

    public void load(Word[] words) {
        this.words = words;
    }

    public abstract HashSet<String> getTerms();
}
